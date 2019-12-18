package com.example.wqter.androidclient_mypart.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wqter.androidclient_mypart.R;

/**
 * Created by wqter on 2019/12/18.
 */

public class ShieldAlarmFragment extends android.support.v4.app.Fragment {

    private View mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化视图和数据
        mView = getActivity().getLayoutInflater().inflate(R.layout.fragment_shieldalarm,null);
    }


    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return mView;
    }
    @Override
    public void onDestroyView() {

        super.onDestroyView();
        ViewGroup mGroup=(ViewGroup) mView.getParent();
        if(mGroup!=null){
            mGroup.removeView(mView);
        }
    }

    public ShieldAlarmFragment newInstance(String title){           //回调
        ShieldAlarmFragment fragment = new ShieldAlarmFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }
}
