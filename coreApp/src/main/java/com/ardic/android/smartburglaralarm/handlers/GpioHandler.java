package com.ardic.android.smartburglaralarm.handlers;

import android.content.Context;
import android.util.Log;

import com.ardic.android.smartburglaralarm.constant.BurglarAlarmConstants;
import com.ardic.android.smartburglaralarm.service.ContextHolder;
import com.google.android.things.contrib.driver.button.Button;
import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

/**
 * Created by yavuz.erzurumlu on 1/4/17.
 */

public class GpioHandler implements Button.OnButtonEventListener {

    private final String GPIO_ALARM_TRIGGER = "BCM21";
    private final String GPIO_ALARM_MOTION = "BCM20";
    private Context mContext;
    private PeripheralManagerService manager;
    private Gpio mAlarmGpio;
    private Button mMotion;
    private boolean lastMotionState = false;

    @Override
    public void onButtonEvent(Button button, boolean pressed) {
        Log.i(BurglarAlarmConstants.LOG_TAG, "INPUT :" + pressed);
        lastMotionState = pressed;
    }


    public static class InstanceHolder {
        private static GpioHandler INSTANCE = new GpioHandler();
    }

    private GpioHandler() {
        EventBus.getDefault().register(this);
        mContext = ContextHolder.getAppContext();
        manager = new PeripheralManagerService();
        try {
            mAlarmGpio = manager.openGpio(GPIO_ALARM_TRIGGER);
            mAlarmGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);

            mMotion = new Button(GPIO_ALARM_MOTION,
                    Button.LogicState.PRESSED_WHEN_HIGH
            );

            mMotion.setOnButtonEventListener(this);
        } catch (IOException e) {
            Log.e(BurglarAlarmConstants.LOG_TAG, "IOException :" + e);
        }

    }

    public static GpioHandler getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public void setGpioAlarm(boolean state) {
        if (mAlarmGpio != null) {
            try {
                mAlarmGpio.setValue(state);
            } catch (IOException e) {
                Log.e(BurglarAlarmConstants.LOG_TAG, "IOException : " + e);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSwitchEvent(EventHandler event) {
        Log.i(BurglarAlarmConstants.LOG_TAG, "Event received in GpioHandler");
        try {
            mAlarmGpio.setValue(event.getState());
        } catch (IOException e) {
            Log.e(BurglarAlarmConstants.LOG_TAG, "IOException : " + e);
        }
    }
    public void destroy(){
        EventBus.getDefault().unregister(this);




    }


}


