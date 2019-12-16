package com.example.wqter.androidclient_mypart.my_application;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wqter on 2019/12/11.
 */

public class GlobalApplication extends Application {

    private List<String> deviceName=new ArrayList<>(),deviceCom=new ArrayList<>(),deviceCode=new ArrayList<>();

    public void setDeviceName(List<String> list)
    {
        this.deviceName=list;
    }
    public List<String> getDeviceName(){

        return this.deviceName;
    }

    public void setDeviceCom(List<String> list){
        this.deviceCom=list;
    }

    public List<String>getDeviceCom(){
        return this.deviceCom;
    }
    public void setDeviceCode(List<String> list){
        this.deviceCom=list;
    }
    public List<String> getDeviceCode(){
        return this.deviceCode;
    }


}
