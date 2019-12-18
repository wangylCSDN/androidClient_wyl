package com.example.wqter.androidclient_mypart.View;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;


public class SubListView extends ListView {

    public SubListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

    }
}
