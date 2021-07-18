package com.analytics.blotout.repository

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import com.analytics.blotout.DependencyInjectorImpl
import com.analytics.blotout.data.database.EventDatabaseService
import com.analytics.blotout.data.database.entity.EventEntity
import com.analytics.blotout.deviceinfo.DeviceAndAppFraudController
import com.analytics.blotout.deviceinfo.device.DeviceInfo
import com.analytics.blotout.model.*
import com.analytics.blotout.repository.data.SharedPreferenceSecureVault
import com.analytics.blotout.util.*
import com.google.gson.Gson
import java.lang.reflect.Field

class EventRepository(var secureStorage: SharedPreferenceSecureVault) {

    private fun prepareMetaData(): Meta {
        val meta = Meta()
        try {
            val context = DependencyInjectorImpl.getInstance().getContext()
            val deviceAndAppFraudController = DeviceAndAppFraudController(context)
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
            meta.referrer = DependencyInjectorImpl.getInstance().mReferrerDetails?.installReferrer
            meta.jbrkn = DeviceInfo(context).isDeviceRooted
            meta.vpn = deviceAndAppFraudController.isVPN()
            meta.dcomp = deviceAndAppFraudController.isDeviceCompromised()
            meta.acomp = deviceAndAppFraudController.isAppCompromised()
        } catch (e: Exception) { }
        return meta
    }

    fun preparePersonalEvent(eventName: String, eventInfo: HashMap<String, Any>, isPHI: Boolean): EventStatus {
        try {
            if (secureStorage.fetchBoolean(Constant.IS_SDK_ENABLE)) {
                val event = prepareEvents(eventName, 0)
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
        } catch (e: Exception) {
        }
        return EventStatus()
    }


    fun prepareCodifiedEvent(eventName: String, eventInfo: HashMap<String, Any>, withEventCode: Int): EventStatus {
        try {
            if (secureStorage.fetchBoolean(Constant.IS_SDK_ENABLE)) {
                val event = prepareEvents(eventName, withEventCode)
                event.type = Constant.BO_CODIFIED
                event.additionalData = eventInfo
                return pushEvents(event)
            }
        } catch (e: Exception) {
        }
        return EventStatus()
    }

    fun prepareSystemEvent(activity: Activity?, eventName: String, eventInfo: HashMap<String, Any>?, withEventCode: Int): EventStatus? {
        try {
            var manifestRepository = DependencyInjectorImpl.getInstance().getManifestRepository()
            //is all system events allowed
            var isAllSystemEventsAllowed = manifestRepository.sdkPushSystemEvents
            when (isAllSystemEventsAllowed) {
                false -> {
                    //now check what system events allowed
                    when (withEventCode) {
                        Constant.BO_EVENT_SDK_START,
                        Constant.BO_EVENT_VISIBILITY_VISIBLE,
                        Constant.BO_EVENT_VISIBILITY_HIDDEN -> {

                        }
                        else -> {
                            var filterEventCode = manifestRepository.sdkSystemEevntsAllowed?.filter { it.key == withEventCode }
                            if (filterEventCode.isNullOrEmpty())
                                return null
                        }
                    }

                }
            }
            if (secureStorage.fetchBoolean(Constant.IS_SDK_ENABLE)) {

                val event = prepareEvents(eventName, withEventCode)
                if (activity != null)
                    event.scrn = activity!!.localClassName.substringAfterLast(delimiter = '.')
                event.type = Constant.BO_SYSTEM
                event.additionalData = eventInfo
                return pushEvents(event)
            }
        } catch (e: Exception) {
        }
        return EventStatus()
    }

    private fun pushEvents(event: Event): EventStatus {
        var eventStatus = EventStatus()
        try {
            val events = Events()
            events.meta = prepareMetaData()
            var eventList = mutableListOf<Event>()
            eventList.add(event)
            events.events = eventList
            var eventEntity = EventEntity(Gson().toJson(events))
            EventDatabaseService().insertEvent(eventEntity)

            eventStatus.isSuccess = true
        }catch (e:Exception){}
        return eventStatus
    }

    fun Activity.getScreenName(): String {
        val packageManager = this.packageManager
        return packageManager.getActivityInfo(
                this.componentName, PackageManager.GET_META_DATA).loadLabel(packageManager).toString()
    }

    fun publishEvent() {
        EventDatabaseService().getEvents()
    }

    private fun prepareEvents(eventName: String, withEventCode: Int): Event {
        val event = Event()
        try {
            val context = DependencyInjectorImpl.getInstance().getContext()
            event.mid = eventName.getMessageIDForEvent()
            event.userid = CommonUtils().getUserID()
            event.evn = eventName
            event.screen = Screen(context)
            event.evt = DateTimeUtils().get13DigitNumberObjTimeStamp()
            event.evcs = if (withEventCode != 0) withEventCode else eventName.codeForDevEvent()
            event.sessionId = DependencyInjectorImpl.getSessionId().toString()
        }catch (e:Exception){}
        return event
    }

}