package com.ardic.android.smartburglaralarm.model.response;

/**
 * Created by murat on 05.01.2017.
 * {"topic":"ignite.com","subTopic":"ignite.com_muratb.irben_gmail.com","dmId":"0","date":1483590384325,"type":"DeviceInfo"}}
 */

public class Header {
    private String topic;
    private String subTopic;
    private String dmId;
    private Long date;
    private String type;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getSubTopic() {
        return subTopic;
    }

    public void setSubTopic(String subTopic) {
        this.subTopic = subTopic;
    }

    public String getDmId() {
        return dmId;
    }

    public void setDmId(String dmId) {
        this.dmId = dmId;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
