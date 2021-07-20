package com.blotout.network.api;

import androidx.annotation.NonNull;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface BORetentionEventPostAPI {
    @NonNull
    @POST("{urlPath}")
    Call<Object> postJson(@Path(value = "urlPath",encoded = true) String urlPath,@Body HashMap<String, Object> body);
}
