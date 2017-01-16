package com.ardic.android.smartburglaralarm.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.ardic.android.smartburglaralarm.constant.BurglarAlarmConstants;

public class BurglarAlarmService extends Service {

    private ContextHolder mContextHolder;

    public BurglarAlarmService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(BurglarAlarmConstants.LOG_TAG,"Burglar Alarm Service Started.");
        mContextHolder = ContextHolder.getInstance(getApplicationContext());
    }
}
