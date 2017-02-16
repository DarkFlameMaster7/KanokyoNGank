package me.tomoya.kanojyongank.module.gank.ui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import butterknife.BindView;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.DraweeTransition;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.Date;
import javax.inject.Inject;
import me.tomoya.kanojyongank.R;
import me.tomoya.kanojyongank.annotation.PropertiesInject;
import me.tomoya.kanojyongank.base.BaseActivity;
import me.tomoya.kanojyongank.bean.GankData;
import me.tomoya.kanojyongank.bean.Kanojyo;
import me.tomoya.kanojyongank.common.GContants;
import me.tomoya.kanojyongank.module.gank.presenter.GankPresenter;
import me.tomoya.kanojyongank.module.gank.presenter.contract.GankContract;
import me.tomoya.kanojyongank.module.gank.ui.adapter.ExpandableGankAdapter;
import me.tomoya.kanojyongank.util.AnimatorUtils;
import me.tomoya.kanojyongank.util.DateUtils;
import me.tomoya.kanojyongank.util.ImageLoader;

@PropertiesInject(contentViewId = R.layout.activity_gank, enableSlideExit = true,
		isStatusBarTranslucent = true)
public class GankActivity extends BaseActivity<GankPresenter> implements GankContract.View {

	private String mDateStr;

	@BindView(R.id.rv_gank)       RecyclerView      rvGank;
	@BindView(R.id.activity_gank) CoordinatorLayout activityGank;
	@BindView(R.id.img_frame)     SimpleDraweeView  imgFrame;
	@BindView(R.id.progress_gank) ProgressBar       progressBar;

	@Inject ExpandableGankAdapter gankAdapter;

	private int    bgColor;
	private String imgUrl;

	public static Intent newIntent(Context context, Kanojyo kanojyo) {
		Intent intent = new Intent(context, GankActivity.class);
		intent.putExtra(GContants.FLAG_START, kanojyo);
		return intent;
	}

	public static void startGankActivity(Context context, Kanojyo kanojyo) {
		Intent intent = new Intent(context, GankActivity.class);
		intent.putExtra(GContants.FLAG_START, kanojyo);
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

	@Override
	protected void onStart() {
		super.onStart();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			getWindow().setSharedElementEnterTransition(
					DraweeTransition.createTransitionSet(ScalingUtils.ScaleType.FIT_START,
							ScalingUtils.ScaleType.FIT_START));
			getWindow().setSharedElementReturnTransition(
					DraweeTransition.createTransitionSet(ScalingUtils.ScaleType.FIT_START,
							ScalingUtils.ScaleType.FIT_START));
		}
	}

	private void initData() {
		Kanojyo kanojyo = (Kanojyo) getIntent().getSerializableExtra(GContants.FLAG_START);
		Date date = kanojyo.publishedAt;
		int[] ymd = DateUtils.divideDate(date);
		imgUrl = kanojyo.url;
		mPresenter.getGankData(ymd[0], ymd[1], ymd[2]);
		bgColor = kanojyo.color;
		mDateStr = ymd[0] + "." + ymd[1] + "." + ymd[2];
	}

	private void initViews() {
		ImageLoader.loadImage(imgFrame, imgUrl,getResources());
		//imgFrame.setAspectRatio();
		activityGank.setBackgroundColor(bgColor);
		this.setTitle(mDateStr);
		//toolbar.setBackgroundColor(bgColor);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
		rvGank.setLayoutManager(linearLayoutManager);

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
		gankAdapter.setData(data);
		rvGank.setAdapter(gankAdapter);
		AnimatorUtils.animFadeIn(rvGank, 500, new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				rvGank.setAlpha((Float) animation.getAnimatedValue());
				rvGank.requestLayout();
			}
		});


		progressBar.setVisibility(View.INVISIBLE);
	}

	@Override
	public void useNightMode() {

	}
}
