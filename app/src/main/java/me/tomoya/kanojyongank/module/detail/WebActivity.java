package me.tomoya.kanojyongank.module.detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import me.tomoya.kanojyongank.R;
import me.tomoya.kanojyongank.annotation.PropertiesInject;
import me.tomoya.kanojyongank.base.BaseActivity;

import butterknife.Bind;

@PropertiesInject(contentViewId = R.layout.activity_web, enableSlideExit = true)
public class WebActivity extends BaseActivity {
	private static final String FLAG_URL     = "web_url";
	private static final String FLAG_BGCOLOR = "bg_clolr";
	@Bind(R.id.webv_gank)
	WebView        webvGank;
	@Bind(R.id.container_web)
	RelativeLayout activityWeb;
	private String url;
	private int    bgColor;

	public static void activityStart(Context context, String url, int color) {
		Intent intent = new Intent(context, WebActivity.class);
		intent.putExtra(FLAG_URL, url);
		intent.putExtra(FLAG_BGCOLOR, color);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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
