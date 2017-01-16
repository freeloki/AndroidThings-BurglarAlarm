package com.ardic.android.smartburglaralarm.model;

/**
 * Created by murat on 04.01.2017.
 */

public class Subscribe {
    private SubscribeMessage message;
    private String type;

    public SubscribeMessage getMessage() {
        return message;
    }

    public void setMessage(SubscribeMessage message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Subscribe{" +
                "message=" + message +
                ", type='" + type + '\'' +
                '}';
    }
}
