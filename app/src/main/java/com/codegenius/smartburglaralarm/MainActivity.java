package com.codegenius.smartburglaralarm;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;

public class MainActivity extends Activity {

    private static final String GPIO_ALARM_TRIGGER = "BCM21";
    private static final String TAG = "Smart Burglar" ;
    private Gpio mGpio;
    private Switch mAlarmTriggerSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAlarmTriggerSwitch = (Switch) findViewById(R.id.alarmTrigger);

        mAlarmTriggerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                try {
                    mGpio.setValue(b);
                    Log.i(TAG,"GPIO SET TO " + mGpio.getValue());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        PeripheralManagerService manager = new PeripheralManagerService();
        try {
            mGpio = manager.openGpio(GPIO_ALARM_TRIGGER);
            mGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
        } catch (IOException e) {
            Log.w(TAG, "Unable to access GPIO", e);
        }

    }
}
