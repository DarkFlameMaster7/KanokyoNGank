package com.tomoya.kanojyongank.module;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import com.tomoya.kanojyongank.R;
import com.tomoya.kanojyongank.widget.SlideLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WebActivity extends AppCompatActivity {
    private static final String FLAG_URL = "web_url";
    private static final String FLAG_BGCOLOR = "bg_clolr";
    @Bind(R.id.webv_gank)
    WebView webvGank;
    @Bind(R.id.activity_web)
    RelativeLayout activityWeb;
    private String url;
    private int bgColor;

    public static void activityStart(Context context, String url,int color) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(FLAG_URL, url);
        intent.putExtra(FLAG_BGCOLOR, color);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        //        Window window = this.getWindow();
        //        //translucent statusbar
        //        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        SlideLayout slideLayout = new SlideLayout(this);
        slideLayout.bind(this);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initView() {
        activityWeb.setBackgroundColor(bgColor);
        webvGank.setWebChromeClient(new WebChromeClient());
        webvGank.loadUrl(url);
    }

    private void initData() {
        Intent intent = getIntent();
        url = intent.getStringExtra(FLAG_URL);
        bgColor = intent.getIntExtra(FLAG_BGCOLOR, Color.WHITE);
    }
}
