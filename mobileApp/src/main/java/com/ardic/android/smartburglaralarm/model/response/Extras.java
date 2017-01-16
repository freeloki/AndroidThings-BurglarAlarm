package com.ardic.android.smartburglaralarm.model.response;

import java.util.List;

/**
 * Created by murat on 05.01.2017.
 */

public class Extras {
    private List<SensorData> sensorData;

    public List<SensorData> getSensorData() {
        return sensorData;
    }

    public void setSensorData(List<SensorData> sensorData) {
        this.sensorData = sensorData;
    }
}
