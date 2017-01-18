package com.ardic.android.smartburglaralarm.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ardic.android.smartburglaralarm.service.BurglarAlarmService;

/**
 * Created by yavuz.erzurumlu on 18.01.2017.
 */

public class BurglarAlarmAutoStartRecevier extends BroadcastReceiver{
    private static final String TAG = "BurglarAutoStart";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i(TAG,"Boot complete received. Starting Burglar Alarm Service..");
        Intent serviceIntent = new Intent(context,BurglarAlarmService.class);
        context.startService(serviceIntent);
    }
}
