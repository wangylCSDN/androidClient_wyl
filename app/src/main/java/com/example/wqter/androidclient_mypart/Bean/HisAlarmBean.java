package com.example.wqter.androidclient_mypart.Bean;

/**
 * Created by wangyl on 2018/11/17.
 */

public class HisAlarmBean {
    public String device_type;
    public String device_name;
    public String start_time;
    public String end_time;
    public String alarm_info;
    public String alarm_type;
    public String alarm_reason;
    public String solve_method;



    public HisAlarmBean(String device_type,String device_name,String start_time,String end_time,String alarm_info,String alarm_type,String alarm_reason,String solve_method )
    {
        this.device_type=device_type;
        this.device_name=device_name;
        this.start_time=start_time;
        this.end_time=end_time;
        this.alarm_info=alarm_info;
        this.alarm_type=alarm_type;
        this.alarm_reason=alarm_reason;
        this.solve_method=solve_method;
    }
}
