package com.ardic.android.smartburglaralarm.rest.client;

import com.ardic.android.smartburglaralarm.model.ActuatorRequest;
import com.ardic.android.smartburglaralarm.model.DeviceResponse;
import com.ardic.android.smartburglaralarm.model.AccessToken;
import com.ardic.android.smartburglaralarm.model.SensorDataResponse;
import com.ardic.android.smartburglaralarm.model.TokenResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by murat on 29.12.2016.
 */

public interface IgniteService {
    @Headers({
            "Authorization:Basic ZnJvbnRlbmQ6",
            "Content-Type:application/x-www-form-urlencoded"
    })
    @FormUrlEncoded
    @POST("login/oauth")
    Call<AccessToken> getAccessToken(@Field("username") String username, @Field("password") String password, @Field("grant_type") String grant_type);


    @GET("device/summary")
    Call<DeviceResponse> getDevice(@HeaderMap Map<String, String> headers, @Query("user") String user);

    @GET("device/{deviceId}/sensor-data")
    Call<SensorDataResponse> getSensorData(@HeaderMap Map<String, String> headers, @Path("deviceId") String deviceId, @Query("device") String device, @Query("nodeId") String nodeId, @Query("sensorId") String sensorId);

    @POST("device/{device}/control/sensor-agent-message")
    Call<Object> sendSensorMessage(@HeaderMap Map<String, String> headers, @Path("device") String device, @Body ActuatorRequest request);

    @GET("subscribe/device/token")
    Call<TokenResponse> getWebsocketToken(@HeaderMap Map<String, String> headers, @Query("device") String device);


}
