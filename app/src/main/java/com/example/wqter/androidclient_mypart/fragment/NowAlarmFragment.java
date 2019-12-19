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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.wqter.androidclient_mypart.Adapter.HisAlarmAdapter;
import com.example.wqter.androidclient_mypart.Bean.HisAlarmBean;
import com.example.wqter.androidclient_mypart.HistoryAlarmActivity;
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

public class NowAlarmFragment extends android.support.v4.app.Fragment {

    private View mView;
    private RefreshLayout mRefreshLayout;
    //private ListView now_lv;
    private SwipeMenuListView now_lv;
    private Handler handler;
    private SQLiteDatabase db;
    private List<HisAlarmBean> dataList;
    private List<String> list_id;
    private ImageView nodata;
    private HisAlarmAdapter hisAdapter;
    private int glo_pos;
    private AlertDialog.Builder delete_builder,confirm_builder,shield_builder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化视图和数据
        mView = getActivity().getLayoutInflater().inflate(R.layout.fragment_nowalarm,null);
        db= DBhelper.getDbHelpter(getActivity()).getWritableDatabase();
        initView(mView);
    }

    private void initView(View view) {
        list_id=new ArrayList<>();
        handler=new Handler();
        now_lv=(SwipeMenuListView)view.findViewById(R.id.now_alarm_lv);
        SwipeMenuCreator creator=new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(getContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                deleteItem.setWidth(dp2px(90));
                deleteItem.setIcon(R.mipmap.item_delete);
//                deleteItem.setTitle("Delete");
//                deleteItem.setTitleSize(18);
//                deleteItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(deleteItem);

                SwipeMenuItem confirmItem = new SwipeMenuItem(
                        getContext());
                confirmItem.setBackground(new ColorDrawable(Color.rgb(0xF7,
                        0xFB, 0x21)));
                confirmItem.setWidth(dp2px(90));
                confirmItem.setTitle("确认");
                confirmItem.setTitleSize(18);
                confirmItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(confirmItem);
                SwipeMenuItem shieldItem = new SwipeMenuItem(
                        getContext());
                shieldItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                shieldItem.setWidth(dp2px(90));
                shieldItem.setTitle("屏蔽");
                shieldItem.setTitleSize(18);
                shieldItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(shieldItem);
            }
        };
        now_lv.setMenuCreator(creator);

        now_lv.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                glo_pos=position;
                switch (index){
                    case 0:
                        //handler.postDelayed(deleteTask, 200);
                       // delete_db_item(position);
                       // Log.v("first menu",String.valueOf(position));
                        if(delete_builder==null){
                            delete_builder= new AlertDialog.Builder(getActivity());
                            delete_builder.setTitle("提示")
                                .setMessage("是否确认删除该报警")
                                .setPositiveButton(getString(R.string.common_dialog_true), new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {

                                    delete_db_item(glo_pos);
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
                        delete_builder.show();
                        break;
                    case 1:
                        if(confirm_builder==null){
                            confirm_builder= new AlertDialog.Builder(getActivity());
                            confirm_builder.setTitle("提示")
                                    .setMessage("是否确认该报警")
                                    .setPositiveButton(getString(R.string.common_dialog_true), new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            confirm_alarm();
                                            delete_db_item(glo_pos);

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
                        confirm_builder.show();
                        //handler.postDelayed(deleteTask,200);
                        //Log.v("second menu",String.valueOf(position));
                        break;
                    case 2:
                        if(shield_builder==null){
                            shield_builder= new AlertDialog.Builder(getActivity());
                            shield_builder.setTitle("提示")
                                    .setMessage("是否屏蔽该报警")
                                    .setPositiveButton(getString(R.string.common_dialog_true), new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            shield_alarm();
                                            delete_db_item(glo_pos);
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
                        shield_builder.show();
                        //handler.postDelayed(deleteTask,200);
                        //Log.v("third menu",String.valueOf(position));
                        break;
                }
                //handler.postDelayed(deleteTask, 200);
                return false;
            }

        });
//        now_lv.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);
        now_lv.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

        dataList=new ArrayList<>();
        mRefreshLayout=(RefreshLayout)view.findViewById(R.id.now_alarm_refreshLayout);
        nodata=(ImageView)view.findViewById(R.id.now_nodata);
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

    private Runnable deleteTask = new Runnable() {
        @Override
        public void run() {
//            delete_db_item(glo_pos);
            String db_id=list_id.get(glo_pos);
            String sql="delete from table_allalarm where id='"+db_id+"'";
            db.execSQL(sql);
            list_id.remove(glo_pos);
            dataList.remove(glo_pos);
            hisAdapter.notifyDataSetChanged();

            handler.removeCallbacks(deleteTask);
        }

    };
    private void delete_db_item(int pos)
    {
        String db_id=list_id.get(pos);
        String sql="delete from table_allalarm where id='"+db_id+"'";
        db.execSQL(sql);
        list_id.remove(pos);
        dataList.remove(pos);
        hisAdapter.notifyDataSetChanged();
    }

    //确认报警
    private void confirm_alarm(){
        HisAlarmBean bean_confirm=dataList.get(glo_pos);
        String insert_sql="insert into table_confirmationalarm (id,devicetype,devicename,start_datetime,alarminfo,alarmtype,alarmreason,solvingmethod,ending_datetime) "
                +"values (null,'"+bean_confirm.device_type+"','"+bean_confirm.device_name+"','"+bean_confirm.start_time+"','"+bean_confirm.alarm_info+"','"+bean_confirm.alarm_type+"','"
                +bean_confirm.alarm_reason+"','"+bean_confirm.solve_method+"','"+bean_confirm.end_time+"')";
        db.execSQL(insert_sql);
    }
    private void shield_alarm(){
        HisAlarmBean bean_shield=dataList.get(glo_pos);
        String sql="insert into table_shieldalarm (id,devicename,alarminfo) values (null,'"+bean_shield.device_name+"','"+bean_shield.alarm_info+"')";
        db.execSQL(sql);
        Cursor cursor=db.rawQuery("select deviceCode,com from table_deviceinformation where deviceName ='"+bean_shield.device_name+"'",null);
        String device_code="";
        String com="";
        if(cursor.moveToFirst())
        {
            device_code=cursor.getString(cursor.getColumnIndex("deviceCode"));
            com=cursor.getString(cursor.getColumnIndex("com"));

        }
        cursor.close();
        String table_name="table_device_alarmcomment_ttyACM"+com+"_device"+device_code;
        String sql_update= "update "+table_name+" set state ='0' where alarm_name = '"+bean_shield.alarm_info+"' and devicename='"+bean_shield.device_name+"'";
        db.execSQL(sql_update);
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
        Cursor cursor = db.rawQuery("select * from table_allalarm where ending_datetime='持续报警' order by start_datetime desc", null);
        while (cursor.moveToNext()){
            String all_id=cursor.getString(cursor.getColumnIndex("id"));
            String device_type = cursor.getString(cursor.getColumnIndex("devicetype"));
            String device_name = cursor.getString(cursor.getColumnIndex("devicename"));
            String start_time = cursor.getString(cursor.getColumnIndex("start_datetime"));
            String end_time = cursor.getString(cursor.getColumnIndex("ending_datetime"));
            String alarm_info = cursor.getString(cursor.getColumnIndex("alarminfo"));
            String alarm_type = cursor.getString(cursor.getColumnIndex("alarmtype"));
            String alarm_reason = cursor.getString(cursor.getColumnIndex("alarmreason"));
            String solve_method = cursor.getString(cursor.getColumnIndex("solvingmethod"));
            list_id.add(all_id);
            dataList.add(new HisAlarmBean(device_type, device_name, start_time, end_time, alarm_info, alarm_type, alarm_reason, solve_method));
        }
        cursor.close();
        mRefreshLayout.finishRefresh();
        if(dataList.size()==0)//判断是否非空不要用光标，否则直接定位到1位置，用movetoNext找不到第一行！
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
        if(hisAdapter==null){
            hisAdapter=new HisAlarmAdapter(getActivity(),dataList);
            now_lv.setAdapter(hisAdapter);
        }
        else {
            hisAdapter.notifyDataSetChanged();
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
        //handler.removeCallbacks(timeTask);
        ViewGroup mGroup=(ViewGroup) mView.getParent();
        if(mGroup!=null){
            mGroup.removeView(mView);
        }
    }


    public NowAlarmFragment newInstance(String title){           //回调
        NowAlarmFragment fragment = new NowAlarmFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }
}
