package com.blotout.repository

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import com.blotout.DependencyInjectorImpl
import com.blotout.data.database.EventDatabaseService
import com.blotout.data.database.entity.EventEntity
import com.blotout.model.*
import com.blotout.repository.data.SharedPrefernceSecureVault
import com.blotout.util.*
import com.google.gson.Gson
import java.lang.reflect.Field

class EventRepository(var secureStorage: SharedPrefernceSecureVault) {

    fun prepareMetaData(): Meta {
        val meta = Meta()
        val context = DependencyInjectorImpl.getInstance().getContext()
        meta.sdkv = "".getVersion()
        meta.tzOffset = DateTimeUtils().getCurrentTimezoneOffsetInMin()
        meta.userIdCreated = CommonUtils().getUserBirthTimeStamp()
        meta.plf = Constant.BO_Android_All
        meta.osv = Build.VERSION.RELEASE
        meta.appv = context.getVersion()
        meta.dmft = Build.MANUFACTURER
        meta.dm = Build.MODEL
        val fields: Array<Field> = Build.VERSION_CODES::class.java.fields
        val osName: String = fields[Build.VERSION.SDK_INT].name
        meta.osn = osName
        return meta
    }

    fun preparePersonalEvent(eventName: String, eventInfo: HashMap<String, Any>, isPHI: Boolean) :EventStatus{
        if(secureStorage.fetchBoolean(Constant.IS_SDK_ENABLE)) {
            val event = prepareEvents(eventName,0)
            when (isPHI) {
                true -> {
                    event.type = Constant.BO_PHI
                    event.additionalData = DependencyInjectorImpl.getInstance().getManifestRepository().sdkPHIPublicKey?.let { Gson().toJson(eventInfo).encrypt(it) }
                }
                else -> {
                    event.type = Constant.BO_PII
                    event.additionalData = DependencyInjectorImpl.getInstance().getManifestRepository().sdkPIIPublicKey?.let { Gson().toJson(eventInfo).encrypt(it) }
                }
            }
            return pushEvents(event)
        }
        return EventStatus()
    }


    fun prepareCodifiedEvent(eventName: String, eventInfo: HashMap<String, Any>, withEventCode: Int) :EventStatus{
        if(secureStorage.fetchBoolean(Constant.IS_SDK_ENABLE)) {
            val event = prepareEvents(eventName,withEventCode)
            event.type = Constant.BO_CODIFIED
            event.additionalData = eventInfo
            return pushEvents(event)
        }
        return EventStatus()
    }

    fun prepareSystemEvent(activity: Activity?, eventName: String, eventInfo: HashMap<String, Any>?, withEventCode: Int):EventStatus {
        if(DependencyInjectorImpl.getInstance().getManifestRepository().sdkPushSystemEvents &&
                secureStorage.fetchBoolean(Constant.IS_SDK_ENABLE))
        {
            val event = prepareEvents(eventName,withEventCode)
            if(activity !=null)
                event.scrn = activity!!.localClassName.substringAfterLast(delimiter = '.')
            event.type = Constant.BO_SYSTEM
            event.additionalData = eventInfo
            return pushEvents(event)
        }
        return EventStatus()
    }

    private fun pushEvents(event: Event):EventStatus {
        val events = Events()
        events.meta = prepareMetaData()
        var eventList = mutableListOf<Event>()
        eventList.add(event)
        events.events = eventList
        var eventEntity = EventEntity(Gson().toJson(events))
        EventDatabaseService().insertEvent(eventEntity)
        var eventStatus = EventStatus()
        eventStatus.isSuccess = true
        return eventStatus
    }

    fun Activity.getScreenName(): String  {
        val packageManager = this.packageManager
        return packageManager.getActivityInfo(
                this.componentName, PackageManager.GET_META_DATA).loadLabel(packageManager).toString()
    }

    fun publishEvent(){
        EventDatabaseService().getEvents()
    }

    private fun prepareEvents(eventName: String,  withEventCode: Int):Event{
        val event = Event()
        val context = DependencyInjectorImpl.getInstance().getContext()
        event.mid = eventName.getMessageIDForEvent()
        event.userid = CommonUtils().getUserID()
        event.evn = eventName
        event.screen = Screen(context)
        event.evt = DateTimeUtils().get13DigitNumberObjTimeStamp()
        event.evcs = if (withEventCode != 0) withEventCode else eventName.codeForDevEvent()
        event.sessionId = DependencyInjectorImpl.getSessionId().toString()
        return event
    }

}