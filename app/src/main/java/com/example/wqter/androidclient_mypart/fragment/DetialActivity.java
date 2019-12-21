package com.example.wqter.androidclient_mypart.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.wqter.androidclient_mypart.Adapter.RealTimeDataGvAdapter;
import com.example.wqter.androidclient_mypart.Adapter.RealtimeDataAdapter;
import com.example.wqter.androidclient_mypart.Bean.RealTimeBean;
import com.example.wqter.androidclient_mypart.R;
import com.example.wqter.androidclient_mypart.Sqlite.DBhelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wqter on 2019/12/20.
 */

public class DetialActivity extends AppCompatActivity implements View.OnClickListener {
    private Spinner ups_sp;
    private ListView lv_1,lv_2,lv_3,lv_4;
    private GridView gv_1;
    private SQLiteDatabase db;
    private List<String> device_name,device_com,device_code;
    private ArrayAdapter<String> adapter_machine;
    private List<List<String>>all_data_comment_en,all_data_comment_ch;
    private RealtimeDataAdapter adapter_lv_1,adapter_lv_2,adapter_lv_3,adapter_lv_4;
    private RealTimeDataGvAdapter adapter_gv;
    private Handler handler;
    private String select_machine;
    private int select_machine_pos;
    private List<RealTimeBean> lv_bean_1,lv_bean_2,lv_bean_3,lv_bean_4,gv_bean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature( Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detial_ups);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        db= DBhelper.getDbHelpter(this).getWritableDatabase();//数据库连接
        initView();
    }

    private void initView() {
        handler=new Handler();
        lv_bean_1=new ArrayList<>();
        lv_bean_2=new ArrayList<>();
        lv_bean_3=new ArrayList<>();
        lv_bean_4=new ArrayList<>();
        gv_bean=new ArrayList<>();
        ups_sp=(Spinner)findViewById(R.id.detial_ups_sp);
        lv_1=(ListView)findViewById(R.id.detial_ups_1);
        lv_2=(ListView)findViewById(R.id.detial_ups_2);
        lv_3=(ListView)findViewById(R.id.detial_ups_3);
        lv_4=(ListView)findViewById(R.id.detial_ups_4);
        gv_1=(GridView)findViewById(R.id.detial_ups_gv);
        all_data_comment_en=new ArrayList<>();
        all_data_comment_ch=new ArrayList<>();
        device_name=new ArrayList<>();
        device_com=new ArrayList<>();
        device_code=new ArrayList<>();
        initSpinnerData();
        initSpinner();
        handler.postDelayed(timeTask,5*1000);

    }

    private void initSpinnerData() {
        Cursor cursor = db.rawQuery("select * from table_deviceinformation where deviceType = '微模块'", null);
        while (cursor.moveToNext()){
            String devicename=cursor.getString(cursor.getColumnIndex("deviceName"));
            String devicecode=cursor.getString(cursor.getColumnIndex("deviceCode"));
            String devicecom=cursor.getString(cursor.getColumnIndex("com"));
            device_name.add(devicename);
            device_code.add(devicecode);
            device_com.add(devicecom);
        }
        cursor.close();
        for(int i=0;i<device_name.size();i++)
        {
            List<String> tem_list_en=new ArrayList<>();
            List<String> tem_list_ch=new ArrayList<>();
            String table_name="table_device_datacomment_ttyACM"+device_com.get(i)+"_device"+device_code.get(i);
            Cursor cursor1=db.rawQuery("select * from "+table_name, null);
            while (cursor1.moveToNext()){
                String tem_en=cursor1.getString(cursor1.getColumnIndex("name_en"));
                String tem_ch=cursor1.getString(cursor1.getColumnIndex("name_cn"));
                tem_list_en.add(tem_en);
                tem_list_ch.add(tem_ch);
            }
            all_data_comment_en.add(tem_list_en);
            all_data_comment_ch.add(tem_list_ch);
            cursor1.close();
        }
        if(adapter_machine==null){
            adapter_machine=new ArrayAdapter<String>(this,R.layout.view_spinner_item,device_name);
            adapter_machine.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);//自带
           ups_sp.setAdapter(adapter_machine);
        }
        else {
            adapter_machine.notifyDataSetChanged();//这个应该不会更新
        }
    }

    private Runnable timeTask = new Runnable() {
        @Override
        public void run() {
            setlvData();
            handler.postDelayed(timeTask,10*1000);
            // Log.v("refreshtime","1111");
        }
    };
    private void initSpinner() {
        ups_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                select_machine=(String)ups_sp.getSelectedItem();
                select_machine_pos=ups_sp.getSelectedItemPosition();
                setlvData();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

    }

    private void setlvData(){
        List<RealTimeBean> temp_all_data=new ArrayList<>();
        String sql="select * from table_device_realtimedata_ttyACM"+device_com.get(select_machine_pos)+"_device"+device_code.get(select_machine_pos);
        Cursor cursor=db.rawQuery(sql,null);
        if(cursor.moveToFirst())
        {
            for(int i=0;i<all_data_comment_en.get(select_machine_pos).size();i++)
            {
                 String data =cursor.getString(cursor.getColumnIndex(all_data_comment_en.get(select_machine_pos).get(i)));
                temp_all_data.add(new RealTimeBean(all_data_comment_ch.get(select_machine_pos).get(i),data));
            }
        }
        cursor.close();
//        List<RealTimeBean>lv_bean_1=new ArrayList<>();
//        List<RealTimeBean>lv_bean_2=new ArrayList<>();
//        List<RealTimeBean>lv_bean_3=new ArrayList<>();
//        List<RealTimeBean>lv_bean_4=new ArrayList<>();
        if(lv_bean_1.size()!=0)
        {
            lv_bean_1.clear();
        }
        if(lv_bean_2.size()!=0)
        {
            lv_bean_2.clear();
        }
        if(lv_bean_3.size()!=0)
        {
            lv_bean_3.clear();
        }
        if(lv_bean_4.size()!=0)
        {
            lv_bean_4.clear();
        }
        if(gv_bean.size()!=0)
        {
            gv_bean.clear();
        }
        lv_bean_1.add(temp_all_data.get(0));
        lv_bean_1.add(temp_all_data.get(1));
        lv_bean_1.add(temp_all_data.get(2));
        lv_bean_2.add(temp_all_data.get(3));
        lv_bean_2.add(temp_all_data.get(4));
        lv_bean_2.add(temp_all_data.get(5));
        lv_bean_3.add(temp_all_data.get(6));
        lv_bean_3.add(temp_all_data.get(7));
        lv_bean_3.add(temp_all_data.get(8));
        lv_bean_4.add(temp_all_data.get(9));
        lv_bean_4.add(temp_all_data.get(10));
        lv_bean_4.add(temp_all_data.get(11));

//        List<RealTimeBean> gv_bean=new ArrayList<>();
        for(int j=0;j<temp_all_data.size();j++)
        {
            switch (j){
                case 32:
                case 33:
                case 34:
                case 35:
                case 36:
                case 37:
                case 38:
                case 39:
                case 40:
                case 41:
                case 42:
                case 43:
                case 44:
                    gv_bean.add(temp_all_data.get(j));
                    break;
                default:
                    break;
            }
        }

        if(adapter_lv_1==null)
        {
            adapter_lv_1=new RealtimeDataAdapter(this,lv_bean_1);
            lv_1.setAdapter(adapter_lv_1);
        }
        else
        {
            adapter_lv_1.notifyDataSetChanged();
        }

        if(adapter_lv_2==null)
        {
            adapter_lv_2=new RealtimeDataAdapter(this,lv_bean_2);
            lv_2.setAdapter(adapter_lv_2);
        }
        else
        {
            adapter_lv_2.notifyDataSetChanged();
        }

        if(adapter_lv_3==null)
        {
            adapter_lv_3=new RealtimeDataAdapter(this,lv_bean_3);
            lv_3.setAdapter(adapter_lv_3);
        }
        else
        {
            adapter_lv_3.notifyDataSetChanged();
        }

        if(adapter_lv_4==null)
        {
            adapter_lv_4=new RealtimeDataAdapter(this,lv_bean_4);
            lv_4.setAdapter(adapter_lv_4);
        }
        else
        {
            adapter_lv_4.notifyDataSetChanged();
        }
        if(adapter_gv==null)
        {
            adapter_gv=new RealTimeDataGvAdapter(this,gv_bean);
            gv_1.setAdapter(adapter_gv);
        }
        else
        {
           adapter_gv.notifyDataSetChanged();
        }
    }


    @Override
    public void onClick(View view) {

    }
}
