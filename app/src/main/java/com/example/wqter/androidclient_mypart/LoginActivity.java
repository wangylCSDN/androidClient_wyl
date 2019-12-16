package com.example.wqter.androidclient_mypart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wqter.androidclient_mypart.Sqlite.DBhelper;
import com.unstoppable.submitbuttonview.SubmitButton;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText login_name,login_pwd;
    private CheckBox check_remember,check_auto;
    private SharedPreferences sp;
    private SubmitButton login_btn;
    private Button register_btn;
    private SQLiteDatabase db;
    private String First_flag="0";
    //private Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature( Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        db= DBhelper.getDbHelpter(this).getWritableDatabase();//数据库连接
        DBhelper.getDbHelpter(this).onCreate(db);//手动调用
        initView();
        check_first();

    }
    private void check_first(){

//        //用户表
//        String  userSQL = "create table if not exists user" +
//                "(id integer not null primary key autoincrement," +
//                "user_id varchar(20) not null,"+
//                "name varchar(20) not null," +
//                "password varchar(20) not null," +
//                "phone varchar(11) not null,"+
//                "add_datetime datetime not null,"+//用户状态在手机端无效
//                "authentication varchar(255) not null,"+//权限
//                "last_login_time datetime not null)";
//        db.execSQL(userSQL);
//        //系统参数
//        String sysSQL ="create table if not exists system_para "+
//                "(id integer not null primary key autoincrement,"+
//                "window_title text not null,"+//窗口显示的标题
//                "deleteDay varchar(10) not null,"+//数据库保留时间
//                "delayAlarmTime varchar(10) not null,"+
//                "copyright text)";
//        db.execSQL(sysSQL);

        Cursor cursor = db.rawQuery("select * from user", null);
        if(!cursor.moveToFirst()){//判断首行是否存在,这边用if!
            //无账户，需要注册，并且为root账户
            First_flag="1";
            //register_btn.setPressed(true);//这个只是按下，并没有释放！搭配false才完成点击动作！
            register_btn.performClick();
        }
        cursor.close();

    }
    private void initView(){
        login_name=(EditText)findViewById(R.id.login_name);
        login_pwd=(EditText)findViewById(R.id.login_pwd);
        check_remember=(CheckBox)findViewById(R.id.remember);
        check_auto=(CheckBox)findViewById(R.id.auto_login);
        login_btn=(SubmitButton)findViewById(R.id.login_bt_sb);
        register_btn=(Button)findViewById(R.id.register_button);
        login_btn.setOnClickListener(this);
        register_btn.setOnClickListener(this);


        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);//获取保存信息
        if(sp.getBoolean("isAuto",false))//key值找不到返回false
        {
           AutoLogin();
        }
        if(sp.getBoolean("isRem",false))
        {
            RemMess();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_bt_sb:
                login();
                break;
            case R.id.register_button:
                intent_register();
                break;
        }

    }

    private void login(){
        String name=login_name.getText().toString();
        String pwd=login_pwd.getText().toString();
        int flag_name=0;
        if(login_name.getText().toString().length()!=0&&login_pwd.getText().toString().length()!=0)//非空
        {
            Cursor cursor=db.rawQuery("select * from user", null);
            while (cursor.moveToNext()){
                String name_db=cursor.getString(cursor.getColumnIndex("name"));
                String pwd_db=cursor.getString(cursor.getColumnIndex("password"));
                String user_id=cursor.getString(cursor.getColumnIndex("user_id"));//这个还不知道是否有用，预留
                if(name_db.equals(name)||user_id.equals(name)){
                    flag_name=1;
                    if(pwd_db.equals(pwd)){
                        String id=cursor.getString(cursor.getColumnIndex("id"));
                        String authentication=cursor.getString(cursor.getColumnIndex("authentication"));
                        login_intent(name_db,pwd_db,id,user_id,authentication);
                        cursor.close();
                        return;//不return会卡
                    }
                    else {
                        Toast.makeText(LoginActivity.this,"密码输入错误",Toast.LENGTH_SHORT).show();
                        login_btn.doResult(false);//回调错误的图案
                        login_btn.reset();
                        cursor.close();
                        return;
                    }
                }
            }
            cursor.close();
            if(flag_name==0){

                Toast.makeText(LoginActivity.this,"无该用户,请注册游客用户",Toast.LENGTH_SHORT).show();
                login_btn.doResult(false);//回调错误的图案
                login_btn.reset();
            }
        }
        else {
            Toast.makeText(LoginActivity.this,"用户名或者密码输入不能为空",Toast.LENGTH_SHORT).show();
            login_btn.reset();
        }
    }
    private void login_intent(String name,String pwd,String id,String user_id,String authentication)
    {

        login_btn.doResult(true);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("user_id",user_id);
        editor.putString("user_name",name);
        editor.putString("user_pwd",pwd);
        String level="";
        if(authentication.equals("root"))
        {
            level="1";
        }
        if(authentication.equals("系统管理员"))
        {
            level="2";
        }
        if(authentication.equals("软件管理员"))
        {
            level="3";
        }
        if(authentication.equals("游客"))
        {
            level="4";
        }
        editor.putString("level",level);
        editor.apply();
        //app的title
        if(!sp.contains("title")){
            Cursor cursor=db.rawQuery("select * from system_para", null);
            while(cursor.moveToNext()){//这个数据库里面也只有1条
                String title=cursor.getString(cursor.getColumnIndex("window_title"));
                String copyright=cursor.getString(cursor.getColumnIndex("copyright"));
                editor.putString("title",title);
                editor.putString("copyright",copyright);
                editor.apply();
            }
            cursor.close();
        }
        //更新登陆时间
        String sql="update user set last_login_time=datetime('now','localtime') where id="+id;
        db.execSQL(sql);
        if(check_remember.isChecked()){
            editor.putBoolean("isRem",true);
        }
        else {
            editor.putBoolean("isRem",false);
        }
        if(check_auto.isChecked()){
            editor.putBoolean("isAuto",true);
        }
        else {
            editor.putBoolean("isAuto",false);
        }
        editor.apply();


        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();




        //TODO 需不需要进行判断，权限不够直接跳转到主界面
        //或者全部跳转，在设备确认界面里面低权限的无法操作！类似大动环。
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this,HistoryAlarmActivity.class);
        startActivity(intent);
        finish();

    }
    private void RemMess(){
        String user_name=sp.getString("user_name","");//这边用id还是name,笑哭
        login_name.setText(user_name);
        String password=sp.getString("user_pwd","");
        login_pwd.setText(password);
        check_remember.setChecked(true);
        Toast.makeText(LoginActivity.this,"保存的是上次登录的用户名",Toast.LENGTH_SHORT).show();
    }

    private void AutoLogin(){
        //直接跳转
        Intent intent=new Intent();
        intent.setClass(LoginActivity.this,HistoryAlarmActivity.class);
        startActivity(intent);
        finish();

    }
    private void intent_register(){
        Intent intent=new Intent();
        intent.setClass(LoginActivity.this,RegisterActivity.class);//register
        intent.putExtra("First_flag",First_flag);
        startActivity(intent);
        //don't finish
    }



}
