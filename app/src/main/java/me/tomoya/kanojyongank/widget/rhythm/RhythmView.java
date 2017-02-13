package me.tomoya.kanojyongank.widget.rhythm;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import me.tomoya.kanojyongank.R;

import static android.content.ContentValues.TAG;

/**
 * Created by Tomoya-Hoo on 2016/10/25.
 */

public class RhythmView extends HorizontalScrollView {
	private RhythmViewAdapter mAdapter;

	/*
	* Item宽度*/
	private float mItemWidth;
	/*
	* 屏幕宽度
	* */
	private int   mScreenWidth;
	/*
	* 当前item位置
	* */
	private int   mCurrentItemPosition;
	/*
	* 上次选中位置
	* */
	private int   mLastDisplayItemPosition;
	/*
	* ScrollView滚动动画延迟执行的时间
	**/
	private int   mScrollStartDelayTime;
	/*
	* item上下位移单位*/
	private int   mIntervalHeight;
	/*
	* 上下位移的最大高度
	* */
	private int   mMaxTranslationHeight;
	/*
	* 横向位移距离
	* */
	private int   mEdgeSizeForShiftRhythm;
	private long  mFingerDownTime;

	private ShiftMonitorTimer mTimer;

	private Context              mContext;
	private LinearLayout         mLinearLayout;
	private Handler              mHandler;
	private IChangePagerListener mListener;

	public RhythmView(Context context) {
		super(context);
	}

	public RhythmView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	public RhythmView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	private void init() {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		mScreenWidth = displayMetrics.widthPixels;
		//item宽度为1/7个屏幕大小
		mItemWidth = mScreenWidth / 7;
		mCurrentItemPosition = -1;
		mLastDisplayItemPosition = -1;
		mMaxTranslationHeight = (int) mItemWidth;
		mIntervalHeight = mMaxTranslationHeight / 6;
		mEdgeSizeForShiftRhythm = getResources().getDimensionPixelSize(
				R.dimen.rhythm_edge_size_for_shift);
		mScrollStartDelayTime = 0;
		mHandler = new Handler();
		mTimer = new ShiftMonitorTimer();
		mTimer.startMonitor();
	}

	public void setListener(IChangePagerListener listener) {
		this.mListener = listener;
	}

	public float getItemWidth() {
		return mItemWidth;
	}

	private View getItemView(int position) {
		return mLinearLayout.getChildAt(position);
	}

	public void invalidateData() {
		int childCount = this.mLinearLayout.getChildCount();
		if (childCount < this.mAdapter.getCount()) {
			for (int i = childCount; i < this.mAdapter.getCount(); i++)
				this.mLinearLayout.addView(this.mAdapter.getView(i, null, null));
		}
	}

	public void setAdapter(RhythmViewAdapter adapter) {
		mAdapter = adapter;
		if (mLinearLayout == null) {
			mLinearLayout = (LinearLayout) getChildAt(0);
		}
		mAdapter.setItemWidth(mItemWidth);
		for (int i = 0; i < mAdapter.getCount(); i++) {
			mLinearLayout.addView(mAdapter.getView(i, null, null));
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
			case MotionEvent.ACTION_MOVE:
				mTimer.monitorTouchPosition(ev.getX(), ev.getY());
				updateItemHeight(ev.getX());
				break;
			case MotionEvent.ACTION_DOWN://按下
				mTimer.monitorTouchPosition(ev.getX(), ev.getY());
				//得到按下时的时间戳
				mFingerDownTime = System.currentTimeMillis();
				//更新钢琴按钮的高度
				updateItemHeight(ev.getX());
				break;
			case MotionEvent.ACTION_UP://抬起
				actionUp();
				break;
		}
		return true;
	}

