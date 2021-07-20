package com.blotout.network.api;

import androidx.annotation.NonNull;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


/**
 * Created by Blotout on 21,November,2019
 */
public interface BOGeoIPAPI {
    @NonNull
    @GET("{urlPath}")
    Call<HashMap<String, Object>> getGeoData(@Path(value = "urlPath",encoded = true) String urlPath);

}
