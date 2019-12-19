package com.example.wqter.androidclient_mypart.Bean;

/**
 * Created by wqter on 2019/12/19.
 */

public class ShieldAlarmBean {
    public String device_name;
    public String alarm_info;

    public ShieldAlarmBean(String devcie_name,String alarm_info)
    {
        this.device_name=devcie_name;
        this.alarm_info=alarm_info;
    }
}
