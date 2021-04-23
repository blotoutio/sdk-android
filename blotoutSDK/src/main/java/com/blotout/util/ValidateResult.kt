package com.blotout.util

data class ValidateResult(val code : Int = Errors.ERROR_CODE_NO_ERROR , val message : String?=null) {
    fun hasError() = code != Errors.ERROR_CODE_NO_ERROR
}