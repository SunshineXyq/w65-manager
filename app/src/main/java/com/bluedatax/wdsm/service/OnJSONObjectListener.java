package com.bluedatax.wdsm.service;

import org.json.JSONObject;

/**
 * Created by xuyuanqiang on 7/22/16.
 */
public interface OnJSONObjectListener {
    void onConnect(String message);
    void onPushBroadcast(JSONObject Json);
    void onJSONObject(JSONObject Json);
}
