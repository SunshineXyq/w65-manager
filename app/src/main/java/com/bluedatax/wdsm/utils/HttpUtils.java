package com.bluedatax.wdsm.utils;

import com.bluedatax.wdsm.activity.SendBroadcast;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by xuyuanqiang on 7/22/16.
 */
public class HttpUtils {

    /**
     * 上传铃声
     * @param urlString    铃声地址
     * @return             判断是否上传成功
     */

    public static String uploadRing(String urlString) {
        URL url = null;
        String message = null;
        HttpURLConnection urlConn = null;
        String recordPath = null;
        try {
            url = new URL(urlString);
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setRequestProperty("ServerProvider", "BDX");
            urlConn.setConnectTimeout(5000);
            urlConn.setRequestMethod("POST");
            urlConn.setDoOutput(true);
            OutputStream out = urlConn.getOutputStream();

            FileInputStream fis = new FileInputStream(SendBroadcast.saveFilePath.getAbsolutePath());
            System.out.println(SendBroadcast.saveFilePath.getAbsolutePath());
            int len;
            byte[] buffer = new byte[1024];
            while ((len = fis.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
            out.close();
            fis.close();
            urlConn.connect();

            int responseCode = urlConn.getResponseCode();
            System.out.println("错误码-----------"+responseCode);
            message = urlConn.getResponseMessage();

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            System.out.println("[getNetWorkBitmap->]MalformedURLException");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("[getNetWorkBitmap->]IOException");
            e.printStackTrace();
        } finally {
            if (urlConn != null) {
                urlConn.disconnect();
            }
        }
        return message;
    }
}
