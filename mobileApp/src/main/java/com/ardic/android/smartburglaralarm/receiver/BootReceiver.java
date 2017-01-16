package com.ardic.android.smartburglaralarm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by murat on 09.01.2017.
 */

public class BootReceiver extends BroadcastReceiver {
    private static final String TAG="BootReceiver";

    @Override public void onReceive(Context context, Intent intent){
        try{
//            context.startService(new Intent(context,ConnectivityListener.class));
            Log.i(TAG,"Starting Service: " + intent.getPackage());
        }catch(Exception e){
            Log.e(TAG,e.toString());
        }
    }
}