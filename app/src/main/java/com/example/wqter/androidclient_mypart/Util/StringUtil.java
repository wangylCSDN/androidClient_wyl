package com.example.wqter.androidclient_mypart.Util;

/**
 * Author: MrZeyu on 2017/12/11 13:51
 * **
 * Email : MrZeyu@126.com
 */

public class StringUtil {


    private String cut_string(String str1, String str2){   //前面的减去后面的

        return Integer.toString( Integer.parseInt(str1)- Integer.parseInt(str2));
    }

    public static Boolean isNeedUpdateVersion(String thisVersion, String cloudVersion){            //是否需要更新是返回ture
        String[] thisVersion_array = thisVersion.split("\\.");
        String[] nowVersion_array = cloudVersion.split("\\.");

        if(Integer.parseInt(thisVersion_array[0])< Integer.parseInt(nowVersion_array[0])){
            return true;
        }else if(Integer.parseInt(thisVersion_array[0])== Integer.parseInt(nowVersion_array[0])){
            if(Integer.parseInt(thisVersion_array[1])< Integer.parseInt(nowVersion_array[1])){
                return true;
            }else if(Integer.parseInt(thisVersion_array[1])== Integer.parseInt(nowVersion_array[1])){
                if(thisVersion_array.length==3){
                    if(Integer.parseInt(thisVersion_array[2])< Integer.parseInt(nowVersion_array[2])){
                        return true;
                    }else {
                        return false;
                    }
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }
        else{
            return false;
        }
    }
}
