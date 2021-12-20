package com.analytics.blotout.repository

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import com.analytics.blotout.DependencyInjectorImpl
import com.analytics.blotout.data.database.EventDatabaseService
import com.analytics.blotout.data.database.entity.EventEntity
import com.analytics.blotout.deviceinfo.DeviceAndAppFraudController
import com.analytics.blotout.deviceinfo.device.DeviceInfo
import com.analytics.blotout.model.*
import com.analytics.blotout.repository.data.SharedPreferenceSecureVault
import com.analytics.blotout.util.*
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.lang.reflect.Field
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class EventRepository(private var secureStorage: SharedPreferenceSecureVault) {

    companion object {
        private const val TAG = "EventRepository"
    }

    lateinit var visibleActivity: Activity

    private suspend fun prepareMetaData() = suspendCoroutine<Result<Meta>> { continuation ->
        try {
            val meta = Meta()
            val context = DependencyInjectorImpl.getInstance().getApplication()
            val deviceAndAppFraudController = DeviceAndAppFraudController(context)
            meta.sdkv = "".getVersion()
            meta.tzOffset = DateTimeUtils().getCurrentTimezoneOffsetInMin()
            meta.user_agent = DeviceInfo(context).userAgent
            meta.appv = context.getVersion()
            meta.referrer =
                DependencyInjectorImpl.getInstance().mReferrerDetails?.installReferrer
            meta.jbrkn = DeviceInfo(context).isDeviceRooted
            meta.vpn = deviceAndAppFraudController.isVPN()
            meta.dcomp = deviceAndAppFraudController.isDeviceCompromised()
            meta.acomp = deviceAndAppFraudController.isAppCompromised()
            if (this::visibleActivity.isInitialized) {
                meta.page_title = visibleActivity.getScreenName()
            }
            continuation.resume(Result.Success(meta))
        } catch (e: Exception) {
            continuation.resume(Result.Error(InternalError(code = ErrorCodes.ERROR_CODE_SDK_INTERNAL_ERROR, msg = e.localizedMessage!!)))
            Log.e(TAG, e.localizedMessage!!)
        }
    }

    suspend fun preparePersonalEvent(
        eventName: String,
        eventInfo: HashMap<String, Any>,
        isPHI: Boolean
    ): Result<String> {
        try {
            if (secureStorage.fetchBoolean(Constant.IS_SDK_ENABLE)) {
                val event = prepareEvents(eventName)
                when (isPHI) {
                    true -> {
                        event.type = Constant.BO_PHI
                        event.additionalData = DependencyInjectorImpl.getInstance()
                            .getManifestRepository().sdkPHIPublicKey?.let {
                            Gson().toJson(eventInfo).encrypt(it)
                        }
                    }
                    else -> {
                        event.type = Constant.BO_PII
                        event.additionalData = DependencyInjectorImpl.getInstance()
                            .getManifestRepository().sdkPIIPublicKey?.let {
                            Gson().toJson(eventInfo).encrypt(it)
                        }
                    }
                }
                return pushEvents(event)
            } else {
                return Result.Error(InternalError(code=ErrorCodes.ERROR_CODE_SDK_NOT_ENABLED,msg="SDK is not enabled"))
            }
        } catch (e: Exception) {
            Log.e(TAG, e.localizedMessage!!)
            return Result.Error(InternalError(code = ErrorCodes.ERROR_CODE_SDK_INTERNAL_ERROR, msg = e.localizedMessage!!))
        }
    }


    suspend fun prepareCodifiedEvent(
        eventName: String,
        eventInfo: HashMap<String, Any>
    ): Result<String> {
        return try {
            if (secureStorage.fetchBoolean(Constant.IS_SDK_ENABLE)) {
                val event = prepareEvents(eventName)
                event.type = Constant.BO_CODIFIED
                event.additionalData = eventInfo
                pushEvents(event)
            } else {
                Result.Error(InternalError(code=ErrorCodes.ERROR_CODE_SDK_NOT_ENABLED,msg="SDK is not enabled"))
            }
        } catch (e: Exception) {
            Log.e(TAG, e.localizedMessage!!)
            Result.Error(InternalError(code = ErrorCodes.ERROR_CODE_SDK_INTERNAL_ERROR, msg = e.localizedMessage!!))
        }
    }

    fun prepareSystemEvent(
        activity: Activity?,
        eventName: String,
        eventInfo: HashMap<String, Any>?,
        withEventCode: Int
    ) {
        try {
            if (activity != null) {
                visibleActivity = activity
            }
            val manifestRepository = DependencyInjectorImpl.getInstance().getManifestRepository()
            //is all system events allowed
            when (manifestRepository.sdkPushSystemEvents) {
                false -> {
                    //now check what system events allowed
                    when (withEventCode) {
                        Constant.BO_EVENT_SDK_START,
                        Constant.BO_EVENT_VISIBILITY_VISIBLE,
                        Constant.BO_EVENT_VISIBILITY_HIDDEN -> {

                        }
                        else -> {
                            val filterEventCode =
                                manifestRepository.sdkSystemEevntsAllowed?.filter { it.key == withEventCode }
                            if (filterEventCode.isNullOrEmpty())
                                return
                        }
                    }

                }
            }
            if (secureStorage.fetchBoolean(Constant.IS_SDK_ENABLE)) {
                val event = prepareEvents(eventName)
                if (activity != null)
                    event.scrn = activity.localClassName.substringAfterLast(delimiter = '.')
                event.type = Constant.BO_SYSTEM
                event.additionalData = eventInfo
                CoroutineScope(Dispatchers.Default).launch {
                    when (pushEvents(event)) {
                        is Result.Success -> {
                        }
                        is Result.Error -> {
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, e.localizedMessage!!)
        }
    }

    private suspend fun pushEvents(event: Event): Result<String> {
        return try {
            val events = Events()
            when (val metaPreparationResult = prepareMetaData()) {
                is Result.Success -> events.meta = metaPreparationResult.data
                is Result.Error -> Result.Error(metaPreparationResult.errorData)
            }
            val eventList = mutableListOf<Event>()
            eventList.add(event)
            events.events = eventList
            val eventEntity = EventEntity(Gson().toJson(events))
            EventDatabaseService().insertEvent(eventEntity)
            Result.Success("")
        } catch (e: Exception) {
            Log.e(TAG, e.localizedMessage!!)
            Result.Error(InternalError(code = ErrorCodes.ERROR_CODE_SDK_INTERNAL_ERROR, msg = e.localizedMessage!!))
        }
    }

    fun Activity.getScreenName(): String {
        val packageManager = this.packageManager
        return packageManager.getActivityInfo(
            this.componentName, PackageManager.GET_META_DATA
        ).loadLabel(packageManager).toString()
    }

    fun publishEvent() {
        EventDatabaseService().getEvents()
    }

    private fun prepareEvents(eventName: String): Event {
        val event = Event()
        try {
            val context = DependencyInjectorImpl.getInstance().getApplication()
            event.mid = eventName.getMessageIDForEvent()
            event.userid = CommonUtils().getUserID()
            event.evn = eventName
            event.screen = Screen(context)
            event.evt = DateTimeUtils().get13DigitNumberObjTimeStamp()
            event.sessionId = DependencyInjectorImpl.getSessionId().toString()
        } catch (e: Exception) {
            Log.e(TAG, e.localizedMessage!!)
        }
        return event
    }

}