package com.analytics.blotout

import android.app.Application
import com.analytics.blotout.model.EventStatus

interface BlotoutAnalyticsInterface {

    fun init(context: Application, blotoutAnalyticsConfiguration: BlotoutAnalyticsConfiguration)

    fun enable( enabled:Boolean)

    fun capture( eventName :String, eventInfo:HashMap<String, Any>): EventStatus

    fun capturePersonal(eventName :String, eventInfo:HashMap<String, Any> ,isPHI : Boolean):EventStatus

    fun mapID(userId:String?, provider:String?, withInformation:HashMap<String,Any>?):EventStatus

    fun getUserId():String
}