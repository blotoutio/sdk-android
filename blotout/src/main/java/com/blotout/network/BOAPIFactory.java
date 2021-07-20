package com.blotout.network;

import androidx.annotation.NonNull;

import com.blotout.analytics.BOSharedManager;
import com.blotout.analytics.BlotoutAnalytics_Internal;
import com.blotout.constants.BONetworkConstants;
import com.blotout.network.api.BOEventPostAPI;
import com.blotout.network.api.BOFunnelAPI;
import com.blotout.network.api.BOGeoIPAPI;
import com.blotout.network.api.BORetentionEventPostAPI;
import com.blotout.network.api.BOSDKManifestAPI;
import com.blotout.network.api.BOSegmentAPI;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;


public class BOAPIFactory {

    private static final String contentType = "application/json";
    private static final String accept = "application/json";

    private final String appVersion;
    @NonNull
    private final String userAgent;
    private final String endPoint;
    private Retrofit restAdapter;

    private OkHttpClient httpClient;
    public BOEventPostAPI eventPostAPI;
    public BOGeoIPAPI geoAPI;
    public BOSDKManifestAPI manifestAPI;
    public BORetentionEventPostAPI retentionAPI;
    public BOFunnelAPI funnelAPI;
   public BOSegmentAPI segmentAPI;

    public BOAPIFactory( String appVersion, String endPoint) {
        this.appVersion = appVersion;
        this.userAgent = "BOAndroid; " + appVersion;
        this.endPoint = endPoint;
        _init();
    }

    private void _init() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // add interceptors
        httpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @NonNull
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                if(BlotoutAnalytics_Internal.getInstance().isSDKEnabled && BlotoutAnalytics_Internal.getInstance().isNetworkSyncEnabled) {
                    Request original = chain.request();
                    String tokenKey = BlotoutAnalytics_Internal.getInstance().isProductionMode ? BlotoutAnalytics_Internal.getInstance().prodBlotoutKey : BlotoutAnalytics_Internal.getInstance().testBlotoutKey;
                    // Request customization: add request headers
                    Request.Builder requestBuilder = original.newBuilder()
                            .addHeader(BONetworkConstants.BO_CONTENT_TYPE, contentType)
                            .addHeader(BONetworkConstants.BO_ACCEPT, accept)
                            .addHeader(BONetworkConstants.BO_TOKEN, tokenKey)
                            .addHeader(BONetworkConstants.BO_VERSION, BONetworkConstants.BO_VERSION_NAME);

                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                } else {
                    throw new NoConnectivityException();
                }
            }
        }).addInterceptor(loggingInterceptor).build();
                /*.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();

                // Request.Builder builder = originalRequest.newBuilder().header("Authorization",
                //    Credentials.basic("aUsername", "aPassword"));

                Request.Builder builder = originalRequest.newBuilder();
                // accessToken
                String accessToken = "";  //getAccessToken();
                if (!TextUtils.isEmpty(accessToken)) {
                    //builder.addHeader("Authorization", "bearer " + accessToken);
                }
                Request request = builder.build();
                return chain.proceed(request);
            }
        }).build(); */

                restAdapter =  new Retrofit.Builder()
                .baseUrl(this.endPoint)
                .client(httpClient)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        // Api
        this.eventPostAPI = restAdapter.create(BOEventPostAPI.class);
        this.geoAPI = restAdapter.create(BOGeoIPAPI.class);
        this.manifestAPI = restAdapter.create(BOSDKManifestAPI.class);
        this.retentionAPI = restAdapter.create(BORetentionEventPostAPI.class);
        this.funnelAPI = restAdapter.create(BOFunnelAPI.class);
        this.segmentAPI = restAdapter.create(BOSegmentAPI.class);
      }

      @NonNull
      private Retrofit getRetrofit(@NonNull String url) {

        return new Retrofit.Builder()
                .baseUrl(url)
                .client(httpClient)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    public class NoConnectivityException extends IOException {
        @Override
        public String getMessage() {
            return "No network available, please check your WiFi or Data connection";
        }
    }
}
