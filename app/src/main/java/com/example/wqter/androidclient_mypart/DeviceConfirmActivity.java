package com.example.wqter.androidclient_mypart;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by wqter on 2019/12/6.
 */

public class DeviceConfirmActivity extends AppCompatActivity implements View.OnClickListener{
    private Spinner com0_sp,com0_sp1;
    private Button sta_btn,confirm_btn,com_btn,com_btn_change;
    private EditText device_num;
    private AlertDialog.Builder com0_bd;
    private AlertDialog com0_ad;
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
        com0_sp=(Spinner)findViewById(R.id.com0_sp);
        com0_sp1=(Spinner)findViewById(R.id.com0_sp1);
    }

    @Override
    public void onClick(View view) {

    }
}
