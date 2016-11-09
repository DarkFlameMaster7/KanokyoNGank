package com.tomoya.kanojyongank.module.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;

import com.tomoya.kanojyongank.network.KanojyoRetrofit;
import com.tomoya.kanojyongank.network.api.GankApi;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class BaseActivity extends RxAppCompatActivity {
    public static GankApi gankApi = new KanojyoRetrofit().getGankApi();
    CompositeSubscription compositeSubscription;
    public Subscription subscription;

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
        return super.onTouchEvent(event);
    }
}
