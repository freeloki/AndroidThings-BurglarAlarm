package com.ardic.android.smartburglaralarm.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.ardic.android.smartburglaralarm.app.App;
import com.ardic.android.smartburglaralarm.events.WebSocketClosedEvent;
import com.ardic.android.smartburglaralarm.events.WebSocketMessageEvent;
import com.ardic.android.smartburglaralarm.model.Subscribe;
import com.ardic.android.smartburglaralarm.model.SubscribeMessage;
import com.ardic.android.smartburglaralarm.model.response.WebSocketSensorDataResponse;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * Created by murat on 03.01.2017.
 */

public class WebsocketService extends WebSocketClient {
    private static final String TAG = "WebsocketService";
    SharedPreferences sharedPref;

      public WebsocketService(URI serverURI) {
        super(serverURI);
        sharedPref = App.context.getSharedPreferences(App.PREPS_FILE, Context.MODE_PRIVATE);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.i(TAG, "new connection opened");
        sendSubscibeMessage();

    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.i(TAG, "closed with exit code " + code + " additional info: " + reason);
        EventBus.getDefault().post(new WebSocketClosedEvent());
    }

    @Override
    public void onMessage(String message) {
        Log.i(TAG, "received message: " + message);
        WebSocketSensorDataResponse response = new Gson().fromJson(message, WebSocketSensorDataResponse.class);
        if (null != response && null != response.getBody() && null != response.getBody().getExtras() && !response.getBody().getExtras().getSensorData().isEmpty()) {
            EventBus.getDefault().post(new WebSocketMessageEvent(response.getBody().getExtras().getSensorData().get(0).getValues().get(0)));
        } else {
            EventBus.getDefault().post(new WebSocketMessageEvent(message));
        }
    }

    @Override
    public void onError(Exception ex) {
        Log.e(TAG, "an error occurred:" + ex);
    }

    private void sendSubscibeMessage() {
       Subscribe subscribe = createSubscribeMessage();
       send(new Gson().toJson(subscribe));
    }

    private Subscribe createSubscribeMessage() {

        SubscribeMessage subscribeMessage = new SubscribeMessage();
        subscribeMessage.setDeviceId(sharedPref.getString(App.context.getResources().getString(com.ardic.android.smartburglaralarm.R.string.device),""));
        subscribeMessage.setNodeId(App.context.getResources().getString(com.ardic.android.smartburglaralarm.R.string.nodeId));
        subscribeMessage.setSensorId(App.context.getResources().getString(com.ardic.android.smartburglaralarm.R.string.sensorId));
        subscribeMessage.setSubscribeId(sharedPref.getString(App.context.getResources().getString(com.ardic.android.smartburglaralarm.R.string.subscribe_id),""));
        subscribeMessage.setVersion(App.context.getResources().getString(com.ardic.android.smartburglaralarm.R.string.subscribe_version));
        subscribeMessage.setToken(sharedPref.getString(App.context.getResources().getString(com.ardic.android.smartburglaralarm.R.string.websocket_token),""));

        Subscribe subscribe = new Subscribe();
        subscribe.setMessage(subscribeMessage);
        subscribe.setType(App.context.getResources().getString(com.ardic.android.smartburglaralarm.R.string.subscribe_type));

        Log.i(TAG, subscribe.toString());

        return subscribe;
    }
}