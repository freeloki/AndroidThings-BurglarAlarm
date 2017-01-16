package com.ardic.android.smartburglaralarm.handlers;

/**
 * Created by yavuz.erzurumlu on 1/4/17.
 */

public class EventHandler {
    private boolean switchState = false;

    public EventHandler(boolean state) {
        this.switchState = state;
    }

    public boolean getState(){
        return this.switchState;
    }
}
