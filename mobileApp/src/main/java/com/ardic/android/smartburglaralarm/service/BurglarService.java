package com.ardic.android.smartburglaralarm.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.ardic.android.smartburglaralarm.app.App;
import com.ardic.android.smartburglaralarm.events.ActuatorFailedEvent;
import com.ardic.android.smartburglaralarm.events.ActuatorSucceedEvent;
import com.ardic.android.smartburglaralarm.events.ConnectionFailedEvent;
import com.ardic.android.smartburglaralarm.events.DeviceRetrievalFailedEvent;
import com.ardic.android.smartburglaralarm.events.DeviceRetrievedEvent;
import com.ardic.android.smartburglaralarm.events.LoginFailedEvent;
import com.ardic.android.smartburglaralarm.events.SensorValFailedEvent;
import com.ardic.android.smartburglaralarm.events.SensorValSuccessEvent;
import com.ardic.android.smartburglaralarm.events.WebSocketTokenFailureEvent;
import com.ardic.android.smartburglaralarm.events.WebSocketTokenSuccessEvent;
import com.ardic.android.smartburglaralarm.model.ActuatorRequest;
import com.ardic.android.smartburglaralarm.model.ActuatorRequestParam;
import com.ardic.android.smartburglaralarm.model.DeviceResponse;
import com.ardic.android.smartburglaralarm.model.SensorDataResponse;
import com.ardic.android.smartburglaralarm.model.TokenResponse;
import com.ardic.android.smartburglaralarm.rest.client.IgniteService;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by murat on 29.12.2016.
 */

public class BurglarService implements Runnable{
    private static final String TAG = "BurglarService";

    private Retrofit retrofit;
    private SharedPreferences sharedPref;

    private String username;
    private String accessToken;
    private IgniteService service;

    private static LoginService loginService;
    private static AtomicInteger checker = new AtomicInteger(0);


    public void getDevice() {
        Call<DeviceResponse> deviceList = service.getDevice(generateHeaders(), username);
        deviceList.enqueue(new Callback<DeviceResponse>() {
            @Override
            public void onResponse(Call<DeviceResponse> call, Response<DeviceResponse> response) {
                if (response.isSuccessful() && null != response.body() && null != response.body().getContent() && !response.body().getContent().isEmpty()) {
                    String device = response.body().getContent().get(0).getDeviceId();
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(App.context.getResources().getString(com.ardic.android.smartburglaralarm.R.string.device), device);
                    editor.commit();
                    EventBus.getDefault().post(new DeviceRetrievedEvent());
                    getSensorVal(device);
                } else {
                    try {
                        if (response.errorBody().string().contains("invalid_token")) {
                            if (sharedPref.contains(App.context.getResources().getString(com.ardic.android.smartburglaralarm.R.string.user_name))) {
                                loginService.run();
                                loginService.login();
                            }
                        }
                        Log.i(TAG, "Get device failed. " + response.errorBody().string());
                        Log.i(TAG, "Get device failed2: " + response.isSuccessful());
                    } catch (IOException e) {
                        Log.e(TAG, e.getLocalizedMessage());
                    }
                    EventBus.getDefault().post(new DeviceRetrievalFailedEvent());
                }
            }

            @Override
            public void onFailure(Call<DeviceResponse> call, Throwable t) {
                Log.i(TAG, "Get device failed. " + t.getMessage());
                EventBus.getDefault().post(new DeviceRetrievalFailedEvent());
            }
        });
    }

