package com.blotout.referral

import android.app.Application
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.ReferrerDetails
import com.blotout.DependencyInjectorImpl
import java.util.*

class InstallRefferal {

    private lateinit var referrerClient: InstallReferrerClient

    fun startClient(app: Application) {

        referrerClient = InstallReferrerClient.newBuilder(app).build()

        referrerClient.startConnection(object : InstallReferrerStateListener {

            override fun onInstallReferrerSetupFinished(responseCode: Int) {
                when (responseCode) {
                    InstallReferrerClient.InstallReferrerResponse.OK -> {
                        getReferrerData()
                    }
                    InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
                    }
                    InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
                    }
                }
            }

            override fun onInstallReferrerServiceDisconnected() {

            }
        })
    }


    private fun getReferrerData() {
        /*val response: ReferrerDetails = referrerClient.installReferrer
        val referrerUrl: String = response.installReferrer
        val referrerClickTime: Long = response.referrerClickTimestampSeconds
        val appInstallTime: Long = response.installBeginTimestampSeconds
        val instantExperienceLaunched: Boolean = response.googlePlayInstantParam
        val eventInfo = HashMap<String, Any>()
        eventInfo["installReferrer"] = response.installReferrer
        eventInfo["ireferrerUrl"] = referrerUrl*/

        DependencyInjectorImpl.getInstance().mReferrerDetails = referrerClient.installReferrer
        referrerClient.endConnection()
    }
}