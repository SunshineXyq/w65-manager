package com.bluedatax.wdsm.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bluedatax.wdsm.R;
import com.bluedatax.wdsm.activity.SendBroadcast;

public class BroadcastFragment extends Fragment {

    private Button sendBroadcast;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_broadcast_fragment,null);
        sendBroadcast = (Button) view.findViewById(R.id.sendBroadcast);
        sendBroadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SendBroadcast.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
