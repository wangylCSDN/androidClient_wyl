package com.example.wqter.androidclient_mypart;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.wqter.androidclient_mypart.Adapter.MyPageAdapter;
import com.example.wqter.androidclient_mypart.fragment.ConfirmAlarmFragment;
import com.example.wqter.androidclient_mypart.fragment.NowAlarmFragment;
import com.example.wqter.androidclient_mypart.fragment.ShieldAlarmFragment;

import java.util.ArrayList;

/**
 * Created by wqter on 2019/12/18.
 */

public class NowAlarmActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ArrayList<String> list;
    private ViewPager viewPager;

    private ArrayList<Fragment> fragments;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nowalarm);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        initView();

    }

    private void initView() {
        tabLayout=(TabLayout)findViewById(R.id.now_alarm_tablayout);
        toolbar=(Toolbar)findViewById(R.id.now_alarm_toolbar);
        viewPager=(ViewPager)findViewById(R.id.now_alarm_view);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initData();
    }

    private void initData() {
        list=new ArrayList<>();
        list.add("当前报警");
        list.add("确认报警");
        list.add("屏蔽报警");

        fragments=new ArrayList<>();

        NowAlarmFragment NowAlarm=new NowAlarmFragment();
        ConfirmAlarmFragment ConfirmAlarm=new ConfirmAlarmFragment();
        ShieldAlarmFragment ShieldAlarm=new ShieldAlarmFragment();

        fragments.add(NowAlarm.newInstance(list.get(0)));
        fragments.add(ConfirmAlarm.newInstance(list.get(1)));
        fragments.add(ShieldAlarm.newInstance(list.get(2)));

        MyPageAdapter a=new MyPageAdapter(getSupportFragmentManager(),list,fragments);

        viewPager.setAdapter(a);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onDestroy() {                         //销毁所有东西，清空内存
        tabLayout=null;
        toolbar=null;
        viewPager=null;
        list=null;
        setContentView(R.layout.view_null);

        super.onDestroy();
    }

}
