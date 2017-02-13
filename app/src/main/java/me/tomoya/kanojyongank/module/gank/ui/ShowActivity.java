package me.tomoya.kanojyongank.module.gank.ui;

import android.animation.Animator;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.OnPageChange;
import com.jakewharton.rxbinding.view.RxView;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import me.tomoya.kanojyongank.R;
import me.tomoya.kanojyongank.annotation.PropertiesInject;
import me.tomoya.kanojyongank.base.BaseActivity;
import me.tomoya.kanojyongank.bean.Kanojyo;
import me.tomoya.kanojyongank.module.gank.presenter.MainPresenter;
import me.tomoya.kanojyongank.module.gank.presenter.contract.MainContract;
import me.tomoya.kanojyongank.module.gank.ui.adapter.KanojyoPagerAdapter;
import me.tomoya.kanojyongank.util.AnimatorUtils;
import me.tomoya.kanojyongank.util.DateUtils;
import me.tomoya.kanojyongank.widget.EzScrollViewPager;
import me.tomoya.kanojyongank.widget.rhythm.IChangePagerListener;
import me.tomoya.kanojyongank.widget.rhythm.RhythmView;
import me.tomoya.kanojyongank.widget.rhythm.RhythmViewAdapter;
import rx.functions.Action1;

@PropertiesInject(contentViewId = R.layout.activity_show, isStatusBarTranslucent = true)
public class ShowActivity extends BaseActivity<MainPresenter> implements MainContract.View {
	private static final String TAG = "ShowActicity";

	@BindView(R.id.activity_show) View              mMainView;
	@BindView(R.id.btn_logo)      TextView          btnLogo;
	@BindView(R.id.text_date)     TextView          textDate;
	@BindView(R.id.text_day)      TextView          textDay;
	@BindView(R.id.rhythm_view)   RhythmView        rhythmView;
	@BindView(R.id.ibtn_rocket)   ImageButton       ibtnRocket;
	@BindView(R.id.vp_show)       EzScrollViewPager viewPager;

	private KanojyoPagerAdapter fragmentAdapter;
	private RhythmViewAdapter   adapter;

	private long mExitTime = 0;
	private List<Kanojyo> kanojyoTachi;

	/*
	* 记录上一个颜色
	* */
	private int mPreColor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivityComponent().inject(this);
		mPresenter.attachView(this);
		//Window window = this.getWindow();
		////绘制完成后加载数据
		//window.getDecorView().post(() -> mPresenter.getDailyData());
		initData();
		initViews();
		initActions();
	}

	private void initData() {
		kanojyoTachi = new ArrayList<>();
	}

	public void initActions() {
		RxView.clicks(ibtnRocket).subscribe(new Action1<Void>() {
			@Override
			public void call(Void aVoid) {
				viewPager.setCurrentItem(0);
			}
		});
		rhythmView.setListener(new IChangePagerListener() {
			@Override
			public void onSelected(final int position) {
				new Handler().postDelayed(new Runnable() {
					public void run() {
						viewPager.setCurrentItem(position);
					}
				}, 100L);
			}
		});
	}

	public void initViews() {
		//rhythmview
		int height = (int) rhythmView.getItemWidth() + (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 10.0F, getResources().getDisplayMetrics());
		rhythmView.getLayoutParams().height = height;
		adapter = new RhythmViewAdapter(this, rhythmView, kanojyoTachi);
		rhythmView.setAdapter(adapter);

		//viewpager & fragment
		fragmentAdapter = new KanojyoPagerAdapter(getSupportFragmentManager(), kanojyoTachi);
		((RelativeLayout.LayoutParams) viewPager.getLayoutParams()).bottomMargin = height;
		viewPager.setPageMargin(50);
		viewPager.setAdapter(fragmentAdapter);
	}

	@OnPageChange(R.id.vp_show)
	void onPageSelected(int position) {
		//set background color
		int currColor = kanojyoTachi.get(position).color;
		AnimatorUtils.showBgColorAnimation(mMainView, mPreColor, currColor, 400);
		AnimatorUtils.showBgColorAnimation(toolbar, mPreColor, currColor, 400);
		mPreColor = currColor;
		moveAppPager(position);
	}

	//@OnClick(R.id.ibtn_rocket)
	//void toFirstPage() {
	//	viewPager.setCurrentItem(0);
	//}

	/**
	 * 移动到相应位置并设置背景颜色和rocket  caution:在view绘制完成后调用
	 */
	private void moveAppPager(int position) {
		rhythmView.moveToSelectedPosition(position);
		if (kanojyoTachi != null && kanojyoTachi.size() > 0) {
			mMainView.setBackgroundColor(kanojyoTachi.get(position).color);
			toolbar.setBackgroundColor(kanojyoTachi.get(position).color);
			setDateText(kanojyoTachi.get(position).publishedAt);
		}
		isRocketBtnShow(position);
	}

	private void setDateText(Date date) {
		Date now = new Date(System.currentTimeMillis());
		if (DateUtils.isTheSameDay(now, date)) {
			textDate.setTextSize(28);
			textDay.setTextSize(28);
			textDay.setText("今日");
			textDate.setText("干货");
		} else if (DateUtils.isYesterday(now, date)) {
			textDate.setTextSize(28);
			textDay.setTextSize(28);
			textDay.setText("昨日");
			textDate.setText("干货");
		} else {
			String week = DateUtils.getWeekOfDate(this, date);
			int[] ymd = DateUtils.divideDate(date);
			textDate.setTextSize(12);
			textDay.setTextSize(36);
			textDate.setText(ymd[1] + "月\n" + week);
			textDay.setText(String.valueOf(ymd[2]));
		}
	}

	private void isRocketBtnShow(int position) {
		if (position > 1) {
			if (ibtnRocket.getVisibility() == View.GONE) {
				ibtnRocket.setVisibility(View.VISIBLE);
				AnimatorUtils.animFadeIn(ibtnRocket);
			}
		} else if (ibtnRocket.getVisibility() == View.VISIBLE) {
			AnimatorUtils.animFadeOut(this.ibtnRocket).addListener(new Animator.AnimatorListener() {
				public void onAnimationCancel(Animator paramAnimator) {
				}

				public void onAnimationEnd(Animator paramAnimator) {
					ibtnRocket.setVisibility(View.GONE);
				}

				public void onAnimationRepeat(Animator paramAnimator) {

				}

				public void onAnimationStart(Animator paramAnimator) {
				}
			});
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (System.currentTimeMillis() - mExitTime > 2000) {
				Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();
			} else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void useNightMode() {

	}

	@Override
	public ShowActivity getActivcity() {
		return this;
	}

	@Override
	public void fetchData(List<Kanojyo> dataList) {
		kanojyoTachi.addAll(dataList);
		reportFullyDrawn();//加载完毕后view绘制完成时间
		adapter.notifyDataSetChanged();
		fragmentAdapter.refresh();
		moveAppPager(0);
	}
}
