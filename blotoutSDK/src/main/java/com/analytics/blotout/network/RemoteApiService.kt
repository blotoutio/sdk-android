package com.analytics.blotout.network

import com.analytics.blotout.model.Events
import com.analytics.blotout.model.ManifestConfigurationResponse
import com.analytics.blotout.model.Result
import com.analytics.blotout.util.Constant
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface RemoteApiService {

    @Retry(3)
    @POST(Constant.BO_SDK_REST_API_MANIFEST_PULL_PATH)
    suspend fun getSDKManifest(): Response<ManifestConfigurationResponse>

    @Retry(3)
    @POST(Constant.BO_SDK_REST_API_EVENTS_PUSH_PATH)
    fun postEvents(@Body body: Events) : Call<Any>


}