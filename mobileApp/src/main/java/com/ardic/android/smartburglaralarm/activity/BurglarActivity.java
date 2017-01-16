package com.ardic.android.smartburglaralarm.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ardic.android.smartburglaralarm.R;
import com.ardic.android.smartburglaralarm.app.App;
import com.ardic.android.smartburglaralarm.events.ActuatorFailedEvent;
import com.ardic.android.smartburglaralarm.events.ActuatorSucceedEvent;
import com.ardic.android.smartburglaralarm.events.ConnectionFailedEvent;
import com.ardic.android.smartburglaralarm.events.DeviceRetrievalFailedEvent;
import com.ardic.android.smartburglaralarm.events.DeviceRetrievedEvent;
import com.ardic.android.smartburglaralarm.events.SensorValFailedEvent;
import com.ardic.android.smartburglaralarm.events.SensorValSuccessEvent;
import com.ardic.android.smartburglaralarm.events.WebSocketClosedEvent;
import com.ardic.android.smartburglaralarm.events.WebSocketMessageEvent;
import com.ardic.android.smartburglaralarm.events.WebSocketTokenFailureEvent;
import com.ardic.android.smartburglaralarm.events.WebSocketTokenSuccessEvent;
import com.ardic.android.smartburglaralarm.service.BurglarService;
import com.ardic.android.smartburglaralarm.service.WebSocketServiceInitiator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.URISyntaxException;

/**
 * Created by murat on 29.12.2016.
 */

public class BurglarActivity extends Activity {
    private static final String TAG = "BurglarActivity";

    TextView deviceTextView;
    Button forgetButton;
    Button safeButton;

    private static BurglarService burglarService;
    private static WebSocketServiceInitiator webSocketServiceInitiator;

