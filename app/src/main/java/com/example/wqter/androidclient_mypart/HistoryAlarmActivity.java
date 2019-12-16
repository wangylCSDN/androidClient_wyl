package com.example.wqter.androidclient_mypart;

import android.app.Activity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.example.wqter.androidclient_mypart.Adapter.HisAlarmAdapter;
import com.example.wqter.androidclient_mypart.Bean.HisAlarmBean;
import com.example.wqter.androidclient_mypart.Sqlite.DBhelper;
import com.zyao89.view.zloading.ZLoadingDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.zyao89.view.zloading.Z_TYPE.CIRCLE;

/**
 * Created by wqter on 2019/12/14.
 */

public class HistoryAlarmActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar his_toolbar;
    private Handler handler;
    private Spinner machine_spn;
    private EditText select_alarm;
    private ImageButton select_alarm_btn;
    private SQLiteDatabase db;
    private ArrayAdapter<String> adapter_machine;

    private List<String> device_name,device_com,device_code,pop_comment;
    private List<Integer> select_alarm_pos;
    private List<List<String>>all_alarm_comment_name,all_alarm_comment_en;
    private String select_machine;
    private int select_machine_pos;
    private TextView startDate_tv,endDate_tv;
    private TimePickerView start_timePicker,end_timePicker;
    private ZLoadingDialog LoadingDialog;
    private RelativeLayout nodata;
    private Button select_btn,start_select_btn,end_select_btn;

    private ListView alarm_alert_lv,his_alarm_lv;
    private AlertDialog alarm_select_ad;
    private AlertDialog.Builder alarm_select_ad_builder;

    private List<HisAlarmBean> dataList;
    private HisAlarmAdapter hisAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historyalarm);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        db= DBhelper.getDbHelpter(this).getWritableDatabase();//数据库连接
        initView();

    }

    private void initView() {
        his_toolbar=(Toolbar)findViewById(R.id.history_alarm_toolbar);
        his_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        nodata=(RelativeLayout)findViewById(R.id.nodata_alarm);
        handler=new Handler();
        machine_spn=(Spinner)findViewById(R.id.alarm_machine_spn);
        select_alarm=(EditText)findViewById(R.id.select_alarm_para_edit);
        select_alarm_btn=(ImageButton)findViewById(R.id.select_alarm_para_btn);
        select_alarm_btn.setOnClickListener(this);

        device_name=new ArrayList<>();
        device_com=new ArrayList<>();
        device_code=new ArrayList<>();
        select_alarm_pos=new ArrayList<>();
        all_alarm_comment_name=new ArrayList<>();
        all_alarm_comment_en=new ArrayList<>();
        pop_comment=new ArrayList<>();
        dataList=new ArrayList<>();

        startDate_tv=(TextView)findViewById(R.id.alarm_startDate_tv);
        endDate_tv=(TextView)findViewById(R.id.alarm_endDate_tv);
        start_select_btn=(Button)findViewById(R.id.alarm_start_select_btn);
        end_select_btn=(Button)findViewById(R.id.alarm_end_select_btn);
        select_btn=(Button)findViewById(R.id.alarm_search);

        start_select_btn.setOnClickListener(this);
        end_select_btn.setOnClickListener(this);
        select_btn.setOnClickListener(this);

        his_alarm_lv=(ListView)findViewById(R.id.his_alarm_lv);
        initSpinnerData();
        initDate();
        initSpinner();
    }

    private void initSpinnerData() {
        Cursor cursor = db.rawQuery("select * from table_deviceinformation", null);
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
            String table_name="table_device_alarmcomment_ttyACM"+device_com.get(i)+"_device"+device_code.get(i);
            Cursor cursor1=db.rawQuery("select * from "+table_name, null);
            while (cursor1.moveToNext()){
                String tem_en=cursor1.getString(cursor1.getColumnIndex("name_en"));
                String tem_ch=cursor1.getString(cursor1.getColumnIndex("alarm_name"));
                tem_list_en.add(tem_en);
                tem_list_ch.add(tem_ch);
            }
            all_alarm_comment_name.add(tem_list_ch);//所有设备的alarm_comment_name,按顺序进行排列
            all_alarm_comment_en.add(tem_list_en);
            //报警原因什么的不管，那个直接查all_alarm表里面有的
            cursor1.close();
        }

        if(adapter_machine==null){
            adapter_machine=new ArrayAdapter<String>(this,R.layout.view_spinner_item,device_name);
            adapter_machine.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);//自带
            machine_spn.setAdapter(adapter_machine);
        }
        else {
            adapter_machine.notifyDataSetChanged();//这个应该不会更新
        }
    }


    private void initDate(){
        SimpleDateFormat today=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        Date dNow = new Date();   //当前时间
        Date dBefore;

        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(dNow);//把当前时间赋给日历
        calendar.add(Calendar.DAY_OF_MONTH, -1);  //设置为前3天,此时calendar为1天前的时间
        dBefore = calendar.getTime();   //得到前一天的时间

        startDate_tv.setText(today.format(dBefore)); //开始日期editText赋值
        endDate_tv.setText(today.format(dNow));      //结束日期editText赋值

        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2018, 1, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2020, 12, 30);
        start_timePicker = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                startDate_tv.setText(getTime(date));
            }
        })
                //年月日时分秒 的显示与否，不设置则默认全部显示
                .setType(new boolean[]{true, true, true, true, true, true})
                .setDate(calendar)
                .setRangDate(startDate, endDate)
                .setOutSideCancelable(false)
                .setCancelText("取消")
                .setSubmitText("确定")
                .build();

        end_timePicker = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                endDate_tv.setText(getTime(date));
            }
        })
                //年月日时分秒 的显示与否，不设置则默认全部显示
                .setType(new boolean[]{true, true, true, true, true, true})
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setOutSideCancelable(false)
                .setCancelText("取消")
                .setSubmitText("确定")
                .build();
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());//原来那边是dd!!!!卧槽
        return time.format(date);
    }


    private void initSpinner() {
        machine_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                select_alarm.setText("");
                select_machine=(String)machine_spn.getSelectedItem();
                select_machine_pos=machine_spn.getSelectedItemPosition();
                List<String> pop_para_list=new ArrayList<>();
                pop_para_list=all_alarm_comment_name.get(select_machine_pos);
                initMultiSelect_machine(pop_para_list);

                TextView tv = (TextView) view;
                tv.setTextSize(15);   //设置spinner hint 的字体
                tv.setGravity(Gravity.CENTER);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private void initMultiSelect_machine(List<String> pop_name){

        if(pop_comment.size()!=0){
            pop_comment.clear();
        }
        for(int j=0;j<pop_name.size();j++){
            pop_comment.add(pop_name.get(j));
        }
        Log.v("popsize",pop_comment.toString());
        boolean[] para_isChecked=new boolean[pop_comment.size()];
        String[] alarm_para_s=pop_name.toArray(new String[pop_comment.size()]);

        if(alarm_select_ad==null)
        {
            alarm_select_ad_builder=new AlertDialog.Builder(this);
            alarm_select_ad_builder
                    .setTitle("请选择查询报警参数")
                    .setMultiChoiceItems(alarm_para_s, para_isChecked, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                        }
                    })
                    .setPositiveButton(R.string.common_dialog_true, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(select_alarm_pos.size()!=0){
                                select_alarm_pos.clear();
                            }
                            String s="";
                            for (int i=0;i<pop_comment.size();i++){
                                if(alarm_alert_lv.getCheckedItemPositions().get(i))//复数
                                {
                                    s+=alarm_alert_lv.getAdapter().getItem(i)+"，"; //拼接成string
                                    select_alarm_pos.add(i);
                                }
                            }
                            if(s.length()==0){
                                Toast.makeText(HistoryAlarmActivity.this, "未选择参数", Toast.LENGTH_SHORT).show();
                                select_alarm.setText("");
                            }
                            else {
                                s=s.substring(0, s.length()-1);  //去最后的逗号
                                select_alarm.setText(s);//将选值放到EditText上显示
                            }
                        }
                    })
                    .setNegativeButton(R.string.common_dialog_false,null);
            alarm_select_ad=alarm_select_ad_builder.create();
            alarm_alert_lv=alarm_select_ad.getListView();
        }
        else
        {
            alarm_select_ad_builder
                    .setMultiChoiceItems(alarm_para_s, para_isChecked, new DialogInterface.OnMultiChoiceClickListener() //只需更新数据
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        }
                    });
            alarm_select_ad=alarm_select_ad_builder.create();
            alarm_alert_lv=alarm_select_ad.getListView();
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.alarm_start_select_btn:
                if(start_timePicker!=null){
                    start_timePicker.show();
                }
                break;
            case R.id.alarm_end_select_btn:
                if(end_timePicker!=null){
                    end_timePicker.show();
                }
                break;
            case R.id.alarm_search:
                SimpleDateFormat data_c=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
                try {
                    Date data_start=data_c.parse(startDate_tv.getText().toString());
                    Date data_end=data_c.parse(endDate_tv.getText().toString());
                    if(data_start.getTime()>=data_end.getTime()){
                        Toast.makeText(HistoryAlarmActivity.this,"起始时间不能大于结束时间", Toast.LENGTH_SHORT).show();
                    }
                    else {
                            if(dataList!=null){
                                dataList.clear();
                            }
                            //要先set，不然第一次点击的时候是空的
                            setList();
                            hisAdapter.notifyDataSetChanged();
                            showList();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.select_alarm_para_btn:
                if(alarm_select_ad!=null){
                    alarm_select_ad.show();
                }
                break;
        }

    }

    private void showList() {
        if(select_machine!=null){
            if(LoadingDialog==null){
                LoadingDialog=new ZLoadingDialog(this);
                LoadingDialog.setLoadingBuilder(CIRCLE)//设置类型
                        .setLoadingColor(Color.BLACK)//颜色
                        .setHintText("Loading....")
                        .setHintTextSize(12) // 设置字体大小 dp
                        .setHintTextColor(Color.GRAY)  // 设置字体颜色
                        .setCanceledOnTouchOutside(false) //设置不能点外面
                        .show();
            }
            else
            {
                LoadingDialog.show();
            }
            handler.postDelayed(SearchTask,1000);
        }

    }
    private Runnable SearchTask= new Runnable(){
        @Override
        public void run() {
            if(dataList.size()!=0){
                dataList.clear();
                dataList.clear();
            }
            String sql;
            if(select_alarm.getText().toString().length()==0){
                 sql="select * from (select * from table_allalarm  where devicename= '"+select_machine+"' and start_datetime between '"+startDate_tv.getText().toString()
                        +"' and '"+endDate_tv.getText().toString()+"' union all select * from table_confirmationalarm where devicename= '"+select_machine+"' and start_datetime between '"
                        +startDate_tv.getText().toString()+"' and '"+endDate_tv.getText().toString()+"') order by start_datetime asc";
                //union all子查询
            }
            else {
                String where="";
                for (int i=0;i<select_alarm_pos.size();i++)
                {
                    if(i==0){
                        where=where+"alarminfo = '"+ all_alarm_comment_name.get(select_machine_pos).get(select_alarm_pos.get(i))+"' ";
                    }
                    else
                    {
                        where=where+"or alarminfo = '"+ all_alarm_comment_name.get(select_machine_pos).get(select_alarm_pos.get(i))+"' ";
                    }
                }
                 sql="select * from (select * from table_allalarm  where devicename= '"+select_machine+"' and start_datetime between '"+startDate_tv.getText().toString()
                        +"' and '"+endDate_tv.getText().toString()+"' and ("+where +") union all select * from table_confirmationalarm where devicename= '"+select_machine+"' and start_datetime between '"
                        +startDate_tv.getText().toString()+"' and '"+endDate_tv.getText().toString()+"' and ("+where+")) order by start_datetime asc";
            }

            Cursor cursor=db.rawQuery(sql,null);
            if(!cursor.moveToFirst())
            {
                cursor.close();
                nodata.setVisibility(View.VISIBLE);
            }
            else {
                nodata.setVisibility(View.GONE);
                while (cursor.moveToNext()) {
                    String device_type = cursor.getString(cursor.getColumnIndex("devicetype"));
                    String device_name = cursor.getString(cursor.getColumnIndex("devicename"));
                    String start_time = cursor.getString(cursor.getColumnIndex("start_datetime"));
                    String end_time = cursor.getString(cursor.getColumnIndex("ending_datetime"));
                    String alarm_info = cursor.getString(cursor.getColumnIndex("alarminfo"));
                    String alarm_type = cursor.getString(cursor.getColumnIndex("alarmtype"));
                    String alarm_reason = cursor.getString(cursor.getColumnIndex("alarmreason"));
                    String solve_method = cursor.getString(cursor.getColumnIndex("solvingmethod"));
                    dataList.add(new HisAlarmBean(device_type, device_name, start_time, end_time, alarm_info, alarm_type, alarm_reason, solve_method));
                }
                cursor.close();
            }
            LoadingDialog.dismiss();
            setList();
            handler.removeCallbacks(SearchTask);
        }
    };


    //刷新Adapter数据
    private void setList() {
        if(hisAdapter==null){
            hisAdapter=new HisAlarmAdapter(HistoryAlarmActivity.this,dataList);
            his_alarm_lv.setAdapter(hisAdapter);
        }
        else {
            hisAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        setContentView(R.layout.view_null);
        super.onDestroy();
    }
}
