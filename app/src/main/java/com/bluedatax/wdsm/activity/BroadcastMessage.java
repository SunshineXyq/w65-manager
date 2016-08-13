package com.bluedatax.wdsm.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bluedatax.wdsm.R;

public class BroadcastMessage extends ActionBarActivity {

    private ImageView arrowReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_message);
        arrowReturn = (ImageView) findViewById(R.id.ar_return);
        arrowReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
