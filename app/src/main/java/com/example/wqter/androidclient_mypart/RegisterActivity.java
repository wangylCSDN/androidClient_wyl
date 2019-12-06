package com.example.wqter.androidclient_mypart;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.wqter.androidclient_mypart.Sqlite.DBhelper;

/**
 * Created by wqter on 2019/12/6.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText edit_tel,edit_id,edit_name,edit_pwd,edit_pwd_confirm,edit_title,edit_db_day,edit_alarm_time;
    private Button btn_ok,btn_cancel;
    private LinearLayout lin_title,lin_day;
    private String First_flag;
    private SQLiteDatabase db;
    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature( Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        initView();

    }
    private void initView(){
        db= DBhelper.getDbHelpter(this).getWritableDatabase();//数据库连接
        First_flag=getIntent().getStringExtra("First_flag");
        edit_tel=(EditText)findViewById(R.id.register_tel);
        edit_id=(EditText)findViewById(R.id.register_id);
        edit_name=(EditText)findViewById(R.id.register_name);
        edit_pwd=(EditText)findViewById(R.id.register_pwd);
        edit_pwd_confirm=(EditText)findViewById(R.id.register_pwd_confirm);
        edit_title=(EditText)findViewById(R.id.window_title);
        edit_db_day=(EditText)findViewById(R.id.edit_db_day);
        edit_alarm_time=(EditText)findViewById(R.id.edit_alarm_time);

        lin_title=(LinearLayout)findViewById(R.id.lin_title);
        lin_day=(LinearLayout)findViewById(R.id.lin_day);

        btn_ok=(Button)findViewById(R.id.register_ok_btn);
        btn_cancel=(Button)findViewById(R.id.register_cancel_btn);
        btn_ok.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        handler=new Handler();
        if(First_flag=="0"){
            lin_day.setVisibility(View.GONE);
            lin_title.setVisibility(View.GONE);
        }
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register_ok_btn:
                Register();
                break;
            case R.id.register_cancel_btn:
                this.finish();
                break;
        }
    }

    private void Register(){
        if(First_flag=="1")
        {
            String user_tel=edit_tel.getText().toString();
            String user_id=edit_id.getText().toString();
            String user_name=edit_name.getText().toString();
            String first_pwd=edit_pwd.getText().toString();
            String confirm_pwd=edit_pwd_confirm.getText().toString();
            String title=edit_title.getText().toString();
            String db_day=edit_db_day.getText().toString();
            String alarm_time=edit_alarm_time.getText().toString();

            if(user_tel.length()==0){
                Toast.makeText(RegisterActivity.this,"请输入电话号码",Toast.LENGTH_SHORT).show();
                return;
            }
             if(user_id.length()==0){
                 Toast.makeText(RegisterActivity.this,"请输入用户id",Toast.LENGTH_SHORT).show();
                 return;
            }
             if(user_name.length()==0){
                 Toast.makeText(RegisterActivity.this,"请输入用户名",Toast.LENGTH_SHORT).show();
                 return;
            }
            if(first_pwd.length()==0){
                Toast.makeText(RegisterActivity.this,"请输入用户密码",Toast.LENGTH_SHORT).show();
                return;
            }
             if(confirm_pwd.length()==0){
                 Toast.makeText(RegisterActivity.this,"请输入确认密码",Toast.LENGTH_SHORT).show();
                 return;
            }
            if(first_pwd.length()!=0&&confirm_pwd.length()!=0){
                if(first_pwd!=confirm_pwd){
                    Toast.makeText(RegisterActivity.this,"两次密码输入不同",Toast.LENGTH_SHORT).show();
                    return;
                }else if(first_pwd==confirm_pwd)
                {
                    if(title.length()==0){
                        title="杭州勤诚微电子科技有限公司";
                    }
                    if(db_day.length()==0){
                        db_day="30";//默认30天
                    }
                    if(alarm_time.length()==0){
                        alarm_time="600";//默认600秒
                    }

                   String sql="insert into user (id,user_id,name,password,phone,add_datetime,authentication,last_login_time) "+
                           "values(null,"+user_id+","+user_name+","+first_pwd+","+user_tel+",datetime('now','localtime'),"+
                           "'root','--'";
                    db.execSQL(sql);


                }
            }



        }
        else if(First_flag=="0")
        {
            String user_tel=edit_tel.getText().toString();
            String user_id=edit_id.getText().toString();
            String user_name=edit_name.getText().toString();
            String first_pwd=edit_pwd.getText().toString();
            String confirm_pwd=edit_pwd_confirm.getText().toString();


        }


    }
}
