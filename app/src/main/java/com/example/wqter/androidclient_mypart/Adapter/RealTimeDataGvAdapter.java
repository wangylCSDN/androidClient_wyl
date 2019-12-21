package com.example.wqter.androidclient_mypart.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wqter.androidclient_mypart.Bean.RealTimeBean;
import com.example.wqter.androidclient_mypart.R;

import java.util.List;

/**
 * Created by wqter on 2019/12/20.
 */

public class RealTimeDataGvAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private List<RealTimeBean> mDataList;
    private int[] stateColor  ={R.drawable.green,R.drawable.red,R.drawable.grey};//不同状态的颜色
    private int[] image={R.mipmap.green,R.mipmap.red,R.mipmap.grey};

    public RealTimeDataGvAdapter (Context context, List<RealTimeBean> list)
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
    public View getView( final int position, View convertView, ViewGroup parent) {
        RealTimeDataGvAdapter.ViewHolder holder=null;
        if(convertView==null)
        {
            holder=new RealTimeDataGvAdapter.ViewHolder();
            convertView=mLayoutInflater.inflate(R.layout.item_realtime_data_gv,null);
            holder.gv_image=(ImageView)convertView.findViewById(R.id.ups_gv_image);
            holder.gv_text=(TextView)convertView.findViewById(R.id.ups_gv_tv);
            convertView.setTag(holder);
        }
        else
        {
            holder=(RealTimeDataGvAdapter.ViewHolder)convertView.getTag();
        }

        RealTimeBean bean =mDataList.get(position);
        if(bean.data.equals("报警"))
        {
            holder.gv_image.setBackgroundResource(stateColor[1]);//也可以换成imageResource，设置图片源
            holder.gv_text.setText(bean.data_ch);
        }
        else if(bean.data.equals("正常"))
        {
            holder.gv_image.setBackgroundResource(stateColor[1]);//也可以换成imageResource，设置图片源
            holder.gv_text.setText(bean.data_ch);
        }
        else
        {
            holder.gv_image.setBackgroundResource(stateColor[2]);
            holder.gv_text.setText(bean.data_ch);
        }
        return convertView;
    }
    private class ViewHolder{

        ImageView gv_image;
        TextView gv_text;
    }
}
