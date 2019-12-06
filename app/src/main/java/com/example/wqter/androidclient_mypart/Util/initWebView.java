package com.example.wqter.androidclient_mypart.Util;

import android.webkit.WebSettings;
import android.webkit.WebViewClient;

/**
 * Author: MrZeyu on 2018/1/25 19:33
 * **
 * Email : MrZeyu@126.com
 */

public class initWebView {
    private android.webkit.WebView WebView;

    public initWebView(android.webkit.WebView webView) {
        WebView=webView;
    }

    public void setWebViewStyle(){
        WebSettings webSettings = WebView.getSettings();

        //支持缩放，默认为true。
        webSettings .setSupportZoom(true);
        // 设置出现缩放工具
        webSettings .setBuiltInZoomControls(true);
        //调整图片至适合webview的大小
        webSettings .setUseWideViewPort(true);
        // 缩放至屏幕的大小
        webSettings .setLoadWithOverviewMode(true);
        //设置默认编码
        webSettings .setDefaultTextEncodingName("utf-8");
        ////设置自动加载图片
        webSettings .setLoadsImagesAutomatically(true);
        //开启javascript
        webSettings.setJavaScriptEnabled(true);
        //支持内容重新布局
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //关闭webview中缓存
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);


        //获取触摸焦点
        WebView.requestFocusFromTouch();
        //WebView.setInitialScale(100);   //100代表不缩放

        //设置不用系统浏览器打开,直接显示在当前Webview
        WebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(android.webkit.WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }
}

