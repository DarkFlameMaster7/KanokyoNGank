package me.tomoya.kanojyongank.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Scroller;
import java.lang.reflect.Field;
import me.tomoya.kanojyongank.R;

/**
 * Created by piper on 17-2-12.
 */

public class EzScrollViewPager extends ViewPager {
	private int mSpeed;//滑动速度(时间)

	private float mShakeFactor;//抖动幅度

	public EzScrollViewPager(Context context) {
		this(context,null);
	}

	public EzScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (attrs != null) {
			TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerProp);
			mSpeed = a.getInteger(R.styleable.ViewPagerProp_speed, 400);
			mShakeFactor = a.getFloat(R.styleable.ViewPagerProp_shakeFactor,0.6F);
		}
		init();
	}

	public void init() {
		try {
			Field field = ViewPager.class.getDeclaredField("mScroller");//动态修改类文件
			field.setAccessible(true);
			EzScroller scroller = new EzScroller(getContext(), new OvershootInterpolator(mShakeFactor));
			scroller.setDuration(mSpeed);
			field.set(this, scroller);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置滑动时间间隔
	 *
	 * @param speed 速度(间隔)
	 */
	public void setScrollDuration(int speed) {
		mSpeed = speed;
		requestLayout();
	}

	/**
	 * 设置抖动幅度
	 *
	 * @param factor 抖动幅度
	 */
	public void setShakeFactor(float factor) {
		mShakeFactor = factor;
		requestLayout();
	}

	class EzScroller extends Scroller {
		private int speed;

		public EzScroller(Context context) {
			super(context);
		}

		public EzScroller(Context context, Interpolator interpolator) {
			super(context, interpolator);
		}

		public EzScroller(Context context, Interpolator interpolator, boolean flywheel) {
			super(context, interpolator, flywheel);
		}

		public void setDuration(int speed) {
			this.speed = speed;
		}

		@Override
		public void startScroll(int startX, int startY, int dx, int dy) {
			super.startScroll(startX, startY, dx, dy, speed);
		}

		@Override
		public void startScroll(int startX, int startY, int dx, int dy, int duration) {
			super.startScroll(startX, startY, dx, dy, speed);
		}
	}
}
