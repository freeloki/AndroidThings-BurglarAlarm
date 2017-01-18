package com.ardic.android.smartburglaralarm.handlers;

import android.content.Context;
import android.util.Log;

import com.ardic.android.iotignite.callbacks.ConnectionCallback;
import com.ardic.android.iotignite.exceptions.UnsupportedVersionException;
import com.ardic.android.iotignite.nodes.IotIgniteManager;
import com.ardic.android.iotignite.nodes.Node;
import com.ardic.android.iotignite.things.Thing;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by yavuz.erzurumlu on 1/4/17.
 */

public class IotIgniteHandler implements ConnectionCallback{

    private static final String TAG = "Smart Door Alarm";
    private static final String NODE_ID = "Iot Ignite Smart Home";
    private static final String THING_ID = "Smart Door Alarm";
    private static final long IGNITE_RECONNECTION_TIMEOUT = 15000L;
    private static IotIgniteHandler INSTANCE;
    private static Object instanceLock = new Object();

    // ignite timer variables

    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    private Runnable igniteTimerRunnable = new Runnable() {
        @Override
        public void run() {

            // connect to ignite.
            if(!igniteConnected){
                // connect to ignite.
                try {
                    mIotManager = new IotIgniteManager.Builder()
                            .setConnectionListener(INSTANCE)
                            .setContext(mContext)
                            .build();
                } catch (UnsupportedVersionException e) {
                    Log.e(TAG,"Unsupported Version Exceptipn : " + e);
                }

                executor.schedule(this,IGNITE_RECONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);
            }
        }
    };

    private Context mContext;
    private IotIgniteManager mIotManager;
    private Node mNode;
    private Thing mDoorAlarm;
    private boolean igniteConnected = false;


    private IotIgniteHandler(Context appContext){
         this.mContext = appContext;
    }

    public static IotIgniteHandler getInstance() {

        synchronized (instanceLock){
        if (INSTANCE == null) {
            INSTANCE = new IotIgniteHandler(ContextHolder.getAppContext());
        }
    }
        return INSTANCE;
    }

    public void start(){

        startIgniteTimer();


    }

    @Override
    public void onConnected() {
        Log.i(TAG,"IoT-Ignite Connected");


    }

    @Override
    public void onDisconnected() {
        Log.i(TAG,"IoT-Ignite Disconnected");
    }

    private void stopIgniteTimer(){
        // destroy connection handler

        executor.shutdown();
        try {
            executor.awaitTermination(5,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Log.e(TAG," Executor Interrupted");
        }

        finally {

            if(!executor.isTerminated()){
                Log.e(TAG,"Ignite Connection Timer Task is still running.");
            }

            executor.shutdownNow();

            Log.i(TAG,"Shutdown finished.");
        }
    }

    private  void startIgniteTimer(){
        executor.submit(igniteTimerRunnable);
    }
}
