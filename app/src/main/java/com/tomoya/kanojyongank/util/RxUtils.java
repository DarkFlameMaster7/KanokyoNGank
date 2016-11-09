package com.tomoya.kanojyongank.util;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Tomoya-Hoo on 2016/11/3.
 */

public class RxUtils {

    /**
     * io程订阅  ui线程观察
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<T,T> normalSchedulers(){
        return new Observable.Transformer<T, T>(){
            @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
