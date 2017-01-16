package com.ardic.android.smartburglaralarm.service;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.ardic.android.smartburglaralarm.app.App;
import com.ardic.android.smartburglaralarm.events.LoginFailedEvent;
import com.ardic.android.smartburglaralarm.events.LoginSuccessEvent;
import com.ardic.android.smartburglaralarm.model.AccessToken;
import com.ardic.android.smartburglaralarm.rest.client.IgniteService;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by murat on 29.12.2016.
 */

public class LoginService implements Runnable {
    private static final String TAG = "LoginService";

    private Retrofit retrofit;

    private SharedPreferences sharedPref;


    public void login() {
        IgniteService service = retrofit.create(IgniteService.class);
        String username = sharedPref.getString(App.context.getResources().getString(com.ardic.android.smartburglaralarm.R.string.user_name), null);
        String password = sharedPref.getString(App.context.getResources().getString(com.ardic.android.smartburglaralarm.R.string.password), null);
        Call<AccessToken> login = service.getAccessToken(username, password, "password");
        Log.i(TAG, login.request().toString());
        Log.i(TAG, login.request().body().toString());

        login.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                if (response.isSuccessful() && null != response.body() && null != response.body().getAccess_token()) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(App.context.getResources().getString(com.ardic.android.smartburglaralarm.R.string.access_token), response.body().getAccess_token());
                    editor.commit();
                    Log.i(TAG, response.body().getAccess_token());
                    EventBus.getDefault().post(new LoginSuccessEvent());
                } else {
                    Log.i(TAG, "Login failed. " + response.errorBody());
                    EventBus.getDefault().post(new LoginFailedEvent());
                }
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                Log.i(TAG, "Login failed. " + t.getMessage());
                EventBus.getDefault().post(new LoginFailedEvent());
            }
        });


    }

    @Override
    public void run() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.ardich.com/api/v3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        sharedPref = App.context.getSharedPreferences(App.PREPS_FILE, Context.MODE_PRIVATE);
    }
}
