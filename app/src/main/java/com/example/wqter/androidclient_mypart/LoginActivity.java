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
        check_first();
        initView();

    }
    private void check_first(){
        db= DBhelper.getDbHelpter(this).getWritableDatabase();//数据库连接
        Cursor cursor = db.rawQuery("select * from user", null);
        if(cursor.moveToFirst()==false){
            //无账户，需要注册，并且为root账户
            cursor.close();
            First_flag="1";
            register_btn.setPressed(true);//直接跳转注册界面
            //还要传一个标志位
        }else {
            cursor.close();
        }
        return;
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
                if(name_db==name){
                    flag_name=1;
                    if(pwd_db==pwd){
                        String id=cursor.getString(cursor.getColumnIndex("id"));
                        String user_id=cursor.getString(cursor.getColumnIndex("user_id"));//这个还不知道用处是啥
                        String authentication=cursor.getString(cursor.getColumnIndex("authentication"));
                        cursor.close();
                        login_intent(name_db,pwd_db,id,user_id,authentication);
                    }
                    else {
                        Toast.makeText(LoginActivity.this,"密码输入错误",Toast.LENGTH_SHORT).show();
                        login_btn.doResult(false);//回调错误的图案
                        login_btn.reset();
                    }
                }
                cursor.close();
            }
            if(flag_name==0){
                Toast.makeText(LoginActivity.this,"无该用户",Toast.LENGTH_SHORT).show();
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

        SharedPreferences.Editor editor = sp.edit();
        editor.putString("user_id",user_id);
        editor.putString("user_name",name);
        editor.putString("user_pwd",pwd);
        String level="";
        if(authentication=="root")
        {
            level="0";
        }
        if(authentication=="系统管理员")
        {
            level="1";
        }
        if(authentication=="软件管理员")
        {
            level="2";
        }
        if(authentication=="游客")
        {
            level="3";
        }
        editor.putString("level",level);
        editor.apply();
        //app的title
        if(!sp.contains("title")){
            Cursor cursor=db.rawQuery("select * from system_para", null);
            if(cursor.moveToNext()){
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
        login_btn.doResult(true);
        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();

    }
    private void RemMess(){
        String user_name=sp.getString("user_name","");
        login_name.setText(user_name);
        String password=sp.getString("user_pwd","");
        login_pwd.setText(password);
        check_remember.setChecked(true);
    }

    private void AutoLogin(){
        //直接跳转
        Intent intent=new Intent();

    }
    private void intent_register(){
        Intent intent=new Intent();
        intent.setClass(LoginActivity.this,RegisterActivity.class);//register
        intent.putExtra("First_flag",First_flag);
        startActivity(intent);
        //don't finish
    }



}
