package com.example.wqter.androidclient_mypart.Util;

/**
 * Author: MrZeyu on 2017/11/24 20:55
 * **
 * Email : MrZeyu@126.com
 */

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {
    private static SharedPreferences sp;
    //´æÈë×Ö·û´®
    public static void saveString(Context context, String key, String value) {
        if(sp == null) {
            sp = context.getSharedPreferences("userInfo", 0);
        }
        sp.edit().putString(key, value).apply();//或者commit
    }
    //¶ÁÈ¡×Ö·û´®
    public static String getString(Context context, String key) {
        if(sp == null) {
            sp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        }

        return sp.getString(key, "");

    }
}
