package com.analytics.blotout.model

import com.analytics.blotout.model.ErrorCodes.ERROR_CODE_NO_ERROR

data class InternalError(val code : Int = ERROR_CODE_NO_ERROR, val msg : String = "") {
    fun hasError() =code!=ERROR_CODE_NO_ERROR
}
