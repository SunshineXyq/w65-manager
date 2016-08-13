package com.bluedatax.wdsm.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bluedatax.wdsm.MainActivity;
import com.bluedatax.wdsm.R;
import com.bluedatax.wdsm.fragment.CommunityFragment;

public class DeviceDetails extends ActionBarActivity {

    private ImageView ivReturn;
    private LinearLayout llPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_details);
        ivReturn = (ImageView) findViewById(R.id.iv_return);
        llPosition = (LinearLayout) findViewById(R.id.ll_position);
        llPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeviceDetails.this, MainActivity.class);
                intent.putExtra("id",1);
                startActivity(intent);
                finish();
            }
        });
        ivReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
