package com.bluedatax.wdsm.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xuyuanqiang on 7/21/16.
 */
public class GetTime {
    private static String CurrentTime;

    public static String getCurrentTime() {
        Date date = new Date();

        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//24小时制

        CurrentTime = sdformat.format(date);


        Log.d("静态方法当前时间为", CurrentTime);

        return CurrentTime;

    }

}
