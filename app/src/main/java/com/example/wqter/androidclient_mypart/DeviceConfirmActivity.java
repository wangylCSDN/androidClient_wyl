package com.example.wqter.androidclient_mypart;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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
        confirm_btn=(Button)findViewById(R.id.confirm_btn);

        com0_sp=(Spinner)findViewById(R.id.com0_sp);
        com0_sp1=(Spinner)findViewById(R.id.com0_sp1);
        com_btn=(Button)findViewById(R.id.com0_btn);
        com_btn_change=(Button)findViewById(R.id.com0_btn_change);
        com_btn.setOnClickListener(this);
        com_btn_change.setOnClickListener(this);

        initSpinner();

    }
    private void initSpinner(){
        com0_sp.setSelection(1,true);
        com0_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String s1=(String)com0_sp.getSelectedItem();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View view) {

    }
}
