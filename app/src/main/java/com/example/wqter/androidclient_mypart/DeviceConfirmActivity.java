package com.example.wqter.androidclient_mypart;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

/**
 * Created by wqter on 2019/12/6.
 */

public class DeviceConfirmActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature( Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deviceconfirm);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        initView();

    }
    private void initView(){

    }
}
