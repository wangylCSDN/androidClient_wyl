package com.example.wqter.androidclient_mypart.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.wqter.androidclient_mypart.Bean.HisAlarmBean;
import com.example.wqter.androidclient_mypart.Bean.ShieldAlarmBean;
import com.example.wqter.androidclient_mypart.R;

import java.util.List;

/**
 * Created by wqter on 2019/12/19.
 */

public class ShieldAlarmAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private List<ShieldAlarmBean> mDataList;
    public ShieldAlarmAdapter(Context context,List<ShieldAlarmBean> list)
    {
        mLayoutInflater = LayoutInflater.from(context);
        mDataList = list;
    }

    @Override
    public int getCount() {  //赋list的长度
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {//赋项目,根据pos去取mdataList
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ShieldAlarmAdapter.ViewHolder holder=null;
        if(convertView==null){

            holder=new ShieldAlarmAdapter.ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.item_shield_alarm_content, null);

            holder.device_name=(TextView)convertView.findViewById(R.id.item_shield_alarm_devicename_tv);
            holder.alarm_info=(TextView)convertView.findViewById(R.id.item_shield_alarm_alarminfo_tv);
            convertView.setTag(holder);

        } else {
            holder = (ShieldAlarmAdapter.ViewHolder) convertView.getTag();
        }
        ShieldAlarmBean bean=mDataList.get(position);
        holder.device_name.setText(bean.device_name);
        holder.alarm_info.setText(bean.alarm_info);
        return convertView;
    }
    private class ViewHolder{

        TextView device_name;
        TextView alarm_info;
    }
}
