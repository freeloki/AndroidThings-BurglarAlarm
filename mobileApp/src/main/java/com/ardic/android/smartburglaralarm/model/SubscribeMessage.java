package com.ardic.android.smartburglaralarm.model;

/**
 * Created by murat on 04.01.2017.
 *  var subscribeMessage = new Object();
 11
 subscribeMessage.deviceId = DEVICE_ID;
 12
 subscribeMessage.nodeId = NODE_ID;
 13
 subscribeMessage.sensorId = SENSOR_ID;
 14
 subscribeMessage.id = subscribeId;
 15
 subscribeMessage.version = "2.0";
 16
 subscribeMessage.token = subscribeToken;
 17
 var subscribe = new Object();
 18
 subscribe.type = "subscribe";
 19
 subscribe.message = subscribeMessage;

 */

public class SubscribeMessage {
    private String deviceId;
    private String nodeId;
    private String sensorId;
    private String subscribeId;
    private String version;
    private String token;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

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

    public String getSubscribeId() {
        return subscribeId;
    }

    public void setSubscribeId(String subscribeId) {
        this.subscribeId = subscribeId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "SubscribeMessage{" +
                "deviceId='" + deviceId + '\'' +
                ", nodeId='" + nodeId + '\'' +
                ", sensorId='" + sensorId + '\'' +
                ", subscribeId='" + subscribeId + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
