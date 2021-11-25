package com.analytics.blotout

import android.app.Application
import com.analytics.blotout.model.*

interface BlotoutAnalyticsInterface {

    fun init(application: Application, blotoutAnalyticsConfiguration: BlotoutAnalyticsConfiguration,completionHandler: CompletionHandler)

    fun enable( enabled:Boolean)

    fun capture( eventName :String, eventInfo:HashMap<String, Any>)

    fun capturePersonal(eventName :String, eventInfo:HashMap<String, Any> ,isPHI : Boolean)

    fun mapID(mapIDData: MapIDData, withInformation:HashMap<String,Any>?)

    fun getUserId():String

    fun transaction(transactionData: TransactionData, withInformation:HashMap<String,Any>?)

    fun item(itemData: Item, withInformation:HashMap<String,Any>?)

    fun persona(personaData: Persona, withInformation:HashMap<String,Any>?)
}