    private SharedPreferences sharedPref;
    private String username;
    private String accessToken;
    private ProgressDialog actuatorProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_main);

        // Initialize shared preference
        sharedPref = getApplicationContext().getSharedPreferences(App.PREPS_FILE, Context.MODE_PRIVATE);

        startBurglarService();

        username = sharedPref.getString(getString(R.string.user_name), null);
        if (null == username) {
            Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
            Log.i(TAG, "No username, start login activity");
            startActivity(loginActivity);
        } else {
            // Buraada access token değişmiş olabileceği için login service login metodu çağırılabilir.
            // mLoginService.login()

            accessToken = sharedPref.getString(getString(R.string.access_token), null);

            Log.i(TAG, "username: " + username);
            Log.i(TAG, "access: " + accessToken);

            burglarService.getData();
        }

        initUIComponents();

    }

    private void initUIComponents() {
        // UI Work
        deviceTextView = (TextView) findViewById(R.id.twdevice);
        forgetButton = (Button) findViewById(R.id.btForget);
        safeButton = (Button) findViewById(R.id.btSafe);
        actuatorProgressDialog = new ProgressDialog(this);
        actuatorProgressDialog.setCancelable(false);
        actuatorProgressDialog.setCanceledOnTouchOutside(false);

        forgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.clear();
                editor.commit();

                Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(loginActivity);
            }
        });

        safeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean unlock = false;
                actuatorProgressDialog.setMessage("Locking door...");
                if (safeButton.getText().equals(getString(R.string.unlock))) {
                    unlock = true;
                    actuatorProgressDialog.setMessage("Unlocking door...");
                }
                actuatorProgressDialog.show();
                burglarService.sendActuatorMessage(sharedPref.getString(getString(R.string.device), null), unlock);
                Runnable progressRunnable = new Runnable() {

                    @Override
                    public void run() {
                        if(actuatorProgressDialog.isShowing()) {
                            actuatorProgressDialog.dismiss();
                            showAlertDialog("Can not receive data from sensor");
                        }
                    }
                };

                Handler pdCanceller = new Handler();
                pdCanceller.postDelayed(progressRunnable, 10000);

            }
        });
    }

    private void showAlertDialog(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private void startBurglarService() {

        if (null == burglarService) {
            synchronized (this) {
                if (null == burglarService) {
                    burglarService = new BurglarService();
                    burglarService.run();
                }
            }
        }

    }

    private void startWebSocketService() throws URISyntaxException {
        Log.i(TAG, "start web socket service");
        String webSocketUrl = sharedPref.getString(getString(R.string.websocket_url), "");
            if (null == webSocketServiceInitiator) {
            synchronized (this) {
                if (null == webSocketServiceInitiator) {
                    Log.i(TAG, "Initialize web socket");
                    webSocketServiceInitiator = new WebSocketServiceInitiator(webSocketUrl, this);
                    webSocketServiceInitiator.run();
                }
            }
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        username = sharedPref.getString(getString(R.string.user_name), null);
        accessToken = sharedPref.getString(getString(R.string.access_token), null);
        Log.i(TAG, "onResume");
        if (null != username) {
            burglarService.getData();

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Register to event bus
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Register to event bus
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeviceFailure(DeviceRetrievalFailedEvent event) {
        Toast.makeText(App.context, "No device found", Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onDeviceSuccess(DeviceRetrievedEvent event) {
        Log.i(TAG, "Device information retrieved successfully.");
        // Get token for websocket subscription
        burglarService.getToken(sharedPref.getString(getString(R.string.device), null));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSensorValSuccess(SensorValSuccessEvent event) {
        Log.i(TAG, "Device: " + sharedPref.getString(getString(R.string.device), "Cihaz kaydedilmemiş") + " val: " + event.sensorVal);
        if (null != event.sensorVal && event.sensorVal.contains("1")) {
            safeButton.setText(getString(R.string.lock));
        } else {
            safeButton.setText(getString(R.string.unlock));
        }
        deviceTextView.setText("Device: " + sharedPref.getString(getString(R.string.device), "Cihaz kaydedilmemiş") + " val: " + event.sensorVal);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSensorValFailure(SensorValFailedEvent event) {
        Log.e(TAG, "sensor val failure");
        Toast.makeText(App.context, "No val found for device", Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onConnectFailure(ConnectionFailedEvent event) {
        Log.w(TAG, "connect Fail, direct to login page");
        Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(loginActivity);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onActuatorSuccess(ActuatorSucceedEvent event) {
        Log.i(TAG, "Actuator message send successfully");

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onActuatorFailure(ActuatorFailedEvent event) {
        Log.w(TAG, "Actuator message could not be sent.");
        actuatorProgressDialog.dismiss();
        Toast.makeText(getApplicationContext(), "Could not perform the operation", Toast.LENGTH_LONG).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WebSocketMessageEvent event) {
        final String message = event.message;
        runOnUiThread(new Thread(new Runnable() {
            @Override
            public void run() {
                if (null != message && message.equals("1")) {
                    safeButton.setText(getString(R.string.lock));
                    safeButton.setVisibility(View.VISIBLE);
                    actuatorProgressDialog.dismiss();
                } else if (null != message && message.equals("0")) {
                    safeButton.setText(getString(R.string.unlock));
                    safeButton.setVisibility(View.VISIBLE);
                    actuatorProgressDialog.dismiss();
                }
            }
        }));
        Log.i(TAG, "onMessageReceived: " + message);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTokenSuccess(WebSocketTokenSuccessEvent event) {
        try {
            startWebSocketService();
        } catch (URISyntaxException e) {
            Log.e(TAG, e.getLocalizedMessage());
            Toast.makeText(getApplicationContext(), "Can not start web socket service", Toast.LENGTH_LONG).show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTokenFailure(WebSocketTokenFailureEvent event) {
        Toast.makeText(getApplicationContext(), "Can not get token to connect websocket", Toast.LENGTH_LONG).show();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onWebSocketDisconnected(WebSocketClosedEvent event) {
        Log.w(TAG, "Websocket closed!");
    }
}
