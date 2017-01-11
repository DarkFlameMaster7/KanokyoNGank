package com.tomoya.kanojyongank.module;

import android.animation.Animator;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tomoya.kanojyongank.R;
import com.tomoya.kanojyongank.bean.Kanojyo;
import com.tomoya.kanojyongank.bean.KanojyoData;
import com.tomoya.kanojyongank.bean.ShortFilmData;
import com.tomoya.kanojyongank.module.adapter.KanojyoPagerAdapter;
import com.tomoya.kanojyongank.module.base.BaseActivity;
import com.tomoya.kanojyongank.util.AnimatorUtils;
import com.tomoya.kanojyongank.util.DateUtils;
import com.tomoya.kanojyongank.util.RxUtils;
import com.tomoya.kanojyongank.widget.ViewPagerScroller;
import com.tomoya.rhythmview.IChangePagerListener;
import com.tomoya.rhythmview.RhythmView;
import com.tomoya.rhythmview.RhythmViewAdapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnPageChange;
import rx.Observable;
import rx.Observer;
import rx.functions.Func1;
import rx.functions.Func2;

import static com.tomoya.kanojyongank.R.id.rhythm_view;

public class ShowActivity extends BaseActivity {
    private static final String TAG = "ShowActicity";

    @Bind(rhythm_view)        RhythmView  rhythmView;
    @Bind(R.id.vp_show)       ViewPager   viewPager;
    @Bind(R.id.activity_show) View        mMainView;
    @Bind(R.id.btn_logo)      TextView    btnLogo;
    @Bind(R.id.ibtn_rocket)   ImageButton ibtnRocket;
    @Bind(R.id.text_day)      TextView    textDay;
    @Bind(R.id.text_date)     TextView    textDate;

    private KanojyoPagerAdapter fragmentAdapter;
    private RhythmViewAdapter   adapter;

    private long mExitTime = 0;
    private List<Kanojyo> kanojyoTachi;

    /*
    * 记录上一个颜色
    * */
    private int mPreColor;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        ButterKnife.bind(this);
        Window window = this.getWindow();
        //translucent statusbar
        //        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //绘制完成后加载数据
        window.getDecorView().post(new Runnable() {
            @Override public void run() {
                loadData(1);
            }
        });
        initData();
        initViews();
        initActions();
    }

    private void initData() {
        kanojyoTachi = new ArrayList<>();
    }

    public void initActions() {
        rhythmView.setListener(new IChangePagerListener() {
            @Override public void onSelected(final int position) {
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
        setViewPagerScrollSpeed(viewPager, 400);
    }

    @OnPageChange(R.id.vp_show) void onPageSelected(int position) {
        //set background color
        int currColor = kanojyoTachi.get(position).color;
        AnimatorUtils.showBgColorAnimation(mMainView, mPreColor, currColor, 400);
        mPreColor = currColor;
        moveAppPager(position);
    }

    @OnClick(R.id.ibtn_rocket) void toFirstPage() {
        viewPager.setCurrentItem(0);
    }

    /**
     * 设置ViewPager的滚动速度，即每个选项卡的切换速度
     *
     * @param viewPager ViewPager控件
     * @param speed 滚动速度，毫秒为单位
     */
    private void setViewPagerScrollSpeed(ViewPager viewPager, int speed) {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");//动态修改类文件
            field.setAccessible(true);
            ViewPagerScroller viewPagerScroller =
                    new ViewPagerScroller(viewPager.getContext(), new OvershootInterpolator(0.6F));
            ;
            viewPagerScroller.setDuration(speed);
            field.set(viewPager, viewPagerScroller);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void loadData(int page) {
        subscription = Observable.zip(gankApi.getKanojyoData(page), gankApi.getShortFilmData(page),
                new Func2<KanojyoData, ShortFilmData, KanojyoData>() {
                    @Override
                    public KanojyoData call(KanojyoData kanojyoData, ShortFilmData shortFilmData) {
                        return reconstructKanojyoData(kanojyoData, shortFilmData);
                    }
                })
                                 .map(new Func1<KanojyoData, List<Kanojyo>>() {
                                     @Override public List<Kanojyo> call(KanojyoData kanojyoData) {
                                         return kanojyoData.results;
                                     }
                                 })
                                 .compose(this.<List<Kanojyo>>bindToLifecycle())
                                 .compose(RxUtils.<List<Kanojyo>>normalSchedulers())
                                 .subscribe(new Observer<List<Kanojyo>>() {
                                     @Override public void onCompleted() {
                                     }

                                     @Override public void onError(Throwable e) {
                                         Log.e("Rx", "onError: " + e);
                                     }

                                     @Override public void onNext(List<Kanojyo> kanojyos) {
                                         if (kanojyos == null) return;
                                         //notify data
                                         kanojyoTachi.addAll(kanojyos);
                                         reportFullyDrawn();//加载完毕后view绘制完成时间
                                         adapter.notifyDataSetChanged();
                                         fragmentAdapter.refresh();
                                         moveAppPager(0);
                                     }
                                 });
        addSubscription(subscription);
    }

    /**
     * 移动到相应位置并设置背景颜色和rocket  caution:在view绘制完成后调用
     */
    private void moveAppPager(int position) {
        rhythmView.moveToSelectedPosition(position);
        if (kanojyoTachi != null && kanojyoTachi.size() > 0) {
            mMainView.setBackgroundColor(kanojyoTachi.get(position).color);
            setDateText(kanojyoTachi.get(position).publishedAt);
        }
        isRocketBtnShow(position);
    }

    /**
     * 添加颜色属性
     */
    private KanojyoData reconstructKanojyoData(KanojyoData kanojyoData,
            ShortFilmData shortFilmData) {
        for (int i = 0; i < kanojyoData.results.size(); i++) {
            Kanojyo kanojyo = kanojyoData.results.get(i);
            kanojyo.color = (int) -(Math.random() * (16777216 - 1) + 1);
            kanojyo.desc = shortFilmData.results.get(i).desc;
            kanojyo.video_url = shortFilmData.results.get(i).url;
        }
        return kanojyoData;
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

    @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
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
}
