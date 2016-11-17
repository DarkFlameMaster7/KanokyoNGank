package com.tomoya.kanojyongank.widget;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by Tomoya-Hoo on 2016/11/17.
 * 实现边缘滑动关闭Activity
 * 获取并删除DecorView的RootView 将其添加至当前View 再添加至DecorView
 */

public class SlideLayout extends FrameLayout {
    /*
    * 边缘事件 滚动处理*/
    private ViewDragHelper mViewDragHelper;
    private ViewGroup mDecorView;
    private View mRootView;
    /*
    * 要绑定的Activity*/
    private Activity mActivity;
    /*
    * 屏幕宽高*/
    private int mScreenHeight, mScreenWidth;
    /*
    * 滑动退出距离*/
    private int mSlideExitWidth;
    /*
    * 当前滑动距离*/
    private int mCurSildeWidth;

    public SlideLayout(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mActivity = (Activity) context;
        mViewDragHelper = ViewDragHelper.create(this, callback);
        mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }

    private ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return false;
        }

        /**
         * 松开手时触发
         * @param releasedChild
         * @param xvel
         * @param yvel
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
            mViewDragHelper.captureChildView(mRootView,pointerId);
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            mCurSildeWidth = left;
            if (changedView == mRootView && left >= mScreenWidth) {
                mActivity.finish();
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

}
