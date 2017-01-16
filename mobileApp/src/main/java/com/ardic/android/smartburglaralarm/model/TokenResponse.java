package com.ardic.android.smartburglaralarm.model;

/**
 * Created by murat on 03.01.2017.
 * {
 "createDate": {TOKEN_CREATE_DATE},
 "expireDate": {TOKEN_EXPIRE_DATE},
 "id": {TOKEN_ID},
 "token": {SUBSCRIBE_TOKEN},
 "url": {URL_FOR_WS}
 }
 */
public class TokenResponse {
    private Long createDate;
    private Long expireDate;
    private String id;
    private String token;
    private String url;

    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    public Long getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Long expireDate) {
        this.expireDate = expireDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "TokenResponse{" +
                "createDate=" + createDate +
                ", expireDate=" + expireDate +
                ", id='" + id + '\'' +
                ", token='" + token + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
