package com.bluedatax.wdsm.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bluedatax.wdsm.R;
import com.bluedatax.wdsm.activity.BroadcastMessage;
import com.bluedatax.wdsm.utils.SendBroadcastData;

import java.util.List;

/**
 * Created by xuyuanqiang on 7/21/16.
 */
public class BroadcastAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<SendBroadcastData> mData;
    private Context context;

    public BroadcastAdapter(Context context,List<SendBroadcastData> Data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.mData = Data;
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SendBroadcastData data = mData.get(position);

        System.out.println("传递过来开始录音时间" + mData.get(position).getStartTime());
        System.out.println("传递过来的录音长度" + mData.get(position).getTime());
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_leave_message,null);
            holder = new ViewHolder();
            holder.textViewMessage = (TextView) convertView.findViewById(R.id.textview_leavemessage);
            holder.textViewTime= (TextView) convertView.findViewById(R.id.textview_leavemessage_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        System.out.println(data.getTime() + "----------" + data.getStartTime());
        holder.textViewTime.setText(data.getStartTime() + "" + "  " +data.getTime() + "" + "s");

        holder.textViewMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,BroadcastMessage.class);
                context.startActivity(intent);
            }
        });

        return convertView;
    }
    class ViewHolder {
         TextView textViewTime;
         TextView textViewMessage;
    }
}



