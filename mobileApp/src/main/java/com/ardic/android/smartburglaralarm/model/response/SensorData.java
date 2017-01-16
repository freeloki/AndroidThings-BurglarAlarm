package com.ardic.android.smartburglaralarm.model.response;

import java.util.List;

/**
 * Created by murat on 05.01.2017.
 */

public class SensorData {
    private List<String> values;
    private Long date;

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }
}
