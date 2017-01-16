package com.ardic.android.smartburglaralarm.model.response;

/**
 * Created by murat on 05.01.2017.
 */

public class Body {
    private String nodeId;
    private String sensorId;
    private Extras extras;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public Extras getExtras() {
        return extras;
    }

    public void setExtras(Extras extras) {
        this.extras = extras;
    }
}
