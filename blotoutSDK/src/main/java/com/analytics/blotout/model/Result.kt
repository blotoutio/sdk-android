package com.analytics.blotout.model

sealed class Result<out R> {
    data class Success<out T>(val data:T):Result<T>()
    data class Error(val errorData:Throwable):Result<Nothing>()
}