    private void getSensorVal(String device) {
        Call<SensorDataResponse> sensorData = service.getSensorData(generateHeaders(), device, device, "VirtualDemoNode", "Lamp");
        sensorData.enqueue(new Callback<SensorDataResponse>() {
            @Override
            public void onResponse(Call<SensorDataResponse> call, Response<SensorDataResponse> response) {
                if (response.isSuccessful() && null != response.body() && null != response.body().getData() && null != response.body().getData().getData()) {
                    EventBus.getDefault().post(new SensorValSuccessEvent(response.body().getData().getData()));
                } else {
                    try {
                        Log.i(TAG, "Get sensor val failed. " + response.errorBody().string());
                    } catch (IOException e) {
                        Log.e(TAG, e.getLocalizedMessage());
                    }
                    EventBus.getDefault().post(new SensorValFailedEvent());
                }
            }

            @Override
            public void onFailure(Call<SensorDataResponse> call, Throwable t) {
                Log.i(TAG, "Get sensor val failed. " + t.getMessage());
                EventBus.getDefault().post(new SensorValFailedEvent());
            }
        });
    }

    public void sendActuatorMessage(String device, boolean type) {
        ActuatorRequestParam param = new ActuatorRequestParam();
        if (type) {
            param.setMessage("1");
        } else {
            param.setMessage("0");
        }
        Log.i(TAG, new Gson().toJson(param));
        ActuatorRequest request = new ActuatorRequest();
        request.setNodeId("VirtualDemoNode");
        request.setParams(new Gson().toJson(param));
        request.setSensorId("Lamp");
        request.setType("Lamp");
        Call<Object> sensorData = service.sendSensorMessage(generateHeaders(),device, request);
        sensorData.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.isSuccessful()) {
                    Log.i(TAG, "send message success");
                    EventBus.getDefault().post(new ActuatorSucceedEvent());
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e(TAG, t.getLocalizedMessage());
                EventBus.getDefault().post(new ActuatorFailedEvent());
            }
        });
    }

    private Map<String, String> generateHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + accessToken);

        return headers;
    }

    @Override
    public void run() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.ardich.com/api/v3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Log.i(TAG, "Retrofit service created");
        sharedPref = App.context.getSharedPreferences(App.PREPS_FILE, Context.MODE_PRIVATE);

        service = retrofit.create(IgniteService.class);
        username = sharedPref.getString(App.context.getResources().getString(com.ardic.android.smartburglaralarm.R.string.user_name), null);
        accessToken = sharedPref.getString(App.context.getResources().getString(com.ardic.android.smartburglaralarm.R.string.access_token), null);

        if (null == loginService) {
            synchronized (this) {
                if (null == loginService) {
                    loginService = new LoginService();
                }
            }
        }
        EventBus.getDefault().register(this);
    }

    public void getData() {
        getDevice();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onLoginFailure(LoginFailedEvent event) {
        int tryCount = checker.getAndAdd(1);
        Log.w(TAG, "Could not login, try again #" + tryCount);
        if (tryCount < 3) {
            getDevice();
        } else {
            EventBus.getDefault().post(new ConnectionFailedEvent());
        }
    }



    public void getToken(String device) {
        Call<TokenResponse> tokenResponse = service.getWebsocketToken(generateHeaders(),device);
        tokenResponse.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if (response.isSuccessful() && null != response.body()) {
                    TokenResponse token = response.body();
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(App.context.getResources().getString(com.ardic.android.smartburglaralarm.R.string.websocket_token), token.getToken());
                    editor.putString(App.context.getResources().getString(com.ardic.android.smartburglaralarm.R.string.websocket_url), token.getUrl());
                    editor.putString(App.context.getResources().getString(com.ardic.android.smartburglaralarm.R.string.subscribe_id), token.getId());
                    editor.putLong(App.context.getResources().getString(com.ardic.android.smartburglaralarm.R.string.websocket_token_expire), token.getExpireDate());
                    Log.i(TAG, "get token response: " + token.toString());
                    editor.commit();
                    EventBus.getDefault().post(new WebSocketTokenSuccessEvent());
                } else {
                    try {
                        Log.i(TAG, "Get subscribe token failed. " + response.errorBody().string());
                    } catch (IOException e) {
                        Log.e(TAG, e.getLocalizedMessage());
                    }
                    EventBus.getDefault().post(new WebSocketTokenFailureEvent());
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                Log.i(TAG, "Get sensor val failed. " + t.getMessage());
                EventBus.getDefault().post(new SensorValFailedEvent());
            }
        });
    }

}
