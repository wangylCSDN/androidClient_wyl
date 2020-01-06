package com.example.wqter.androidclient_mypart;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.webkit.WebView;

import com.example.wqter.androidclient_mypart.Util.initWebView;

/**
 * Created by wqter on 2019/12/17.
 */

public class HtmlActvity extends AppCompatActivity {
    private android.webkit.WebView WebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        this.requestWindowFeature( Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_html);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        initView();
    }

    private void initView() {
        WebView=(android.webkit.WebView)findViewById(R.id.test_html);
        initWebView initWebView=new initWebView (WebView);
        initWebView.setWebViewStyle();
        WebView.loadUrl("http://www.baidu.com");
    }

}
