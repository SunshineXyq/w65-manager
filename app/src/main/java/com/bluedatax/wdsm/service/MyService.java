package com.bluedatax.wdsm.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketConnectionHandler;
import de.tavendo.autobahn.WebSocketException;

/**
 * Created by xuyuanqiang on 7/22/16.
 */
public class MyService extends Service {


    public static WebSocketConnection mConnection;
    private OnJSONObjectListener onJSONObjectListener;

    public void setOnJSONObjectListener(OnJSONObjectListener onJSONObjectListener) {
        this.onJSONObjectListener = onJSONObjectListener;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String duid = intent.getStringExtra("duid");
        mConnection = new WebSocketConnection();
        try {
            mConnection.connect(String.format("ws://192.168.0.9:19000?connect&prod=w65" +
                    "&auid=89b5129f7d5f447562b632d724e4c7a0&duid=%s&apc=2",duid),new WebSocketConnectionHandler() {
                @Override
                public void onOpen() {
                    Log.d("Status", "connect:ws://192.168.0.5:19600/websocket");
                    Log.d("onConnect", "Connected!");
                    String notice = "Connected";
                    Log.d("判断连接状态", notice);
                    if(onJSONObjectListener != null ) {
                        Log.d("发送的通知", notice);
                        onJSONObjectListener.onConnect(notice);
                    }
                }

                @Override
                public void onTextMessage(String message) {
                    Log.d("onMessage", String.format("Got string message! %s", message));
                    try {
                        JSONObject jsonObject = new JSONObject(message);
                        System.out.println( "---------------" + jsonObject.getInt("msg"));
                        if (onJSONObjectListener != null && jsonObject.getInt("msg") == 10) {         //初始化
                            onJSONObjectListener.onJSONObject(jsonObject);
                        } else if (jsonObject.getInt("action") == 62) {                               //登录
                            onJSONObjectListener.onJSONObject(jsonObject);
                        } else if (onJSONObjectListener != null && jsonObject.getInt("action") == 61) {  //注册
                            onJSONObjectListener.onJSONObject(jsonObject);

                        } else if (onJSONObjectListener != null && jsonObject.getInt("msg") == 154 ) { //接收订单
                            onJSONObjectListener.onPushBroadcast(jsonObject);
                        }

                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }

                @Override
                public void onClose(int code, String reason) {
                    Log.d("Status","返回码" + code + "\n" + reason);
                    if (code == 2) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "网络连接错误，请重新连接!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        } catch (WebSocketException e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {

        return new MyBinder();
    }

    public class MyBinder extends Binder{

        public MyService getService() {

            return MyService.this;
        }
    }
}
