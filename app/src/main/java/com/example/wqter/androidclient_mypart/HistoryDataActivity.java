package com.example.wqter.androidclient_mypart;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.example.wqter.androidclient_mypart.Adapter.PerfectTitleAdapter;
import com.example.wqter.androidclient_mypart.ListTable.UDLRSlideListView;
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
 * Created by wqter on 2019/12/10.
 */

public class HistoryDataActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar his_toolbar;
    private Handler handler;
    private Spinner machine_spn;
    private EditText select_para;
    private ImageButton select_para_btn;
    private SQLiteDatabase db;
    private List<List<String>> data_all;
    private ArrayAdapter<String> adapter_machine;
    private List<String>device_name,device_com,device_code,data_para_ch,data_para_en;
     private List<Integer> select_para_pos;
    private List<List<String>>all_data_comment_en,all_data_comment_ch;

    private String select_machine;
    private int select_machine_pos;
    private TextView startDate_tv,endDate_tv;
    private TimePickerView start_timePicker,end_timePicker;
    private ZLoadingDialog LoadingDialog;

    private RelativeLayout nodata;
    private Button select_btn,start_select_btn,end_select_btn;
    private AlertDialog para_select_ad;
    private AlertDialog.Builder para_select_ad_builder;
    private ListView para_alert_lv;


    private UDLRSlideListView mListView;
    private List<String> title;
    private List<List<String>>content;
    private PerfectTitleAdapter PerfectTitleAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historydata);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        db= DBhelper.getDbHelpter(this).getWritableDatabase();//数据库连接
        initView();

    }
    private void initView(){
        his_toolbar=(Toolbar)findViewById(R.id.history_data_toolbar);
        his_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        nodata  =(RelativeLayout)findViewById(R.id.nodata_data);


        //表格显示的初始化
        mListView=(UDLRSlideListView)findViewById(R.id.data_table);
        title=new ArrayList<>();
        content=new ArrayList<>();

        startDate_tv=(TextView)findViewById(R.id.data_startDate_tv);
        endDate_tv=(TextView)findViewById(R.id.data_endDate_tv);

        start_select_btn=(Button)findViewById(R.id.data_start_select_btn);//开始时间
        end_select_btn=(Button)findViewById(R.id.data_end_select_btn);
        select_btn=(Button)findViewById(R.id.data_search); //查询按钮
        select_para_btn=(ImageButton) findViewById(R.id.select_para_btn);//参数选择按钮
        select_para=(EditText)findViewById(R.id.select_para_edit);//参数选择显示框

        start_select_btn.setOnClickListener(this);
        end_select_btn.setOnClickListener(this);
        select_para_btn.setOnClickListener(this);
        select_btn.setOnClickListener(this);

        machine_spn=(Spinner)findViewById(R.id.data_machine_spn);


        handler=new Handler();
        data_para_ch=new ArrayList<>();
        data_para_en=new ArrayList<>();
        select_para_pos=new ArrayList<>();

        device_name=new ArrayList<>();
        device_code=new ArrayList<>();
        device_com=new ArrayList<>();
        all_data_comment_en=new ArrayList<>();
        all_data_comment_ch=new ArrayList<>();
        initSpinnerData();
        initDate();
        initSpinner();
    }

    private void initSpinnerData(){
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

        //获取所有设备的data_en和data_ch的list
//        List<String> tem_list_en=new ArrayList<>();
//        List<String> tem_list_ch=new ArrayList<>();
        for(int i=0;i<device_name.size();i++)
        {
            //有点问题，是需要判断是否不为零才正确么
//            tem_list_ch.clear();//这个局部有点问题！，会把那个后边的
//            tem_list_en.clear();
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


    private void initSpinner(){
        machine_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//select
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                select_para.setText("");
                select_machine=(String)machine_spn.getSelectedItem();

                select_machine_pos=machine_spn.getSelectedItemPosition();//应该直接能等于position
                List<String> pop_para_list_ch=new ArrayList<>();
                List<String> pop_para_list_en=new ArrayList<>();
                pop_para_list_ch=all_data_comment_ch.get(select_machine_pos);
                pop_para_list_en=all_data_comment_en.get(select_machine_pos);

                initMultiSelect_machine(pop_para_list_ch,pop_para_list_en);
                //下拉框的字体相关的东西设置
                TextView tv = (TextView) view;
                tv.setTextSize(15);   //设置spinner hint 的字体
                tv.setGravity(Gravity.CENTER);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initMultiSelect_machine(final List<String> para_ch, List<String> para_en){
            if(data_para_ch.size()!=0)
            {
                data_para_ch.clear();
            }
            if(data_para_en.size()!=0){
                data_para_en.clear();
            }
            for(int j=0;j<para_ch.size();j++){
                data_para_en.add(para_en.get(j));
                data_para_ch.add(para_ch.get(j));
            }

        boolean[] para_isChecked=new boolean[data_para_ch.size()];
        String[] data_para_s=para_ch.toArray(new String[data_para_ch.size()]);//转成字符串数组
        if(para_select_ad==null)
        {
            para_select_ad_builder=new AlertDialog.Builder(this);
            para_select_ad_builder
                    .setTitle("请选择查询参数")
                    .setMultiChoiceItems(data_para_s, para_isChecked, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                        }
                    })
                    .setPositiveButton(R.string.common_dialog_true, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(select_para_pos.size()!=0){
                                select_para_pos.clear();
                            }
                            String s="";
                            for (int i=0;i<data_para_ch.size();i++){
                               if(para_alert_lv.getCheckedItemPositions().get(i))//复数
                               {
                                   s+=para_alert_lv.getAdapter().getItem(i)+"，"; //拼接成string
                                   select_para_pos.add(i);
                               }
                            }
                            if(s.length()==0){
                                Toast.makeText(HistoryDataActivity.this, "未选择参数", Toast.LENGTH_SHORT).show();
                                select_para.setText("");
                            }
                            else {
                                s=s.substring(0, s.length()-1);  //去最后的逗号
                                select_para.setText(s);//将选值放到EditText上显示
                            }
                        }
                    })
                    .setNegativeButton(R.string.common_dialog_false,null);
            para_select_ad=para_select_ad_builder.create();
            para_alert_lv=para_select_ad.getListView();
        }
        else
        {
            para_select_ad_builder
                    .setMultiChoiceItems(data_para_s, para_isChecked, new DialogInterface.OnMultiChoiceClickListener() //只需更新数据
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        }
                    });
            para_select_ad=para_select_ad_builder.create();
            para_alert_lv=para_select_ad.getListView();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.data_start_select_btn:
                if(start_timePicker!=null){
                    start_timePicker.show();
                }
                break;
            case R.id.data_end_select_btn:
                if(end_timePicker!=null){
                    end_timePicker.show();
                }
                break;
            case R.id.data_search:
                SimpleDateFormat data_c=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
                try {
                    Date data_start=data_c.parse(startDate_tv.getText().toString());
                    Date data_end=data_c.parse(endDate_tv.getText().toString());
                    if(data_start.getTime()>=data_end.getTime()){
                        Toast.makeText(HistoryDataActivity.this,"起始时间不能大于结束时间", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        //有问题
                        if(content.size()!=0){
                        content.clear();
                       }
                        if(title.size()!=0){
                            title.clear();
                        }
                        setList();
                        showList();

                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.select_para_btn:
                if(para_select_ad!=null){
                    para_select_ad.show();
                }
                break;
        }
    }

    private void setList() {//会不会是这边的问题

        if(PerfectTitleAdapter==null){
            PerfectTitleAdapter=new PerfectTitleAdapter(HistoryDataActivity.this,content);
            mListView.setAdapter(PerfectTitleAdapter);
        }
        else {
            Log.v("adapter",content.toString());
            //PerfectTitleAdapter.setData(content);

            //TODO 存在bug，多个参数变一个参数，adapter刷新时产生问题
           // PerfectTitleAdapter.setData(content);
            PerfectTitleAdapter.notifyDataSetChanged();
            //PerfectTitleAdapter.notifyDataSetChanged();//感觉是这个函数的错误

        }
//        PerfectTitleAdapter madapter=new PerfectTitleAdapter(this,content);
//        mListView.setAdapter(madapter);

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
            else {
                LoadingDialog.show();
            }
           // handledata();
            handler.postDelayed(SearchTask,1000);//
        }
    }

    private Runnable SearchTask=new Runnable() {
        @Override
        public void run() {
           // int Flag=0;
            if(content.size()!=0)
            {
                content.clear();   //说是clear是有关联的
            }
            if(title.size()!=0){
                title.clear();
            }
//            List<List<String>> content =new ArrayList<>();
//            List<String>title =new ArrayList<>();

//            Log.v("content_empty",content.toString());
//            Log.v("title",title.toString());

            String sql="select * from table_system_historydata_ttyACM"+ device_com.get(select_machine_pos) +"_device"+device_code.get(select_machine_pos)
                    +" where datetime between '"+startDate_tv.getText().toString()+"' and '"+endDate_tv.getText().toString()+"'";
            Cursor cursor=db.rawQuery(sql ,null);
            if(!cursor.moveToFirst())
            {
                cursor.close();
                nodata.setVisibility(View.VISIBLE);
            }
            else {
                nodata.setVisibility(View.GONE);
                if (select_para.getText().toString().length() == 0){
                    title.add("更新时间");
                    for (int i = 0; i < all_data_comment_ch.get(select_machine_pos).size(); i++) {
                        title.add(all_data_comment_ch.get(select_machine_pos).get(i));//表头添加
                    }
                    content.add(title);

                    while (cursor.moveToNext()) {
                        List<String> content_row = new ArrayList<>();
                        String date = cursor.getString(cursor.getColumnIndex("datetime"));
                        content_row.add(date);
                        for (int j = 0; j < all_data_comment_en.get(select_machine_pos).size(); j++) {
                            String temp = cursor.getString(cursor.getColumnIndex(all_data_comment_en.get(select_machine_pos).get(j)));
                            content_row.add(temp);
                        }
                        //Log.v("content_row",content_row.toString());
                        content.add(content_row);
                    }
                   // Log.v("a",content.toString());
                } else
                    {
                    title.add("更新时间");
                    for (int i = 0; i < select_para_pos.size(); i++) {
                        title.add(all_data_comment_ch.get(select_machine_pos).get(select_para_pos.get(i)));
                    }
                    content.add(title);
                    while (cursor.moveToNext()) {
                        List<String> content_row1 = new ArrayList<>();
                        String date = cursor.getString(cursor.getColumnIndex("datetime"));
                        content_row1.add(date);
                        for (int j = 0; j < select_para_pos.size(); j++) {
                            String temp = cursor.getString(cursor.getColumnIndex(all_data_comment_en.get(select_machine_pos).get(select_para_pos.get(j))));
                            content_row1.add(temp);
                        }
                       // Log.v("content_row1",content_row1.toString());
                        content.add(content_row1);
                    }
                  //  Log.v("b",content.toString());//数组没问题的！
                }
                cursor.close();
            }
            LoadingDialog.dismiss();
            setList();
            handler.removeCallbacks(SearchTask);
        }
    };

    @Override
    protected void onDestroy() {
        setContentView(R.layout.view_null);
        super.onDestroy();
    }
}
