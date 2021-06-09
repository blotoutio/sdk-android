package com.blotout.network

import com.blotout.model.Events
import com.blotout.model.ManifestConfigurationRequest
import com.blotout.model.ManifestConfigurationResponse
import com.blotout.util.Constant
import retrofit2.Call
import retrofit2.http.*

interface RemoteApiService {

    @POST(Constant.BO_SDK_REST_API_MANIFEST_PULL_PATH)
    fun getSDKManifest(): Call<ManifestConfigurationResponse?>?

    @POST(Constant.BO_SDK_REST_API_EVENTS_PUSH_PATH)
    fun postEvents(@Body body: Events) : Call<Any>


}