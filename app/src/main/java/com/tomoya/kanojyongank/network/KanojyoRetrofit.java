package com.tomoya.kanojyongank.network;

import com.tomoya.kanojyongank.network.api.GankApi;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Tomoya-Hoo on 2016/10/13.
 */

public class KanojyoRetrofit {
    private OkHttpClient okHttpClient;
    private Converter.Factory converterFactory = GsonConverterFactory.create();
    private CallAdapter.Factory callAdapterFactory = RxJavaCallAdapterFactory.create();
    private GankApi gankApi;

    public KanojyoRetrofit() {
        HttpLoggingInterceptor log = new HttpLoggingInterceptor();
        log.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient = new OkHttpClient.Builder().addInterceptor(log).build();
        if (gankApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("http://gank.io/api/")
                    .addConverterFactory(converterFactory)
                    .addCallAdapterFactory(callAdapterFactory)
                    .build();

            gankApi = retrofit.create(GankApi.class);
        }
    }

    public GankApi getGankApi() {
        return gankApi;
    }
}
