package com.ardic.android.smartburglaralarm.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ardic.android.smartburglaralarm.app.App;
import com.ardic.android.smartburglaralarm.events.LoginFailedEvent;
import com.ardic.android.smartburglaralarm.events.LoginSuccessEvent;
import com.ardic.android.smartburglaralarm.service.LoginService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class LoginActivity extends Activity {
    private static final String TAG = "LoginActivity";

    EditText username;
    EditText password;
    Button loginButton;

    private LoginService mService;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.ardic.android.smartburglaralarm.R.layout.activity_main);

        mService = new LoginService();
        mService.run();

        initUIComponents();
    }

    private void initUIComponents() {
        username = (EditText) findViewById(com.ardic.android.smartburglaralarm.R.id.editText2);
        password = (EditText) findViewById(com.ardic.android.smartburglaralarm.R.id.etPass);
        loginButton = (Button) findViewById(com.ardic.android.smartburglaralarm.R.id.loginButton);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging In...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        final SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(App.PREPS_FILE, Context.MODE_PRIVATE);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "clicked");
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(com.ardic.android.smartburglaralarm.R.string.user_name), username.getText().toString());
                editor.putString(getString(com.ardic.android.smartburglaralarm.R.string.password), password.getText().toString());
                editor.commit();
                progressDialog.show();

                Runnable progressRunnable = new Runnable() {

                    @Override
                    public void run() {
                        if(progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            showAlertDialog("Can not login");
                        }
                    }
                };

                Handler pdCanceller = new Handler();
                pdCanceller.postDelayed(progressRunnable, 10000);
                mService.login();

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

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginSuccess(LoginSuccessEvent event) {
        progressDialog.dismiss();
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginFailure(LoginFailedEvent event) {
        progressDialog.dismiss();
        Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_LONG).show();
        password.setText("");
    }
}
