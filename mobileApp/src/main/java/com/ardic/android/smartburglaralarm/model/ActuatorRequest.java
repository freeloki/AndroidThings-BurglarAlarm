package com.ardic.android.smartburglaralarm.model;

/**
 * Created by murat on 02.01.2017.
 * {
 "nodeId": "VirtualDemoNode",
 "params": "{\"message\": 1}",
 "sensorId": "Lamp",
 "type": "Lamp"
 }

 */
public class ActuatorRequest {
    private String nodeId;
    private String params;
    private String sensorId;
    private String type;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
