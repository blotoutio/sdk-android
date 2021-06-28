package com.blotout

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks2
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.DefaultLifecycleObserver
import com.blotout.geasture.Gesture
import com.blotout.geasture.GestureListener
import com.blotout.repository.EventRepository
import com.blotout.repository.impl.SharedPreferenceSecureVaultImpl
import com.blotout.util.Constant
import java.lang.ref.WeakReference
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import kotlin.system.exitProcess


class AnalyticsActivityLifecycleCallbacks(var eventRepository: EventRepository, var secureStorage: SharedPreferenceSecureVaultImpl):
        Application.ActivityLifecycleCallbacks,
        ComponentCallbacks2,
        DefaultLifecycleObserver,
        View.OnTouchListener ,
        GestureListener{

    private var numberOfActivities: AtomicInteger? = null
    private var trackedApplicationLifecycleEvents: AtomicBoolean? = null
    private var firstLaunch: AtomicBoolean? = null
    private var gesture:Gesture?=null
    private var activityReference: WeakReference<Activity>? = null

    init {
        numberOfActivities = AtomicInteger(1)
        firstLaunch = AtomicBoolean(false)
        trackedApplicationLifecycleEvents = AtomicBoolean(false)
        gesture = Gesture()
        gesture!!.addListener(this)
    }


    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (!trackedApplicationLifecycleEvents!!.getAndSet(true)) {
            numberOfActivities!!.set(0)
            firstLaunch!!.set(true)
            trackApplicationLifecycleEvents(activity)
            trackDeepLink(activity)
        }
        activityReference = WeakReference(activity)
        val view = activity.window.decorView.rootView
        view.setOnTouchListener(this)
        handleUncaughtException()
    }

    private fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        gesture!!.dispatchTouchEvent(event)
        return true
    }

    private fun getPackageInfo(context: Context): PackageInfo? {
        val packageManager = context.packageManager
        return try {
            return packageManager.getPackageInfo(context.packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            return null
        }
    }

    override fun onActivityStarted(activity: Activity) {
            eventRepository.prepareSystemEvent(activity, "Application Opened", null, Constant.BO_APPLICATION_OPENED)
    }

    override fun onActivityResumed(activity: Activity) {
            eventRepository.prepareSystemEvent(activity, Constant.BO_VISIBILITY_VISIBLE, null, Constant.BO_EVENT_VISIBILITY_VISIBLE)

    }

    override fun onActivityPaused(activity: Activity) {
            eventRepository.prepareSystemEvent(activity, Constant.BO_VISIBILITY_HIDDEN, null, Constant.BO_EVENT_VISIBILITY_HIDDEN)
            eventRepository.prepareSystemEvent(activity, "Application Backgrounded", null, Constant.BO_APPLICATION_BACKGROUNDED)
    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {
        eventRepository.prepareSystemEvent(activity, Constant.BO_VISIBILITY_HIDDEN, null, Constant.BO_EVENT_VISIBILITY_HIDDEN)

    }

    override fun onConfigurationChanged(newConfig: Configuration) {

    }

    override fun onLowMemory() {

    }

    override fun onTrimMemory(level: Int) {

    }

    private fun trackDeepLink(activity: Activity) {
        val intent = activity.intent
        if (intent == null || intent.data == null) {
            return
        }
        val properties = hashMapOf<String, Any>()
        val uri = intent.data
        for (parameter in uri!!.queryParameterNames) {
            val value = uri.getQueryParameter(parameter)
            if (value != null && !value.trim { it <= ' ' }.isEmpty()) {
                properties.put(parameter, value)
            }
        }
        properties.put("url", uri.toString())
        eventRepository.prepareSystemEvent(activity, "Deep Link Opened", properties, Constant.BO_DEEP_LINK_OPENED)
    }

    fun trackApplicationLifecycleEvents(activity: Activity) {
        val packageInfo: PackageInfo? = getPackageInfo(activity)
        val currentVersion = packageInfo?.versionName

        var previousVersion = secureStorage.fetchString((Constant.BO_VERSION_KEY))
        if (previousVersion.isNullOrEmpty()) {
            eventRepository.prepareSystemEvent(activity, "Application Installed", null, Constant.BO_APPLICATION_INSTALLED)

        } else if (previousVersion != currentVersion) {
            eventRepository.prepareSystemEvent(activity, "Application Updated", null, Constant.BO_APPLICATION_UPDATED)
        }

        secureStorage.storeString(Constant.BO_VERSION_KEY, currentVersion!!)

    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        dispatchTouchEvent(event)
        return true
    }

    override fun onPress(motionEvent: MotionEvent?) {
        eventRepository.prepareSystemEvent(activityReference!!.get(), Constant.BO_EVENT_CLICK_NAME, null, Constant.BO_EVENT_CLICK)
    }

    override fun onTap(motionEvent: MotionEvent?) {
        eventRepository.prepareSystemEvent(activityReference!!.get(), Constant.BO_EVENT_TOUCH_NAME, null, Constant.BO_EVENT_TOUCH)
    }

    override fun onDrag(motionEvent: MotionEvent?) {
        eventRepository.prepareSystemEvent(activityReference!!.get(), Constant.BO_EVENT_SCROLL_NAME, null, Constant.BO_EVENT_SCROLL)
    }

    override fun onMove(motionEvent: MotionEvent?) {
        //eventRepository.prepareSystemEvent(activityReference!!.get(), Constant.BO_VISIBILITY_HIDDEN, null, Constant.BO_EVENT_VISIBILITY_HIDDEN)
    }

    override fun onRelease(motionEvent: MotionEvent?) {
        eventRepository.prepareSystemEvent(activityReference!!.get(), Constant.BO_EVENT_KEY_RELEASE_NAME, null, Constant.BO_EVENT_KEY_RELEASE)
    }

    override fun onLongPress(motionEvent: MotionEvent?) {
        eventRepository.prepareSystemEvent(activityReference!!.get(), Constant.BO_EVENT_LONG_TOUCH_NAME, null, Constant.BO_EVENT_LONG_TOUCH)
    }

    override fun onMultiTap(motionEvent: MotionEvent?, clicks: Int) {
        eventRepository.prepareSystemEvent(activityReference!!.get(), Constant.BO_EVENT_MULTI_CLICK_NAME, null, Constant.BO_EVENT_MULTI_CLICK)
    }

    /**
     * Catch the Errors that are not handled by application and log to backend
     */
    private fun handleUncaughtException(){
        Thread.setDefaultUncaughtExceptionHandler { paramThread, paramThrowable ->
            val properties = hashMapOf<String, Any>()
            properties.put(Constant.BO_EVENT_ERROR_NAME,paramThrowable.localizedMessage!!)
            eventRepository.prepareSystemEvent(activityReference!!.get(), Constant.BO_EVENT_ERROR_NAME, properties, Constant.BO_EVENT_ERROR)
            exitProcess(1)
        }
    }
}