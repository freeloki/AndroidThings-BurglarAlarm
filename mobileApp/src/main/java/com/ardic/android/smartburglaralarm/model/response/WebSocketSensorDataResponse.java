package com.ardic.android.smartburglaralarm.model.response;

/**
 * Created by murat on 05.01.2017.
 */

public class WebSocketSensorDataResponse {
    private Body body;
    private Header header;

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }
}
