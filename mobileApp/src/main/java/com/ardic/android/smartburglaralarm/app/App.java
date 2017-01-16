package com.ardic.android.smartburglaralarm.app;

import android.content.Context;

/**
 * Created by murat on 02.01.2017.
 */

public class App extends android.app.Application {
    public static Context context;
    public static final String PREPS_FILE = "burglarPrepsFile";

    @Override public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
