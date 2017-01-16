package com.ardic.android.smartburglaralarm.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.ardic.android.smartburglaralarm.R;
import com.ardic.android.smartburglaralarm.handlers.EventHandler;
import com.ardic.android.smartburglaralarm.handlers.GpioHandler;
import com.ardic.android.smartburglaralarm.service.ContextHolder;

import org.greenrobot.eventbus.EventBus;

public class MainActivity extends Activity {


    private Switch mAlarmTriggerSwitch;
    private ContextHolder mContextHolder;
    private GpioHandler mGpioHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContextHolder = ContextHolder.getInstance(getApplicationContext());

        mGpioHandler = GpioHandler.getInstance();

        mAlarmTriggerSwitch = (Switch) findViewById(R.id.alarmTrigger);

        mAlarmTriggerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    EventBus.getDefault().post(new EventHandler(b));
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGpioHandler.destroy();
    }
}
