package com.analytics.blotout.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class ApiDataProvider<T> : Callback<T> {

    abstract fun onFailed(errorCode: Int, message: String, call: Call<T>)

    abstract fun onError(t: Throwable, call: Call<T>)

    abstract fun onSuccess(data: T?)

    override fun onFailure(call: Call<T>, t: Throwable) {
        onError(t, call)
    }

    override fun onResponse(call: Call<T>, response: Response<T>) {
        try {
            if (response.isSuccessful) {
                onSuccess(response.body())
            } else {
                onFailed(
                        response.code(),
                        response.errorBody()?.string() ?: "",
                        call
                )
            }
        } catch (e: Exception) {
            onError(e, call)
        }
    }
}