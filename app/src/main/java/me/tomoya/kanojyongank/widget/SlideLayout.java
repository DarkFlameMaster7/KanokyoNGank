package me.tomoya.kanojyongank.widget;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import me.tomoya.kanojyongank.util.ScreenUtils;

/**
 * Created by Tomoya-Hoo on 2016/11/17. 实现边缘滑动关闭Activity 获取并删除DecorView的RootView 将其添加至当前View
 * 再添加至DecorView
 */

public class SlideLayout extends FrameLayout {
	/*
	* 边缘事件 滚动处理*/
	private ViewDragHelper mViewDragHelper;
	private ViewGroup      mDecorView;
	private View           mRootView;
	/*
	* 要绑定的Activity*/
	private Activity       mActivity;
	/*
	* 屏幕宽高*/
	private int            mScreenHeight, mScreenWidth;
	/*
	* 滑动退出距离*/
	private float mSlideExitWidth;
	/*
	* 当前滑动距离*/
	private int   mCurSildeWidth;

	public SlideLayout(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		Log.e("SlideLayout", "init: ");
		mViewDragHelper = ViewDragHelper.create(this, callback);
		mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
		int[] srcSize = ScreenUtils.getScreenSize(context);
		mScreenWidth = srcSize[0];
		mScreenHeight = srcSize[1];
		mSlideExitWidth = mScreenWidth * 0.3f;
	}

	@Override
	public boolean onInterceptHoverEvent(MotionEvent ev) {
		return mViewDragHelper.shouldInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mViewDragHelper.processTouchEvent(event);
		return true;
	}

	private ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
		@Override
		public boolean tryCaptureView(View child, int pointerId) {
			return false;
		}

		/**
		 * 松开手时触发
		 */
		@Override
		public void onViewReleased(View releasedChild, float xvel, float yvel) {
			super.onViewReleased(releasedChild, xvel, yvel);
			int left = releasedChild.getLeft();
			if (left <= mSlideExitWidth) {
				//小于设定距离时划回
				mViewDragHelper.settleCapturedViewAt(0, 0);
			} else {
				mViewDragHelper.settleCapturedViewAt(mScreenHeight, 0);
			}
			invalidate();
		}

		@Override
		public void onEdgeDragStarted(int edgeFlags, int pointerId) {
			//边缘触发 捕捉RootView
			mViewDragHelper.captureChildView(mRootView, pointerId);
		}

		@Override
		public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
			super.onViewPositionChanged(changedView, left, top, dx, dy);
			mCurSildeWidth = left;
			invalidate();
			if (changedView == mRootView && left >= mScreenWidth) {

				mActivity.finish();
				mActivity.overridePendingTransition(0, 0);
			}
		}

		@Override
		public int clampViewPositionHorizontal(View child, int left, int dx) {
			left = left >= 0 ? left : 0;
			return left;
		}

		@Override
		public int clampViewPositionVertical(View child, int top, int dy) {
			return super.clampViewPositionVertical(child, top, dy);
		}
	};

	/**
	 * 使用settleCapturedViewAt()是必须调用此函数
	 */
	@Override
	public void computeScroll() {
		//滚动期间 不断刷新ViewGroup
		if (mViewDragHelper.continueSettling(true)) {
			invalidate();
		}
	}

	public void bind(Context context) {
		mActivity = (Activity) context;
		mDecorView = (ViewGroup) mActivity.getWindow().getDecorView();
		mRootView = mDecorView.getChildAt(0);
		mDecorView.removeView(mRootView);
		this.addView(mRootView);
		mDecorView.addView(this);
	}
}
