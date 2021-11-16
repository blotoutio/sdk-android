package com.analytics.blotout.referral

import android.app.Application
import android.util.Log
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.analytics.blotout.DependencyInjectorImpl

class InstallRefferal {

    companion object {
        private const val TAG = "InstallRefferal"
    }

    private lateinit var referrerClient: InstallReferrerClient

    fun startClient(app: Application) {
        try {
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
        }catch (e:Throwable){
            Log.e(TAG,e.toString())
        }
    }


    private fun getReferrerData() {
        try {
            DependencyInjectorImpl.getInstance().mReferrerDetails = referrerClient.installReferrer
            referrerClient.endConnection()
        }catch (e:Throwable){
            Log.e(TAG,e.toString())
        }
    }
}