package me.tomoya.kanojyongank.network;

import java.io.File;
import me.tomoya.kanojyongank.BuildConfig;
import me.tomoya.kanojyongank.bean.GankData;
import me.tomoya.kanojyongank.bean.KanojyoData;
import me.tomoya.kanojyongank.bean.ShortFilmData;
import me.tomoya.kanojyongank.common.GContants;
import me.tomoya.kanojyongank.network.api.GankApi;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by piper on 17-2-8.
 */

public class RetrofitWrapper {
	private OkHttpClient mOkHttpClient;
	private GankApi      mGankApi;

	public RetrofitWrapper() {
		init();
	}

	private void init() {
		initOkHttp();
		mGankApi = getApiService(GankApi.BASE, GankApi.class);
	}

	private void initOkHttp() {
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		if (BuildConfig.DEBUG) {
			HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
			builder.addInterceptor(logInterceptor);
		}
		int cacheSize = 20 * 1024 * 1024;

		File  cacheFile = new File(GContants.PATH_CACHE);
		Cache cache     = new Cache(cacheFile, cacheSize);

		builder.cache(cache);
		builder.retryOnConnectionFailure(true);
		mOkHttpClient = builder.build();
	}

	/**
	 *
	 * @param baseUrl
	 * @param clz
	 * @param <T>
	 * @return
	 */
	private <T> T getApiService(String baseUrl, Class<T> clz) {
		Retrofit retrofit = new Retrofit.Builder().client(mOkHttpClient)
				.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
				.addConverterFactory(GsonConverterFactory.create())
				.baseUrl(baseUrl)
				.build();
		return retrofit.create(clz);
	}

	public Observable<KanojyoData> fetchKanojoData(int page) {
		return mGankApi.getKanojyoData(page);
	}

	public Observable<GankData> fetchGankData(int year, int month, int day) {
		return mGankApi.getGankData(year, month, day);
	}

	public Observable<ShortFilmData> fetchShortFilmData(int page) {
		return mGankApi.getShortFilmData(page);
	}
}
