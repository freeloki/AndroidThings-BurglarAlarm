package com.ardic.android.smartburglaralarm.model;

/**
 * Created by murat on 31.12.2016.
 */

public class SensorDataResponse {
    private SensorData data;
    private String status;


    public SensorData getData() {
        return data;
    }

    public void setData(SensorData data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
