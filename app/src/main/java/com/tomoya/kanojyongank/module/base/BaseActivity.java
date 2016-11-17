package com.tomoya.kanojyongank.module.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;

import com.tomoya.kanojyongank.network.KanojyoRetrofit;
import com.tomoya.kanojyongank.network.api.GankApi;
import com.tomoya.kanojyongank.util.ScreenUtils;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class BaseActivity extends RxAppCompatActivity {
    int mStartX, mLastX;
    public static GankApi gankApi = new KanojyoRetrofit().getGankApi();
    CompositeSubscription compositeSubscription;
    public Subscription subscription;
    public boolean mCanScrollClose = false;

//    public abstract void initData();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public CompositeSubscription getCompositeSubscription() {
        if (compositeSubscription == null) {
            compositeSubscription = new CompositeSubscription();
        }
        return compositeSubscription;
    }

    public void addSubscription(Subscription subscription) {
        if (compositeSubscription == null) {
            compositeSubscription = new CompositeSubscription();
        }
            compositeSubscription.add(subscription);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compositeSubscription != null)
            compositeSubscription.unsubscribe();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = (int) event.getRawX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = x;
                return true;
            case MotionEvent.ACTION_MOVE:
                mLastX = x;
                return true;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                Log.e("Test", "onTouchEvent: "+(mLastX - mStartX));
                if (mCanScrollClose&&(mLastX - mStartX) > ScreenUtils.getScreenWidth(this)/3) {
                    Log.e("Test", "onTouchEvent: in");
                    this.finish();
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}
