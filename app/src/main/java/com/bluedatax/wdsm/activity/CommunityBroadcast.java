package com.bluedatax.wdsm.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.bluedatax.wdsm.R;

public class CommunityBroadcast extends ActionBarActivity {

    private Button send_broadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_broadcast);
        send_broadcast = (Button) findViewById(R.id.sendBroadcast);
        send_broadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommunityBroadcast.this,SendBroadcast.class);
                startActivity(intent);
            }
        });
    }
}
