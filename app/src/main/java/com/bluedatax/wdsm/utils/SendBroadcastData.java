package com.bluedatax.wdsm.utils;

/**
 * Created by xuyuanqiang on 7/21/16.
 */
public class SendBroadcastData {

    private int time;
    private String path;
    private String startTime;


    public SendBroadcastData(String path, int time, String startTime) {
        this.path = path;
        this.time = time;
        this.startTime = startTime;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;

    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

}
