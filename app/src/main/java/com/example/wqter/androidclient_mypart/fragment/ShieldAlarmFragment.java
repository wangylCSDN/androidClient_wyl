package com.example.wqter.androidclient_mypart.fragment;

import android.app.Fragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.wqter.androidclient_mypart.Adapter.HisAlarmAdapter;
import com.example.wqter.androidclient_mypart.Adapter.ShieldAlarmAdapter;
import com.example.wqter.androidclient_mypart.Bean.HisAlarmBean;
import com.example.wqter.androidclient_mypart.Bean.ShieldAlarmBean;
import com.example.wqter.androidclient_mypart.R;
import com.example.wqter.androidclient_mypart.Sqlite.DBhelper;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import static com.scwang.smartrefresh.layout.util.DensityUtil.dp2px;

/**
 * Created by wqter on 2019/12/18.
 */

public class ShieldAlarmFragment extends android.support.v4.app.Fragment {

    private View mView;
    private RefreshLayout mRefreshLayout;
    private SwipeMenuListView shield_lv;
    private Handler handler;
    private SQLiteDatabase db;
    private List<String> list_id;
    private ImageView nodata;
    private int glo_pos;
    private AlertDialog.Builder allow_builder;
    private List<ShieldAlarmBean> dataList;
    private ShieldAlarmAdapter shield_adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化视图和数据
        mView = getActivity().getLayoutInflater().inflate(R.layout.fragment_shieldalarm,null);

        db= DBhelper.getDbHelpter(getActivity()).getWritableDatabase();
        initView(mView);
    }

    private void initView(View view) {

        list_id=new ArrayList<>();
        handler=new Handler();
        shield_lv=(SwipeMenuListView)view.findViewById(R.id.shield_alarm_lv);
        SwipeMenuCreator creator=new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem allowItem = new SwipeMenuItem(
                        getContext());
                allowItem.setBackground(new ColorDrawable(Color.rgb(0x10,
                        0xEE, 0x6D)));
                allowItem.setWidth(dp2px(150));
                allowItem.setTitle("允许报警");
                allowItem.setTitleSize(18);
                allowItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(allowItem);
            }
        };
        shield_lv.setMenuCreator(creator);
        shield_lv.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                glo_pos=position;
                switch (index){
                    case 0:
                        if(allow_builder==null){
                            allow_builder= new AlertDialog.Builder(getActivity());
                            allow_builder.setTitle("提示")
                                    .setMessage("是否确认允许该报警")
                                    .setPositiveButton(getString(R.string.common_dialog_true), new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            allow_alarm();
                                        }
                                    })
                                    .setNegativeButton(getString(R.string.common_dialog_false), new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                        }
                                    });
                        }
                        allow_builder.show();
                        break;
                }
                return false;
            }

        });
        shield_lv.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

        dataList=new ArrayList<>();
        mRefreshLayout=(RefreshLayout)view.findViewById(R.id.shield_alarm_refreshLayout);
        nodata=(ImageView)view.findViewById(R.id.shield_nodata);
        mRefreshLayout.setDisableContentWhenRefresh(true);
        mRefreshLayout.setDisableContentWhenLoading(true);

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

                mRefreshLayout.finishRefresh(2*1000);
                setData();//获取数据库数据并进行显示
                handler.postDelayed(timeTask, 10*1000);//相当于定时器的延时刷新
                // Log.v("refresh_lay","222");
            }
        });
        mRefreshLayout.autoRefresh();//refresh的header

    }
    private void allow_alarm()
    {
        ShieldAlarmBean bean=dataList.get(glo_pos);
        Cursor cursor=db.rawQuery("select deviceCode,com from table_deviceinformation where deviceName ='"+bean.device_name+"'",null);
        String device_code="";
        String com="";
        if(cursor.moveToFirst())
        {
            device_code=cursor.getString(cursor.getColumnIndex("deviceCode"));
            com=cursor.getString(cursor.getColumnIndex("com"));
        }
        cursor.close();
        String table_name="table_device_alarmcomment_ttyACM"+com+"device"+device_code;
        String sql_update= "update "+table_name+" set state ='1' where alarm_name = '"+bean.alarm_info+"' and devicename='"+bean.device_name+"'";
        db.execSQL(sql_update);

        String db_id=list_id.get(glo_pos);
        String sql="delete from table_shieldalarm where id='"+db_id+"'";
        db.execSQL(sql);
        list_id.remove(glo_pos);
        dataList.remove(glo_pos);
        shield_adapter.notifyDataSetChanged();
    }
    private Runnable timeTask = new Runnable() {
        @Override
        public void run() {
            setData();
            handler.postDelayed(timeTask,10*1000);
            // Log.v("refreshtime","1111");
        }
    };
    private void setData() {

        if(dataList.size()!=0)
        {
            dataList.clear();
        }
        if(list_id.size()!=0)
        {
            list_id.clear();
        }
        Cursor cursor = db.rawQuery("select * from table_shieldalarm", null);
        while (cursor.moveToNext()){
            String all_id=cursor.getString(cursor.getColumnIndex("id"));
            String device_name = cursor.getString(cursor.getColumnIndex("devicename"));
            String alarm_info = cursor.getString(cursor.getColumnIndex("alarminfo"));
            list_id.add(all_id);
            dataList.add(new ShieldAlarmBean(device_name,alarm_info));
        }
        cursor.close();
        mRefreshLayout.finishRefresh();
        if(dataList.size()==0)
        {
            nodata.setVisibility(View.VISIBLE);
        }
        else
        {
            nodata.setVisibility(View.GONE);
        }
        setList();
    }

    private void setList() {
        if(shield_adapter==null){
            shield_adapter=new ShieldAlarmAdapter(getActivity(),dataList);
            shield_lv.setAdapter(shield_adapter);
        }
        else {
            shield_adapter.notifyDataSetChanged();
        }

    }


    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return mView;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(timeTask);
        db=null;
        ViewGroup mGroup=(ViewGroup) mView.getParent();
        if(mGroup!=null){
            mGroup.removeView(mView);
        }
    }

    public ShieldAlarmFragment newInstance(String title){           //回调
        ShieldAlarmFragment fragment = new ShieldAlarmFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }
}
