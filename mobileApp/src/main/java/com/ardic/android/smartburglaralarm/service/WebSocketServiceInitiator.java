package com.ardic.android.smartburglaralarm.service;

import android.util.Log;

import com.ardic.android.smartburglaralarm.activity.BurglarActivity;

import org.java_websocket.client.DefaultSSLWebSocketClientFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

/**
 * Created by murat on 04.01.2017.
 */

public class WebSocketServiceInitiator implements Runnable{
    private static final String TAG = "WebSocketServiceInit";

    private WebsocketService websocketService;
    private String url;
    private BurglarActivity burglarActivity;

    public WebSocketServiceInitiator(String url, BurglarActivity burglarActivity) {
        this.url = url;
        this.burglarActivity = burglarActivity;
    }

    @Override
    public void run() {
        try {
            Log.i(TAG, "Connecting to: " + url);
            websocketService = new WebsocketService(new URI(url));
            SSLContext sslContext = null;
            try {
                sslContext = SSLContext.getInstance( "TLS" );
                sslContext.init( null, null, null ); // will use java's default key and trust store which is sufficient unless you deal with self-signed certificates
            } catch (NoSuchAlgorithmException e) {
               Log.e(TAG, e.getLocalizedMessage());
            } catch (KeyManagementException e) {
                Log.e(TAG, e.getLocalizedMessage());
            }

            websocketService.setWebSocketFactory( new DefaultSSLWebSocketClientFactory( sslContext ) );
            websocketService.connect();
        } catch (URISyntaxException e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
    }

}
