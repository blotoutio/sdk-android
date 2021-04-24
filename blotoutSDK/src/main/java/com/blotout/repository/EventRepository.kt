package com.blotout.repository

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import com.blotout.DependencyInjectorImpl
import com.blotout.model.Event
import com.blotout.model.Events
import com.blotout.model.Meta
import com.blotout.model.Screen
import com.blotout.network.ApiDataProvider
import com.blotout.repository.data.SharedPrefernceSecureVault
import com.blotout.util.*
import com.google.gson.Gson
import retrofit2.Call
import java.lang.reflect.Field

class EventRepository(var secureStorage: SharedPrefernceSecureVault) {

    fun prepareMetaData(): Meta {
        val meta = Meta()
        val context = DependencyInjectorImpl.getInstance().getContext()
        meta.sdkv = secureStorage.fetchString(Constant.BO_VERSION_KEY)
        meta.tzOffset = DateTimeUtils().getCurrentTimezoneOffsetInMin()
        meta.userIdCreated = CommonUtils().getUUID()
        meta.plf = Constant.BO_Android_All
        meta.appn = context.packageManager.getApplicationLabel(context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)).toString()
        meta.osv = Build.VERSION.RELEASE
        meta.appv = context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_META_DATA).versionName
        meta.dmft = Build.MANUFACTURER
        meta.dm = Build.MODEL
        val fields: Array<Field> = Build.VERSION_CODES::class.java.fields
        val osName: String = fields[Build.VERSION.SDK_INT].name
        meta.osn = osName
        return meta
    }

    fun preparePersonalEvent(eventName: String, eventInfo: HashMap<String, Any>, isPHI: Boolean) {
        val context = DependencyInjectorImpl.getInstance().getContext()
        val event = Event()
        event.sdkv = (Constant.BOSDK_MAJOR_VERSION+Constant.BOSDK_MINOR_VERSION+Constant.BOSDK_PATCH_VERSION).toString()
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


    fun prepareCodifiedEvent(eventName: String, eventInfo: HashMap<String, Any>, eventCode: Int) {
        val event = Event()
        val context = DependencyInjectorImpl.getInstance().getContext()
        event.sdkv = (Constant.BOSDK_MAJOR_VERSION+Constant.BOSDK_MINOR_VERSION+Constant.BOSDK_PATCH_VERSION).toString()
        event.mid = eventName.getMessageIDForEvent()
        event.type = Constant.BO_CODIFIED
        event.userid = CommonUtils().getDeviceId()
        event.evn = eventName
        event.screen = Screen(context)
        event.evt = DateTimeUtils().get13DigitNumberObjTimeStamp()
        event.evcs = if(eventCode!=0) eventCode else eventName.codeForDevEvent()
        event.sessionId = DependencyInjectorImpl.getSessionId().toString()
        event.additionalData = eventInfo
        pushEvents(event)
    }

    fun prepareSystemEvent(activity: Activity, eventName: String, eventInfo: HashMap<String, Any>?, withEventCode: Int) {
        if(DependencyInjectorImpl.getInstance().getManifestRepository().sdkPushSystemEvents) {
            val event = Event()
            val context = DependencyInjectorImpl.getInstance().getContext()
            event.sdkv = (Constant.BOSDK_MAJOR_VERSION + Constant.BOSDK_MINOR_VERSION + Constant.BOSDK_PATCH_VERSION).toString()
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
        DependencyInjectorImpl.getInstance().getConfigurationManager().publishEvents(events, object : ApiDataProvider<Any?>() {
            override fun onFailed(errorCode: Int, message: String, call: Call<Any?>) {
            }

            override fun onError(t: Throwable, call: Call<Any?>) {
            }

            override fun onSuccess(data: Any?) {
            }

        })
    }

    fun Activity.getScreenName(): String  {
        val packageManager = this.packageManager
        return packageManager.getActivityInfo(
                this.componentName, PackageManager.GET_META_DATA).loadLabel(packageManager).toString()
    }

}