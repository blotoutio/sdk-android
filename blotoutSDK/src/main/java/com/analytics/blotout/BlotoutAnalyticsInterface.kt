package com.analytics.blotout

import android.app.Application
import com.analytics.blotout.model.CompletionHandler
import com.analytics.blotout.model.MapIDData

interface BlotoutAnalyticsInterface {

    fun init(context: Application, blotoutAnalyticsConfiguration: BlotoutAnalyticsConfiguration,completionHandler: CompletionHandler)

    fun enable( enabled:Boolean)

    fun capture( eventName :String, eventInfo:HashMap<String, Any>)

    fun capturePersonal(eventName :String, eventInfo:HashMap<String, Any> ,isPHI : Boolean)

    fun mapID(mapIDData: MapIDData, withInformation:HashMap<String,Any>?)

    fun getUserId():String
}