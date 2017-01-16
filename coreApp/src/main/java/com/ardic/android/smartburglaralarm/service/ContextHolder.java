package com.ardic.android.smartburglaralarm.service;

import android.content.Context;

/**
 * Created by yavuz.erzurumlu on 1/4/17.
 */

public class ContextHolder {
    private static Context appContext;
    private static ContextHolder instance;

    private ContextHolder(Context appContext){
        this.appContext = appContext;

    }

    public static ContextHolder getInstance(Context appContext){
        if(instance == null){
            instance = new ContextHolder(appContext);
        }
        return instance;
    }

    public static synchronized Context getAppContext() {
        return instance.getContext();
    }
    public  Context getContext() {
        return appContext;
    }

}
