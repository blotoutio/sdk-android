package com.blotout

import android.app.Application

interface BlotoutAnalyticsInterface {

    fun init(context: Application, blotoutAnalyticsConfiguration: BlotoutAnalyticsConfiguration)

    fun enable( enabled:Boolean)

    fun capture( eventName :String, eventInfo:HashMap<String, Any>)

    fun capturePersonal(eventName :String, eventInfo:HashMap<String, Any> ,isPHI : Boolean)

    fun mapID(userId:String, provider:String, withInformation:HashMap<String,Any>)

    fun getUserId():String
}