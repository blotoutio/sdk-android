package com.blotout

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks2
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.blotout.repository.EventRepository
import com.blotout.repository.impl.SharedPrefernceSecureVaultImpl
import com.blotout.util.Constant
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger


class AnalyticsActivityLifecycleCallbacks(var eventRepository: EventRepository,var secureStorage: SharedPrefernceSecureVaultImpl): Application.ActivityLifecycleCallbacks, ComponentCallbacks2, DefaultLifecycleObserver {

    private var numberOfActivities: AtomicInteger? = null
    private var firstLaunch: AtomicBoolean? = null

    init {
        numberOfActivities = AtomicInteger(1)
        firstLaunch = AtomicBoolean(false)
    }


    private val stubOwner: LifecycleOwner = object : LifecycleOwner {
        var stubLifecycle: Lifecycle = object : Lifecycle() {
            override fun addObserver(observer: LifecycleObserver) {
            }

            override fun removeObserver(observer: LifecycleObserver) {
            }

            override fun getCurrentState(): State {
                return State.DESTROYED
            }
        }

        override fun getLifecycle(): Lifecycle {
            return stubLifecycle
        }
    }

    override fun onStart(owner: LifecycleOwner) {
        if (numberOfActivities!!.incrementAndGet() == 1) {
            eventRepository.prepareSystemEvent("Application Opened", null, Constant.BO_APPLICATION_OPENED)
        }
    }

    override fun onStop(owner: LifecycleOwner) {

        // App in background
        if (numberOfActivities!!.decrementAndGet() == 0) {
            eventRepository.prepareSystemEvent("Application Backgrounded", null, Constant.BO_APPLICATION_BACKGROUNDED)
        }
    }

    override fun onCreate(owner: LifecycleOwner) {
        numberOfActivities!!.set(0)
        firstLaunch!!.set(true)

    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

        onCreate(stubOwner)
        trackApplicationLifecycleEvents(activity)
        trackDeepLink(activity)
    }

    fun getPackageInfo(context: Context): PackageInfo? {
        val packageManager = context.packageManager
        return try {
            return packageManager.getPackageInfo(context.packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            return null
        }
    }

    override fun onActivityStarted(activity: Activity) {
        onStart(stubOwner)

    }

    override fun onActivityResumed(activity: Activity) {

    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {
        onStop(stubOwner)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {

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
        eventRepository.prepareSystemEvent("Deep Link Opened", properties,Constant.BO_DEEP_LINK_OPENED)
    }

    fun trackApplicationLifecycleEvents(activity: Activity) {
        val packageInfo: PackageInfo? = getPackageInfo(activity)
        val currentVersion = packageInfo?.versionName

        var previousVersion = secureStorage.fetchString((Constant.BO_VERSION_KEY))
        if (previousVersion.isNullOrEmpty()) {
            eventRepository.prepareSystemEvent("Application Installed", null, Constant.BO_APPLICATION_INSTALLED)

        } else if (!previousVersion.equals(currentVersion)) {
            eventRepository.prepareSystemEvent("Application Updated", null, Constant.BO_APPLICATION_UPDATED)
        }

        secureStorage.storeString(Constant.BO_VERSION_KEY, currentVersion!!)

    }
}