package com.ardic.android.smartburglaralarm.handlers;

import android.content.Context;

/**
 * Created by yavuz.erzurumlu on 18.01.2017.
 */

public class ContextHolder {

    private static Context appContext;

    public static synchronized Context getAppContext() {
        return appContext;
    }

    public static synchronized void setAppContext(Context appContext) {
        ContextHolder.appContext = appContext;
    }

}
