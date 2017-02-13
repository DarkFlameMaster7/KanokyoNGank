package me.tomoya.kanojyongank.module.gank.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import butterknife.BindView;
import java.util.Date;
import javax.inject.Inject;
import me.tomoya.kanojyongank.R;
import me.tomoya.kanojyongank.annotation.PropertiesInject;
import me.tomoya.kanojyongank.base.BaseActivity;
import me.tomoya.kanojyongank.bean.GankData;
import me.tomoya.kanojyongank.bean.Kanojyo;
import me.tomoya.kanojyongank.module.gank.presenter.GankPresenter;
import me.tomoya.kanojyongank.module.gank.presenter.contract.GankContract;
import me.tomoya.kanojyongank.module.gank.ui.adapter.ExpandableGankAdapter;
import me.tomoya.kanojyongank.util.DateUtils;

@PropertiesInject(contentViewId = R.layout.activity_gank, enableSlideExit = true,
		isStatusBarTranslucent = true)
public class GankActivity extends BaseActivity<GankPresenter> implements GankContract.View {
	private static final String FLAG_START = "start_data";
	private String mDateStr;

	@BindView(R.id.rv_gank)       RecyclerView      rvGank;
	@BindView(R.id.activity_gank) CoordinatorLayout activityGank;

	//GankListAdapter adapter;
	@Inject ExpandableGankAdapter gankAdapter;
	//private List<Gank> ganks = new ArrayList<>();
	private int bgColor;
	private GankData mGankData = new GankData();

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivityComponent().inject(this);
		mPresenter.attachView(this);
		initData();
		initViews();
	}

	private void initData() {
		Kanojyo kanojyo = (Kanojyo) getIntent().getSerializableExtra(FLAG_START);
		Date date = kanojyo.publishedAt;
		int[] ymd = DateUtils.divideDate(date);
		mPresenter.getGankData(ymd[0], ymd[1], ymd[2]);
		bgColor = kanojyo.color;
		mDateStr = ymd[0] +"."+ ymd[1] +"."+  ymd[2];
	}

	private void initViews() {
		activityGank.setBackgroundColor(bgColor);
		this.setTitle(mDateStr);
		//toolbar.setBackgroundColor(bgColor);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
		//adapter = new GankListAdapter(this, ganks);

		//rvGank.setAdapter(gankAdapter);
		rvGank.setLayoutManager(linearLayoutManager);
		//adapter.setItemClickListener(new OnItemClickListener() {
		//	@Override
		//	public void onItemClick(View view, int position) {
		//		WebActivity.activityStart(GankActivity.this, ganks.get(position).getUrl(), bgColor);
		//	}
		//});
	}

	@Override
	protected void onPause() {
		super.onPause();
		overridePendingTransition(R.anim.fade_in, R.anim.activity_out);
	}

	@Override
	protected void onStop() {
		super.onStop();
		overridePendingTransition(R.anim.fade_in, R.anim.activity_out);
	}

	@Override
	public void fetchData(GankData data) {
		Log.e("data", "fetchData: "+data.category.toString());
		//ganks.addAll(dataList);
		gankAdapter.setData(data);
		//mGankData = data;
		rvGank.setAdapter(gankAdapter);
		gankAdapter.notifyDataSetChanged();
	}

	@Override
	public void useNightMode() {

	}
}
