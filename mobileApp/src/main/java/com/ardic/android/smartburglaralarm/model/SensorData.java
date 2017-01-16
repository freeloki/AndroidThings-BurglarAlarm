package com.ardic.android.smartburglaralarm.model;

/**
 * Created by murat on 02.01.2017.
 * {
 "code": null,
 "subCode": null,
 "status": "OK",
 "description": null,
 "data": {
 "deviceId": "5c:f8:a1:14:a2:8d@iotigniteagent",
 "command": "SensorData",
 "data": "[\"53\"]",
 "createDate": 1483354391674,
 "nodeId": "VirtualDemoNode",
 "sensorId": "Temperature",
 "cloudDate": 1483354389854
 },
 "ok": true
 }
 */

public class SensorData {
    private String data;
    private Long createDate;
    private Long cloudDate;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    public Long getCloudDate() {
        return cloudDate;
    }

    public void setCloudDate(Long cloudDate) {
        this.cloudDate = cloudDate;
    }
}
