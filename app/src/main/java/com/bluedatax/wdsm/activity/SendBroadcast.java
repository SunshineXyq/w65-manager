package com.bluedatax.wdsm.activity;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bluedatax.wdsm.R;
import com.bluedatax.wdsm.adapter.BroadcastAdapter;
import com.bluedatax.wdsm.service.MyService;
import com.bluedatax.wdsm.service.OnJSONObjectListener;
import com.bluedatax.wdsm.utils.GetTime;
import com.bluedatax.wdsm.utils.HttpUtils;
import com.bluedatax.wdsm.utils.SendBroadcastData;
import com.bluedatax.wdsm.utils.SharedPrefsUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SendBroadcast extends ActionBarActivity implements View.OnTouchListener {

    private Button sendVoice;
    private ListView lvBroadcast;
    private int time;
    private MediaRecorder mRecorder;
    private String path;
    private Timer mTimer;
    private TextView mTextViewRecordTime;
    private ImageView mImageViewShow;
    private AnimationDrawable animationDrawable;
    private List<SendBroadcastData> mDatas;
    private Dialog dialog;
    private String startRecordTime;
    private BroadcastAdapter adapter;
    public static File saveFilePath;
    private MyService mService;
    private int fileId;
    private String appfileId;
    private String auidId;
    private String authName;
    private String fub;
    private String auid;
    private boolean _isExe;

    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = ((MyService.MyBinder) service).getService();
            mService.setOnJSONObjectListener(new OnJSONObjectListener() {
            @Override
            public void onConnect(String message) {

            }
            @Override
            public void onPushBroadcast(JSONObject Json) {
                try {
                    JSONArray jsonArray = Json.getJSONObject("body").getJSONArray("file");
                    fileId = jsonArray.getJSONObject(0).getInt("file_id");
                    appfileId = jsonArray.getJSONObject(0).getString("app_file_id");
                    auidId = Json.getJSONObject("body").getString("auth_id");
                    authName = Json.getJSONObject("body").getString("auth_name");
                    System.out.println(fileId + "\n" + auidId + "\n" + authName + appfileId + "\n");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (!_isExe) {
                    UploadRecordTask task = new UploadRecordTask();
                    task.execute(String.format("%s?prod=w65&id=1&fttype=ul_files&auth_user=w65_wdas&" +
                            "file_id=%s&fname=&auth_id=%s&auth_name=%s&type=json&bdx=prod", fub, fileId, auid, authName));
                }
                _isExe = true;
            }

            @Override
            public void onJSONObject(JSONObject Json) {

            }
           });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

    };
    private String currentTime;
    private String localFileId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_broadcast);
        currentTime = GetTime.getCurrentTime();
        sendVoice = (Button) findViewById(R.id.imageview_mic);
        sendVoice.setOnTouchListener(this);
        fub = SharedPrefsUtil.getValue(this, "fub", "");
        auid = SharedPrefsUtil.getValue(this, "auid", "");
        Intent in = new Intent(SendBroadcast.this,MyService.class);
        bindService(in,conn, Context.BIND_AUTO_CREATE);
        mDatas = new ArrayList<SendBroadcastData>();
        lvBroadcast = (ListView) findViewById(R.id.lv_broadcast);
        adapter = new BroadcastAdapter(SendBroadcast.this, mDatas);
        lvBroadcast.setAdapter(adapter);
        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/w65/manage/recorder/";
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        /**
         * 播放设备电话留言
         */
        lvBroadcast.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    MediaPlayer mPlayer = new MediaPlayer();
                    mPlayer.reset();
                    mPlayer.setDataSource(mDatas.get(position).getPath());
                    System.out.println(mDatas.get(position).getPath());
                    if (!mPlayer.isPlaying()) {
                        mPlayer.prepare();
                        mPlayer.start();
                    } else {
                        mPlayer.pause();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            startRecordTime = GetTime.getCurrentTime();
            time = 0;
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);           //声音来源麦克风
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);   //输出格式3gp
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);     //编码格式
            localFileId = new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis());
            try {
                String paths = path                                            //文件存储目录
                        + localFileId //文件命名格式
                        + ".amr";
                saveFilePath = new File(paths);
                mRecorder.setOutputFile(saveFilePath.getAbsolutePath());
                System.out.println("语音路径" + saveFilePath.getAbsolutePath());
                mRecorder.prepare();
                mRecorder.start();

            } catch (IOException e) {
                Log.d("00000000", "prepare() failed");
                System.out.println(e);
            }
            showRecorderDialog();
            timedTask();
            animationDrawable.start();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            mTimer.cancel();
            animationDrawable.stop();
            dialog.dismiss();
            mRecorder.stop();//停止录音
            mRecorder.release();//释放资源
            mRecorder = null;
            System.out.println("录音结束后语音路径" + saveFilePath.getAbsolutePath());
            System.out.println("时间" + time);
            System.out.println("开始时间" + startRecordTime);
            mDatas.add(new SendBroadcastData(saveFilePath.getAbsolutePath(), time, startRecordTime));
            adapter.notifyDataSetChanged();
            pushSendBroadcast(localFileId);
        }
        return false;
    }

    android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x09:
                    mTextViewRecordTime.setText("正在录音..." + time);
                    break;
                default:
                    break;
            }
        }
    };


    /**
     * 计算录音时的时间
     */

    private void timedTask() {
        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                Message message = handler.obtainMessage();
                message.obj = time;
                message.what = 0x09;
                handler.sendMessage(message);
                time++;
            }
        }, 0, 1000);
    }

    /**
     * 录音的时候弹出一个diglog来显示录音的时间
     */
    private void showRecorderDialog() {
        dialog = new Dialog(this, R.style.callserviceDialogTheme);
        LayoutInflater inflater = getLayoutInflater();
        View dialogSelf = inflater.inflate(R.layout.recorder_time, null);
        mTextViewRecordTime = (TextView) dialogSelf.findViewById(R.id.textview_recordertime);
        mImageViewShow = (ImageView) dialogSelf.findViewById(R.id.imageview_animation);
        animationDrawable = (AnimationDrawable) mImageViewShow.getBackground();
        dialog.setContentView(dialogSelf);
        dialog.show();
    }

    /**
     * 请求推送电话留言，获取fileId 用于上传文件
     *
     * @param id 本地文件的id
     */
    private void pushSendBroadcast(String id) {
        try {
            JSONObject jsonGeo = new JSONObject();
            jsonGeo.put("lat", "116.350831");
            jsonGeo.put("lng", "40.061487");
            Log.d("json经纬度", jsonGeo.toString());

            JSONObject jsReq = new JSONObject();
            jsReq.put("stype", "broadcast_msg");
            jsReq.put("action", "push");
            jsReq.put("title", "");
            jsReq.put("file_id", "");
            jsReq.put("app_file_id", Integer.parseInt(id));
            jsReq.put("dt_s", "");
            jsReq.put("dt_e", "");
            JSONObject req = new JSONObject(jsReq.toString());

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("gdid", 1);
            jsonBody.put("gdid", "1-1001");
            jsonBody.put("geo", jsonGeo);
            jsonBody.put("req", req);
            jsReq.put("ts", currentTime);

            JSONObject jsonReq = new JSONObject();
            jsonReq.put("msg", 154);

            jsonReq.put("body", jsonBody);

            MyService.mConnection.sendTextMessage(jsonReq.toString());

            System.out.println("send Broadcast:" + jsonReq.toString());

        } catch (Exception e) {

        }
    }

    class UploadRecordTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            System.out.println("[downloadImageTask->]doInBackground " + params[0]);
            String response = HttpUtils.uploadRing(params[0]);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            System.out.println("result = " + result);
            super.onPostExecute(result);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }
}
