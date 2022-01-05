package com.analytics.blotout.network

import com.analytics.blotout.model.*
import com.analytics.blotout.util.Constant
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

class RemoteApiDataSource(private val remoteApiService: RemoteApiService) {

    suspend fun getSDKManifest(): Result<ManifestConfigurationResponse?>{
        return processNetworkResponse(remoteApiService.getSDKManifest())
    }

    fun postEvents(events: Events) : Call<Any>{
        return remoteApiService.postEvents(events)
    }

    private fun <T> processNetworkResponse(response : Response<T>):Result<T?>{
        return try{
            when(response.isSuccessful){
                true->{
                    Result.Success(response.body())
                }
                false->{
                    val errorResponseString = response.errorBody()?.string()
                    Result.Error(
                            errorData = InternalError(code = response.code(),msg = response.message())
                    )
                }
            }
        }catch(e:Exception){
            Result.Error(errorData = InternalError(code = ErrorCodes.ERROR_CODE_NETWORK_ERROR))
        }

    }
}