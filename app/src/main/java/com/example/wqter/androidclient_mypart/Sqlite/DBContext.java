package com.example.wqter.androidclient_mypart.Sqlite;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;

/**
 * Created by wqter on 2019/9/29.
 */

public class DBContext extends ContextWrapper {
    public static final String TAG = "DBContext";
    private Context mContext;
    public DBContext(Context context) {
        super(context);
        this.mContext=context;
    }
    /**
     * 返回 数据库文件
     * 重写此方法 返回我们位于sd卡的数据库文件
     * @param name
     * @return
     */
    @Override
    public File getDatabasePath(String name) {
        String dbpath="/storage/emulated/0/data";//内部硬盘
        File file =new File(dbpath,name);
        return file;
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name),factory);
        return db;
    }
}
