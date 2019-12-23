package com.example.wqter.androidclient_mypart.Sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wqter on 2019/9/29.
 */

public class DBhelper extends SQLiteOpenHelper {

    public static final String TAG = "DBHelper";
    private  static final String NAME = "pem_db.db";
    //TODO 是否需要使用两个数据库？一个永久不变，一个软件升级的时候替换掉？
    private  static final int VERSION = 1;
    private  static DBhelper dbHelpter;
    private static SQLiteDatabase INSTANCE;
    private static Context mcontext;

    //需要改变，这个不同的activity因为他是单例的

    public static synchronized DBhelper getDbHelpter(Context context){
        //有bug，不同activity，如果上一个activity没有finish掉的话，下一个activity的context改变了，但是helpter还是上一个的！
        if (dbHelpter == null){
            dbHelpter = new DBhelper(new DBContext(context));//不用再传dbcontext了
        }
        return dbHelpter;
    }

    private DBhelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    //首次创建数据库时调用,一般进行建库建表操作
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //要手动进行调用
        //用户表
        String userSQL = "create table if not exists user" +
                "(id integer not null primary key autoincrement," +
                "user_id varchar(20) not null,"+
                "name varchar(20) not null," +
                "password varchar(20) not null," +
                "phone varchar(11) not null,"+
                "add_datetime datetime not null,"+//用户状态在手机端无效
                "authentication varchar(255) not null,"+//权限
                "last_login_time datetime not null)";
        sqLiteDatabase.execSQL(userSQL);
        //TODO 报警推送表，微信推送
        //TODO 历史登陆操作记录表
        //TODO 许可证表
        //TODO 所有设备的列表，选型参数列表
        //系统参数
        String sysSQL ="create table if not exists system_para "+
                "(id integer not null primary key autoincrement,"+
                "window_title text not null,"+//窗口显示的标题
                "deleteDay varchar(10) not null,"+//数据库保留时间
                "delayAlarmTime varchar(10) not null,"+
                "copyright text)";
        sqLiteDatabase.execSQL(sysSQL);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
