package com.blotout.repository

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import com.blotout.DependencyInjectorImpl
import com.blotout.data.database.EventDatabaseService
import com.blotout.data.database.entity.EventEntity
import com.blotout.model.Event
import com.blotout.model.Events
import com.blotout.model.Meta
import com.blotout.model.Screen
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

    fun preparePersonalEvent(eventName: String, eventInfo: HashMap<String, Any>, isPHI: Boolean) {
        if(secureStorage.fetchBoolean(Constant.IS_SDK_ENABLE)) {
            val context = DependencyInjectorImpl.getInstance().getContext()
            val event = Event()
            event.mid = eventName.getMessageIDForEvent()
            event.userid = CommonUtils().getDeviceId()
            event.evn = eventName
            event.screen = Screen(context)
            event.evt = DateTimeUtils().get13DigitNumberObjTimeStamp()
            event.evcs = eventName.codeForDevEvent()
            event.sessionId = DependencyInjectorImpl.getSessionId().toString()


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

            pushEvents(event)
        }
    }


    fun prepareCodifiedEvent(eventName: String, eventInfo: HashMap<String, Any>, eventCode: Int) {
        if(secureStorage.fetchBoolean(Constant.IS_SDK_ENABLE)) {
            val event = Event()
            val context = DependencyInjectorImpl.getInstance().getContext()
            event.mid = eventName.getMessageIDForEvent()
            event.type = Constant.BO_CODIFIED
            event.userid = CommonUtils().getDeviceId()
            event.evn = eventName
            event.screen = Screen(context)
            event.evt = DateTimeUtils().get13DigitNumberObjTimeStamp()
            event.evcs = if (eventCode != 0) eventCode else eventName.codeForDevEvent()
            event.sessionId = DependencyInjectorImpl.getSessionId().toString()
            event.additionalData = eventInfo
            pushEvents(event)
        }
    }

    fun prepareSystemEvent(activity: Activity, eventName: String, eventInfo: HashMap<String, Any>?, withEventCode: Int) {
        if(DependencyInjectorImpl.getInstance().getManifestRepository().sdkPushSystemEvents ||
                secureStorage.fetchBoolean(Constant.IS_SDK_ENABLE))
        {
            val event = Event()
            val context = DependencyInjectorImpl.getInstance().getContext()
            event.mid = eventName.getMessageIDForEvent()
            event.type = Constant.BO_SYSTEM
            event.userid = CommonUtils().getDeviceId()
            event.evn = eventName
            event.scrn = activity.getScreenName()
            event.screen = Screen(context)
            event.evt = DateTimeUtils().get13DigitNumberObjTimeStamp()
            event.evcs = withEventCode
            event.sessionId = DependencyInjectorImpl.getSessionId().toString()
            event.additionalData = eventInfo
            pushEvents(event)
        }
    }

    private fun pushEvents(event: Event) {
        val events = Events()
        events.meta = prepareMetaData()
        var eventList = mutableListOf<Event>()
        eventList.add(event)
        events.events = eventList
        var eventEntity = EventEntity(Gson().toJson(events))
        EventDatabaseService().insertEvent(eventEntity)
    }

    fun Activity.getScreenName(): String  {
        val packageManager = this.packageManager
        return packageManager.getActivityInfo(
                this.componentName, PackageManager.GET_META_DATA).loadLabel(packageManager).toString()
    }

    fun publishEvent(){
        EventDatabaseService().getEvents()
    }

}