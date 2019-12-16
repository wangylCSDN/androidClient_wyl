package com.example.wqter.androidclient_mypart.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.wqter.androidclient_mypart.Bean.HisAlarmBean;
import com.example.wqter.androidclient_mypart.R;

import java.util.List;

/**
 * Created by wangyl on 2018/11/17.
 */

public class HisAlarmAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private List<HisAlarmBean> mDataList;

    public HisAlarmAdapter(Context context, List<HisAlarmBean> list) {
        mLayoutInflater = LayoutInflater.from(context);
        mDataList = list;
    }


    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        HisAlarmAdapter.ViewHolder holder = null;
        if(convertView==null){

            holder=new HisAlarmAdapter.ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.item_his_alarm_content, null);

            holder.device_name=(TextView)convertView.findViewById(R.id.item_his_alarm_devicename_tv);
            holder.alarm_info=(TextView)convertView.findViewById(R.id.item_his_alarm_alarminfo_tv);
            holder.alarm_type=(TextView)convertView.findViewById(R.id.item_his_alarm_alarmtype_tv);
            holder.start_time=(TextView)convertView.findViewById(R.id.item_his_alarm_start_tv);
            holder.alarm_reason=(TextView)convertView.findViewById(R.id.item_his_alarm_reason_tv);
            holder.alarm_solve=(TextView)convertView.findViewById(R.id.item_his_alarm_solve_tv);
            holder.end_time=(TextView)convertView.findViewById(R.id.item_his_alarm_end_tv);
            convertView.setTag(holder);

        } else {
            holder = (HisAlarmAdapter.ViewHolder) convertView.getTag();
        }

        HisAlarmBean bean=mDataList.get(position);//position
        holder.device_name.setText(bean.device_name);
        holder.alarm_info.setText(bean.alarm_info);
        holder.alarm_type.setText(bean.alarm_type);
        holder.start_time.setText(bean.start_time);
        holder.alarm_reason.setText(bean.alarm_reason);
        holder.alarm_solve.setText(bean.solve_method);
        holder.end_time.setText(bean.end_time);

//        int time= Integer.parseInt(bean.alarm_time);
//        int second=time%60;
//        int minutes=(time/60)%60;
//        int hours=(time/3600)%24;
//        int day=time/86400;
//        String ala_time;
//        if(day==0){
//            if(hours==0){
//                if(minutes==0){
//                    ala_time=second+"秒";
//                }else {
//                    ala_time=minutes+"分"+second+"秒";
//                }
//
//            }else{
//                ala_time=hours+"时"+minutes+"分"+second+"秒";
//            }
//        }else {
//            ala_time = day + "天" + hours + "时" + minutes + "分" + second + "秒";
//        }
//        holder.machine_id.setText(bean.machine_id);
//        holder.alarm_name.setText(bean.alarm_name);
//        holder.alarm_time.setText(ala_time);
//        holder.start_time.setText(bean.start_time);


        if (position % 2 == 0) {               //设置隔行变色
            convertView.setBackgroundColor(Color.parseColor("#E4E2E2"));
        }
//        else {
//            convertView.setBackgroundColor(Color.parseColor("#ffffff"));
//        }
        return convertView;
    }

    private class ViewHolder{

        TextView device_name;
        TextView alarm_info;
        TextView alarm_type;
        TextView start_time;
        TextView alarm_reason;
        TextView alarm_solve;
        TextView end_time;

    }
}
