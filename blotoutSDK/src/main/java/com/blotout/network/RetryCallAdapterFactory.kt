package com.blotout.network

import android.util.Log
import okhttp3.Request
import okio.Timeout
import retrofit2.*
import java.io.IOException
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicInteger

class RetryCallAdapterFactory : CallAdapter.Factory() {
    override fun get(
            returnType: Type, annotations: Array<Annotation>,
            retrofit: Retrofit
    ): CallAdapter<*, *>? {
        val maxRetries: Int
        if(annotations.isEmpty()) return null
        val retry = getRetry(annotations)
        when {
            retry != null -> maxRetries = retry.max
            else -> return null
        }
        Log.d(TAG, "Starting a CallAdapter with $maxRetries retries")
        return RetryCallAdapter(
                retrofit.nextCallAdapter(this, returnType, annotations),
                maxRetries
        )
    }

    private fun getRetry(annotations: Array<Annotation>): Retry? {
        return annotations.firstOrNull { it is Retry } as Retry?
    }

    internal class RetryCallAdapter<R, T>(
            private val delegated: CallAdapter<R, T>,
            private val maxRetries: Int
    ) : CallAdapter<R, T> {
        override fun responseType(): Type {
            return delegated.responseType()
        }

        override fun adapt(call: Call<R>): T {
            return delegated.adapt(if (maxRetries > 0) RetryingCall(call, maxRetries) else call)
        }
    }

    internal class RetryingCall<R>(private val delegated: Call<R>, private val maxRetries: Int,
                                   private var executeRetryCount: AtomicInteger = AtomicInteger()
    ) :
            Call<R> {

        @Throws(IOException::class)
        override fun execute(): Response<R> {
            fun isCallRetryable(): Boolean {
                val retryable = executeRetryCount.incrementAndGet() <= maxRetries
                Log.d(TAG, "Retryable State: $retryable Counts: $executeRetryCount Max Retries $maxRetries")
                return retryable
            }

            fun retryCall(): Response<R> {
                Log.d(TAG, "${executeRetryCount.get()} / $maxRetries: Retrying Call")
                val retryCall = this.clone() as RetryingCall
                retryCall.executeRetryCount = executeRetryCount
                return retryCall.execute()
            }

            fun retryFailedCall(e: Exception): Response<R> {
                Log.e(TAG, "Error executing retry call", e)
                // Retry Call if possible. Throw error if not.
                return when (isCallRetryable()) {
                    true -> retryCall()
                    false -> throw e
                }
            }

            val response: Response<R>
            return try {
                response = delegated.execute()
                when {
                    !response.isSuccessful && isCallRetryable() -> {
                        Log.d(TAG, "Execute call with no success result code: ${response.code()}")
                        retryCall()
                    }
                    else -> {
                        response
                    }
                }
            } catch (e: IOException) {
                retryFailedCall(e)
            } catch (e: RuntimeException){
                retryFailedCall(e)
            }
        }

        override fun enqueue(callback: Callback<R>) {
            delegated.enqueue(
                    RetryCallback(
                            delegated, callback, maxRetries
                    )
            )
        }

        override fun isExecuted(): Boolean {
            return delegated.isExecuted
        }

        override fun cancel() {
            delegated.cancel()
        }

        override fun isCanceled(): Boolean {
            return delegated.isCanceled
        }

        override fun clone(): Call<R> {
            return RetryingCall(delegated.clone(), maxRetries)
        }

        override fun request(): Request {
            return delegated.request()
        }

        override fun timeout(): Timeout {
            return delegated.timeout()
        }
    }

    internal class RetryCallback<T>(
            private val call: Call<T>,
            private val callback: Callback<T>,
            private val maxRetries: Int
    ) : Callback<T> {

        private val retryCount = AtomicInteger(0)

        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (!response.isSuccessful && retryCount.incrementAndGet() <= maxRetries) {
                Log.d(TAG, "Call with no success result code: ${response.code()}")
                retryCall()
            } else {
                callback.onResponse(call, response)
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            Log.d(TAG, "Call failed with message: ${t.message}", t)
            when {
                retryCount.incrementAndGet() <= maxRetries -> retryCall()
                else -> {
                    callback.onFailure(call, t)
                }
            }
        }

        private fun retryCall() {
            Log.d(TAG, "${retryCount.get()} / $maxRetries: Retrying Call")
            call.clone().enqueue(this)
        }
    }

    companion object {
        private const val TAG = "RetryCallAdapterFactory"
        fun create(): RetryCallAdapterFactory {
            return RetryCallAdapterFactory()
        }
    }
}