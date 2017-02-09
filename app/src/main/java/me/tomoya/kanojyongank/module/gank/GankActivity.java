package me.tomoya.kanojyongank.module.gank;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.RelativeLayout;

import me.tomoya.kanojyongank.R;
import me.tomoya.kanojyongank.annotation.PropertiesInject;
import me.tomoya.kanojyongank.bean.Gank;
import me.tomoya.kanojyongank.bean.GankData;
import me.tomoya.kanojyongank.bean.Kanojyo;
import me.tomoya.kanojyongank.base.BaseActivity;
import me.tomoya.kanojyongank.module.detail.WebActivity;
import me.tomoya.kanojyongank.module.gank.adapter.GankListAdapter;
import me.tomoya.kanojyongank.util.DateUtils;
import me.tomoya.kanojyongank.util.RxUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import rx.Observer;
import rx.Subscription;
import rx.functions.Func1;

@PropertiesInject(contentViewId = R.layout.activity_gank, enableSlideExit = true,
		isStatusBarTranslucent = true)
public class GankActivity extends BaseActivity {
	private static final String FLAG_START = "start_data";

	@Bind(R.id.rv_gank)       RecyclerView   rvGank;
	@Bind(R.id.activity_gank) RelativeLayout activityGank;

	GankListAdapter adapter;
	private List<Gank> ganks = new ArrayList<>();
	private int bgColor;

	public static Intent newIntent(Context context, Kanojyo kanojyo) {
		Intent intent = new Intent(context, GankActivity.class);
		intent.putExtra(FLAG_START, kanojyo);
		return intent;
	}

	public static void startGankActivity(Context context, Kanojyo kanojyo) {
		Intent intent = new Intent(context, GankActivity.class);
		intent.putExtra(FLAG_START, kanojyo);
		context.startActivity(intent);
	}

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
		initViews();
	}

	private void initData() {
		Kanojyo kanojyo = (Kanojyo) getIntent().getSerializableExtra(FLAG_START);
		Date    date    = kanojyo.publishedAt;
		int[]   ymd     = DateUtils.divideDate(date);
		loadData(ymd[0], ymd[1], ymd[2]);
		bgColor = kanojyo.color;
	}

	private void initViews() {
		activityGank.setBackgroundColor(bgColor);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
		adapter = new GankListAdapter(this, ganks);
		rvGank.setAdapter(adapter);
		rvGank.setLayoutManager(linearLayoutManager);
		adapter.setItemClickListener(
				(view, position) -> WebActivity.activityStart(GankActivity.this, ganks.get(position)
						.getUrl(), bgColor));
	}

	private void loadData(int year, int month, int day) {
		Subscription subscription = gankApi.getGankData(year, month, day)
				.map(new Func1<GankData, List<Gank>>() {
					@Override public List<Gank> call(GankData gankData) {
						return addAllData2List(gankData.results);
					}
				})
				.compose(this.<List<Gank>>bindToLifecycle())
				.compose(RxUtils.<List<Gank>>normalSchedulers())
				.subscribe(new Observer<List<Gank>>() {
					@Override public void onCompleted() {

					}

					@Override public void onError(Throwable e) {
						Log.e("Gank", "onError: " + e.toString());
					}

					@Override public void onNext(List<Gank> list) {
						adapter.notifyDataSetChanged();
						Log.e("Gank", "onNext: " + adapter.getItemCount());
					}
				});
		addSubscription(subscription);
	}

	@Override protected void onPause() {
		super.onPause();
		overridePendingTransition(R.anim.fade_in, R.anim.activity_out);
	}

	@Override protected void onStop() {
		super.onStop();
		overridePendingTransition(R.anim.fade_in, R.anim.activity_out);
	}

	public List<Gank> addAllData2List(GankData.Result results) {
		if (results.androidList != null) ganks.addAll(results.androidList);
		if (results.iOSList != null) ganks.addAll(results.iOSList);
		if (results.appList != null) ganks.addAll(results.appList);
		if (results.拓展资源List != null) ganks.addAll(results.拓展资源List);
		if (results.瞎推荐List != null) ganks.addAll(results.瞎推荐List);
		if (results.休息视频List != null) ganks.addAll(0, results.休息视频List);
		return ganks;
	}
}