	/**
	 * 琴键落
	 */
	private void actionUp() {
		mTimer.monitorTouchPosition(-1, -1);
		if (mCurrentItemPosition < 0) {
			return;
		}
		int firstVisiblePosition = getFirstVisibleItemPosition();
		int lastVisiblePosition = firstVisiblePosition + mCurrentItemPosition;
		final List<View> list = getVisibleViews();
		int size = list.size();
		//移除当前琴键
		if (size > mCurrentItemPosition) {
			list.remove(mCurrentItemPosition);
		}
		if (firstVisiblePosition - 1 >= 0) {
			list.add(mLinearLayout.getChildAt(firstVisiblePosition - 1));
		}
		if (lastVisiblePosition + 1 <= mLinearLayout.getChildCount()) {
			list.add(mLinearLayout.getChildAt(lastVisiblePosition + 1));
		}
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				for (View view : list) {
					itemFallenAnime(view, true);
				}
			}
		}, 20L);
		if (mListener != null) {
			mListener.onSelected(lastVisiblePosition);
		}
		mCurrentItemPosition = -1;
	}

	/**
	 * 更新琴键高度
	 */
	private void updateItemHeight(float scrollX) {
		//得到屏幕上可见的7个小图标的视图
		List viewList = getVisibleViews();
		//当前手指所在的item
		int position = (int) (scrollX / mItemWidth);
		//如果手指位置没有发生变化或者大于childCount的则跳出方法不再继续执行
		if (position == mCurrentItemPosition || position >= mLinearLayout.getChildCount()) {
			return;
		}
		mCurrentItemPosition = position;
		makeItems(position, viewList);
	}

	/**
	 * 计算出每个琴键高度
	 */
	private void makeItems(int position, List<View> viewList) {
		if (position >= viewList.size()) {
			return;
		}
		int size = viewList.size();
		for (int i = 0; i < size; i++) {
			//根据小图标的位置计算出在Y轴需要位移的大小

			int translationY = Math.min(Math.max(Math.abs(position - i) * mIntervalHeight, 10),
					mMaxTranslationHeight);
			//位移动画
			updateItemHeightAnimator(viewList.get(i), translationY);
		}
	}

	/**
	 * 更新琴键高度动画
	 */
	private void updateItemHeightAnimator(View view, int translationY) {
		if (view != null) {
			AnimatorUtils.showUpAndDownBounce(view, translationY, 180, true, true);
		}
	}

	/**
	 * 琴键回落
	 */
	private Animator itemFallenAnime(View view, boolean isStart) {
		if (view != null) {
			return AnimatorUtils.showUpAndDownBounce(view, mMaxTranslationHeight, 500, isStart, true);
		}
		return null;
	}

	/**
	 * 获取可见的第一个item的位置
	 */
	private int getFirstVisibleItemPosition() {
		if (mLinearLayout == null) {
			return 0;
		}
		int size = mLinearLayout.getChildCount();
		for (int i = 0; i < size; i++) {
			//当出现钢琴按钮的x轴比当前ScrollView的x轴大时，这个钢琴按钮就是当前可见的第一个
			if (((float) getScrollX()) < this.mLinearLayout.getChildAt(i).getX() + (this.mItemWidth
					/ 2.0f)) {
				return i;
			}
		}
		return 0;
	}

	/*
	* 获取当前可见的7个view
	* */
	private List<View> getVisibleViews() {
		ArrayList<View> list = new ArrayList<>();
		if (mLinearLayout == null) {
			return list;
		}
		int firstVisibleView = getFirstVisibleItemPosition();
		int lastVisibleView = firstVisibleView + 7;

		if (mLinearLayout.getChildCount() < 7) {
			lastVisibleView = mLinearLayout.getChildCount();
		}

		for (int i = firstVisibleView; i < lastVisibleView; i++)
			list.add(mLinearLayout.getChildAt(i));
		return list;
	}

	/**
	 * 获取当前可见的7个和前一个和后一个
	 */
	private List<View> getVisibleViews(boolean isForward, boolean isBackward) {
		ArrayList<View> list = new ArrayList<>();
		if (mLinearLayout == null) {
			return list;
		}
		int firstPosition = getFirstVisibleItemPosition();
		int lastPosition = firstPosition + 7;
		if (mLinearLayout.getChildCount() < 7) {
			lastPosition = mLinearLayout.getChildCount();
		}
		if (isForward && firstPosition > 0) {
			firstPosition--;
		}
		if (isBackward && lastPosition < mLinearLayout.getChildCount()) {
			lastPosition++;
		}
		for (int i = firstPosition; i < lastPosition; i++) {
			list.add(mLinearLayout.getChildAt(i));
		}
		return list;
	}

	private Animator scrollToPosition(int position, int duration, int startDelay, boolean isStart) {
		if (mLinearLayout.getChildCount() > 0) {
			int viewX = (int) mLinearLayout.getChildAt(position).getX();
			return smoothScrollX(viewX, duration, startDelay, isStart);
		}
		return null;
	}

	/**
	 * @param duration 动画持续时间
	 * @param startDelay 延迟动画开始时间
	 * @param isStart 是否开始
	 */
	private Animator smoothScrollX(int position, int duration, int startDelay, boolean isStart) {
		return AnimatorUtils.moveScrollViewToX(this, position, duration, startDelay, isStart);
	}

	private Animator itemBounceUpAnimator(View view, boolean isStart) {
		if (view != null) {
			return AnimatorUtils.showUpAndDownBounce(view, 10, 350, isStart, true);
		}
		return null;
	}

	/**
	 * 提供给外部 移动到所选的位置
	 */
	public void moveToSelectedPosition(int position) {
		if (mLinearLayout.getChildCount() <= 0) {
			Log.e(TAG, "moveToSelectedPosition: 跳出");
			return;
		}
		if (mLastDisplayItemPosition == position) {
			return;
		}
		//ScrollView 位移
		Animator scrollAnimator;
		//item下降动画
		Animator itemFallenAnimator;
		//item弹起动画
		Animator itemBounceUpAnimator;
		//ScrollView 滑动
		if ((this.mLastDisplayItemPosition < 0) || (mAdapter.getCount() <= 7) || (position <= 3)) {//前三个
			scrollAnimator = scrollToPosition(0, 300, mScrollStartDelayTime, false);
		} else if (mAdapter.getCount() - position <= 3) {//后三个
			scrollAnimator = scrollToPosition(mAdapter.getCount() - 7, 300, mScrollStartDelayTime, false);
		} else {
			scrollAnimator = scrollToPosition(position - 3, 300, mScrollStartDelayTime, false);
		}
		itemBounceUpAnimator = itemBounceUpAnimator(getItemView(position), false);

		itemFallenAnimator = itemFallenAnime(getItemView(mLastDisplayItemPosition), false);

		AnimatorSet bounceNFallen = new AnimatorSet();
		if (itemFallenAnimator != null) {
			bounceNFallen.playTogether(itemFallenAnimator);
		}
		if (itemBounceUpAnimator != null) {
			bounceNFallen.playTogether(itemBounceUpAnimator);
		}
		AnimatorSet all = new AnimatorSet();
		all.playSequentially(new Animator[] {scrollAnimator, bounceNFallen});
		all.start();
		mLastDisplayItemPosition = position;
	}

	class ShiftMonitorTimer extends Timer {
		private TimerTask timerTask;
		private boolean canShift = false;
		private float x;
		private float y;

		void monitorTouchPosition(float x, float y) {
			this.x = x;
			this.y = y;
			//当按下位置在第一个后最后一个，或x<0,y<0时，canShift为false，使计时器线程中的代码不能执行
			if ((x < 0.0F) || ((x > mEdgeSizeForShiftRhythm) && (x
					< mScreenWidth - mEdgeSizeForShiftRhythm)) || (y < 0.0F)) {
				mFingerDownTime = System.currentTimeMillis();
				this.canShift = false;
			} else {
				this.canShift = true;
			}
		}

		void startMonitor() {
			if (this.timerTask == null) {
				timerTask = new TimerTask() {
					@Override
					public void run() {
						long duration = System.currentTimeMillis() - mFingerDownTime;
						//按下时间大于1秒，且按下的是第一个或者最后一个时为true
						if (canShift && duration > 1000) {
							int firstPosition = getFirstVisibleItemPosition();
							int toPosition = 0; //要移动到的小图标的位置
							boolean isForward = false; //是否获取第firstPosition-1个小图标
							boolean isBackward = false;//是否获取第lastPosition+1个小图标
							final List<View> localList;
							if (x <= mEdgeSizeForShiftRhythm && x >= 0.0F) {//第一个
								if (firstPosition - 1 >= 0) {
									mCurrentItemPosition = 0;
									toPosition = firstPosition - 1;
									isForward = true;
									isBackward = false;
								}
							} else if (x > mScreenWidth - mEdgeSizeForShiftRhythm) {//最后一个
								if (mLinearLayout.getChildCount() >= 1 + (firstPosition + 7)) {
									mCurrentItemPosition = 7;
									toPosition = firstPosition + 1;
									isForward = false;
									isBackward = true;
								}
							}
							//当按下的是第一个的时候isForward为true，最后一个时isBackward为true
							if (isForward || isBackward) {
								localList = getVisibleViews(isForward, isBackward);
								final int finalToPosition = toPosition;
								mHandler.post(new Runnable() {
									public void run() {
										makeItems(mCurrentItemPosition, localList);//设置每个Item的高度
										scrollToPosition(finalToPosition, 200, 0, true);//设置ScrollView在x轴的坐标
									}
								});
							}
						}
					}
				};
			}
			//200毫秒之后开始执行，每隔250毫秒执行一次
			schedule(timerTask, 200L, 250L);
		}
	}
}
