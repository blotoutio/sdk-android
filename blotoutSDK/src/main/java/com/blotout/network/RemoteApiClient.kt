package com.blotout.network

import com.blotout.DependencyInjectorImpl
import com.blotout.util.Constant
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RemoteApiClient(private val hostConfiguration: HostConfiguration) {

    private fun getAdapter(): Retrofit {
        return Retrofit.Builder().run{
            baseUrl(hostConfiguration.baseUrl)
            addConverterFactory(GsonConverterFactory.create())
            addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            addCallAdapterFactory(RetryCallAdapterFactory.create())
            client(createOkHttpClient())
            build()
        }

    }

    fun getRemoteApiService(): RemoteApiService {
        return getAdapter().create(RemoteApiService::class.java)
    }

    private fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().run {
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })

            .addInterceptor { chain ->
                val request: Request = chain.request().newBuilder().addHeader("token", hostConfiguration.baseKey!!).build()
                chain.proceed(request)
            }

            connectTimeout(hostConfiguration.mConnectionTimeout, TimeUnit.MILLISECONDS)
            readTimeout(hostConfiguration.mReadTimeout, TimeUnit.MILLISECONDS)
            writeTimeout(hostConfiguration.mWriteTimeout, TimeUnit.MILLISECONDS)
            Dispatcher().maxRequests = 10
            build()
        }
    }

}