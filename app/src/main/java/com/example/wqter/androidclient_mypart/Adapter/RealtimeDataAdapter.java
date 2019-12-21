package com.example.wqter.androidclient_mypart.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.wqter.androidclient_mypart.Bean.HisAlarmBean;
import com.example.wqter.androidclient_mypart.Bean.RealTimeBean;
import com.example.wqter.androidclient_mypart.R;

import java.util.List;

/**
 * Created by wqter on 2019/12/20.
 */

public class RealtimeDataAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private List<RealTimeBean> mDataList;

    public RealtimeDataAdapter( Context context, List<RealTimeBean> list)
    {
        mLayoutInflater = LayoutInflater.from(context);
        mDataList = list;
    }
    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int i) {
        return mDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RealtimeDataAdapter.ViewHolder holder=null;

        if(convertView==null){
            holder=new RealtimeDataAdapter.ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.item_realtimedata, null);
            holder.data_ch=(TextView)convertView.findViewById(R.id.item_real_data_ch);
            holder.data=(TextView)convertView.findViewById(R.id.item_real_data);
            convertView.setTag(holder);
        } else {
            holder = (RealtimeDataAdapter.ViewHolder) convertView.getTag();
        }
        RealTimeBean bean=mDataList.get(position);//position
        holder.data_ch.setText(bean.data_ch);
        holder.data.setText(bean.data);
        return convertView;
    }
    private class ViewHolder{

        TextView data_ch;
        TextView data;
    }
}
