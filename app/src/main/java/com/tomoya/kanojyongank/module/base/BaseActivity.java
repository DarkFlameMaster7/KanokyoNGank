package com.tomoya.kanojyongank.module.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;

import com.tomoya.kanojyongank.network.KanojyoRetrofit;
import com.tomoya.kanojyongank.network.api.GankApi;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class BaseActivity extends RxAppCompatActivity {
    public static GankApi gankApi = new KanojyoRetrofit().getGankApi();
    CompositeSubscription compositeSubscription;
    public Subscription subscription;

    public boolean mCanSlideExit = false;

//    public abstract void initData();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        //translucent statusbar
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
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


}
