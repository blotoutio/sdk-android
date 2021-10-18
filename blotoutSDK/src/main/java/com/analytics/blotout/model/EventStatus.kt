package com.analytics.blotout.model

interface EventStatus {
        fun onSuccess()
        fun onError()
}