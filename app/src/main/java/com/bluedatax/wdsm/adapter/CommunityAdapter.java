package com.bluedatax.wdsm.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bluedatax.wdsm.R;
import com.bluedatax.wdsm.activity.SendBroadcast;

import java.util.List;

/**
 * Created by xuyuanqiang on 7/30/16.
 */
public class CommunityAdapter extends BaseAdapter{

    private Context context;
    private List<String> data;

    public CommunityAdapter(Context context,List<String> data) {
        this.context = context;
        this.data = data;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_community_item,null);
            holder = new ViewHolder();
            holder.itemSendBroadcast = (ImageView) convertView.findViewById(R.id.item_send_broadcast);
            holder.personalInfoOne = (LinearLayout) convertView.findViewById(R.id.personal_information_one);
            holder.personalInfoTwo = (RelativeLayout) convertView.findViewById(R.id.personal_information_two);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.itemSendBroadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SendBroadcast.class);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        ImageView itemSendBroadcast;
        LinearLayout personalInfoOne;
        RelativeLayout personalInfoTwo;
    }
}
