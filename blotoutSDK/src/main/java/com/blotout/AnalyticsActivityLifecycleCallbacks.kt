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
import com.blotout.repository.EventRepository
import com.blotout.repository.impl.SharedPreferenceSecureVaultImpl
import com.blotout.util.Constant
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger


class AnalyticsActivityLifecycleCallbacks(var eventRepository: EventRepository,var secureStorage: SharedPreferenceSecureVaultImpl): Application.ActivityLifecycleCallbacks, ComponentCallbacks2, DefaultLifecycleObserver {

    private var numberOfActivities: AtomicInteger? = null
    private var firstLaunch: AtomicBoolean? = null

    init {
        numberOfActivities = AtomicInteger(1)
        firstLaunch = AtomicBoolean(false)
    }


    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

        numberOfActivities!!.set(0)
        firstLaunch!!.set(true)
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
        if (numberOfActivities!!.incrementAndGet() == 1) {
            eventRepository.prepareSystemEvent(activity,"Application Opened", null, Constant.BO_APPLICATION_OPENED)
        }

    }

    override fun onActivityResumed(activity: Activity) {

    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {
        if (numberOfActivities!!.decrementAndGet() == 0) {
            eventRepository.prepareSystemEvent(activity,"Application Backgrounded", null, Constant.BO_APPLICATION_BACKGROUNDED)
        }
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
        eventRepository.prepareSystemEvent(activity,"Deep Link Opened", properties,Constant.BO_DEEP_LINK_OPENED)
    }

    fun trackApplicationLifecycleEvents(activity: Activity) {
        val packageInfo: PackageInfo? = getPackageInfo(activity)
        val currentVersion = packageInfo?.versionName

        var previousVersion = secureStorage.fetchString((Constant.BO_VERSION_KEY))
        if (previousVersion.isNullOrEmpty()) {
            eventRepository.prepareSystemEvent(activity,"Application Installed", null, Constant.BO_APPLICATION_INSTALLED)

        } else if (!previousVersion.equals(currentVersion)) {
            eventRepository.prepareSystemEvent(activity,"Application Updated", null, Constant.BO_APPLICATION_UPDATED)
        }

        secureStorage.storeString(Constant.BO_VERSION_KEY, currentVersion!!)

    }
}