package com.blotout.network.api;

import androidx.annotation.NonNull;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


/**
 * Created by Blotout on 21,November,2019
 */
public interface BOSegmentAPI {
    @NonNull
    @POST("{urlPath}")
    Call<Object> postSegmentData(@Path(value = "urlPath",encoded = true) String urlPath,@Body HashMap<String, Object> body);

    @NonNull
    @POST("{urlPath}")
    Call<Object> postCustomSegmentData(@Path(value = "urlPath",encoded = true) String urlPath,@Body HashMap<String, Object> body);

    @NonNull
    @POST("{urlPath}")
    Call<Object> getSegmentData(@Path(value = "urlPath",encoded = true) String urlPath,@Body HashMap<String, Object> body);
}
