package com.example.wqter.androidclient_mypart.Sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by wqter on 2019/12/5.
 */

public class base {
    private static base base=null;
    private DBhelper myBase;
    private base(Context context){
        this.myBase= DBhelper.getDbHelpter(context);//在那边改了
    }
    public static synchronized base setBase(Context context){
        if (base==null){
            base=new base(context);
        }
        return base;
    }
    public DBhelper getmyHelper(){
        return this.myBase;
    }
    public SQLiteDatabase getWrite(){
        return myBase.getWritableDatabase();
    }
    public SQLiteDatabase getread(){
        return myBase.getReadableDatabase();
    }
}
