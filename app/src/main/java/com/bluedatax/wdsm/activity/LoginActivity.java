package com.bluedatax.wdsm.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bluedatax.wdsm.MainActivity;
import com.bluedatax.wdsm.R;
import com.bluedatax.wdsm.service.MyService;
import com.bluedatax.wdsm.service.OnJSONObjectListener;
import com.bluedatax.wdsm.utils.GetAppVersion;
import com.bluedatax.wdsm.utils.GetTime;
import com.bluedatax.wdsm.utils.MD5Utils;
import com.bluedatax.wdsm.utils.SharedPrefsUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends ActionBarActivity implements View.OnClickListener {

    private Button btnLogin;
    private MyService mService;
    private Intent in;
    private EditText etUsename;
    private EditText etPassword;
    private String currentTime;
    private String name;
    private String version;
    private String duid;
    private String model;

    private Activity mContext = this;


    ServiceConnection coon = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            System.out.println("绑定成功");
            startService(in);
            mService = ((MyService.MyBinder) service).getService();
            mService.setOnJSONObjectListener(new OnJSONObjectListener() {

                @Override
                public void onConnect(String message) {
                    Log.d("message", message);
                    if (message.equals("Connected")) {
                        appInit();
                    }
                }

                @Override
                public void onPushBroadcast(JSONObject json) {

                }

                @Override
                public void onJSONObject(JSONObject json) {
                    try {
                        if (json.getInt("msg") == 150) {
                            parseLoginRespond(json);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private String aver;
    private long token;
    private String fub;
    private String auid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        currentTime = GetTime.getCurrentTime();
        name = Build.MANUFACTURER;             //生产厂家   name
        version = Build.VERSION.RELEASE;       //固件版本   version  sver
        model = Build.MODEL;                   //机型
        GetAppVersion getAppVersion = new GetAppVersion(LoginActivity.this);
        aver = getAppVersion.getVersion();
        getPosition();
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String DEVICE_ID = tm.getDeviceId();
        String MD5DeviceID = MD5Utils.encode(DEVICE_ID);
        Log.d("MD5设备号码", MD5DeviceID);
        StringBuffer sb = new StringBuffer(MD5DeviceID);
        sb.insert(6, "-");
        sb.insert(11, "-");
        sb.insert(16, "-");
        sb.insert(21, "-");
        duid = sb.toString();                  //唯一区分设备ID
        Log.d("duid", duid);
        in = new Intent(LoginActivity.this, MyService.class);
        in.putExtra("duid", duid);
//        bindService(in, coon, Context.BIND_AUTO_CREATE);
        initView();
    }


    private void initView() {
        btnLogin = (Button) findViewById(R.id.bt_login);
        etUsename = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);

        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:
                String usename = etUsename.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
//                sendLoginRequest(usename, password);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;

            default:
                break;
        }
    }

    /**
     * 连接服务器后初始化
     */

    private void appInit() {
//        mSharedPreference = getSharedPreferences("count", MODE_PRIVATE);
//        latitude = mSharedPreference.getString("lat", "");
//        lnglatitude = mSharedPreference.getString("lng", "");
//        Log.d("取出存入的经纬度", latitude + "#" + lnglatitude);

        try {
            JSONObject jsonGeo = new JSONObject();
            jsonGeo.put("lat", "40.066179");
            jsonGeo.put("lng", "116.340071");
            Log.d("json经纬度", jsonGeo.toString());

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("duid", duid);
            Log.d("duid", duid);
            jsonBody.put("name", name);
            Log.d("name", name);
            jsonBody.put("model", model);
            Log.d("model", model);
            jsonBody.put("sver", version);
            Log.d("sver", version);
            jsonBody.put("aver", aver);
            Log.d("aver", aver);
            jsonBody.put("type", "2");
            jsonBody.put("ts", currentTime);
            jsonBody.put("geo", jsonGeo);

            JSONObject jsonReq = new JSONObject();
            jsonReq.put("msg", 10);
            jsonReq.put("body", jsonBody);

            MyService.mConnection.sendTextMessage(jsonReq.toString());

            System.out.println("Init request:" + jsonReq.toString());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 发送登录请求
     */
    private void sendLoginRequest(String name, String pwd) {
        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject jsonGeo = new JSONObject();
            jsonGeo.put("lng", "116.340071");
            jsonGeo.put("lat", "40.066179");

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("upn", name);
            jsonBody.put("pwd", pwd);
            jsonBody.put("ts", currentTime);
            jsonBody.put("geo", jsonGeo);

            jsonObject.put("msg", 150);
            jsonObject.put("body", jsonBody);

            MyService.mConnection.sendTextMessage(jsonObject.toString());

            System.out.println("Login request:" + jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析登录返回的json数据
     *
     * @param json 服务器返回的json串
     */

    private void parseLoginRespond(JSONObject json) {
        try {
            token = json.getLong("token");
            Log.d("解析后的token数据", token + "");
            JSONObject body = json.getJSONObject("body");
            Log.d("解析后的body数据", body + "");
            String name = body.getString("name");
            Log.d("解析后的name数据", name);
            auid = body.getString("auid");
            Log.d("auid", auid);
            int ast = body.getInt("ast");
            Log.d("解析后的ast数据", ast + "");
            fub = body.getString("fub");
            Log.d("解析后的fub数据", fub);
            String ts = body.getString("ts");
            Log.d("解析后的tm数据", ts);

        } catch (Exception e) {
        }
        SharedPrefsUtil.putValue(LoginActivity.this, "fub", fub);
        SharedPrefsUtil.putValue(mContext, "auid", auid);
        SharedPrefsUtil.putLong(mContext, "token", token);
    }


    private void getPosition() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unbindService(coon);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == event.ACTION_DOWN) {
            MyService.mConnection.disconnect();
        }
        return super.onKeyDown(keyCode, event);
    }
}
