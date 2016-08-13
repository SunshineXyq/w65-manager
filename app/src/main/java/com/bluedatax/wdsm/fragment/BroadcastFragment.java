package com.bluedatax.wdsm.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.bluedatax.wdsm.R;
import com.bluedatax.wdsm.activity.SendBroadcast;
import com.bluedatax.wdsm.adapter.CommunityAdapter;
import com.bluedatax.wdsm.service.MyService;
import com.bluedatax.wdsm.utils.GetTime;
import com.bluedatax.wdsm.utils.SharedPrefsUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BroadcastFragment extends Fragment {

    private Button sendBroadcast;
    private String currentTime;
    private ListView lvCommunity;
    private ArrayList<String> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_broadcast_fragment,null);
        list = new ArrayList<>();
        for (int i=0;i<10;i++) {
            list.add("你好");
        }
        currentTime = GetTime.getCurrentTime();
        lvCommunity = (ListView) view.findViewById(R.id.lv_community);
        long loginToken = SharedPrefsUtil.getLong(getActivity().getApplicationContext(),"token",0L);
        System.out.println("取出的token"+loginToken);
//        queryEquipment(loginToken);
        CommunityAdapter adapter = new CommunityAdapter(getActivity(),list);
        lvCommunity.setAdapter(adapter);
        return view;
    }

    /**
     * 查询设备
     */

    private void queryEquipment(long token) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msg",152);
            jsonObject.put("token",token);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("gid","21");
            jsonBody.put("ts",currentTime);
            JSONObject jsonGeo = new JSONObject();
            jsonGeo.put("lng","116.340071");
            jsonGeo.put("lat","40.066179");
            jsonBody.put("geo",jsonGeo);

            jsonObject.put("body",jsonBody);

            MyService.mConnection.sendTextMessage(jsonObject.toString());

            Log.d("Query equipment",jsonObject.toString());
        } catch (JSONException e) {
                e.printStackTrace();
        }
    }

}
