package me.tomoya.kanojyongank.module.gank.presenter;

import android.util.Log;
import java.util.List;
import javax.inject.Inject;
import me.tomoya.kanojyongank.base.RxPresenter;
import me.tomoya.kanojyongank.bean.Kanojyo;
import me.tomoya.kanojyongank.bean.KanojyoData;
import me.tomoya.kanojyongank.bean.ShortFilmData;
import me.tomoya.kanojyongank.module.gank.presenter.contract.MainContract;
import me.tomoya.kanojyongank.network.RetrofitWrapper;
import me.tomoya.kanojyongank.util.RxUtils;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by piper on 17-2-10.
 */

public class MainPresenter extends RxPresenter<MainContract.View>
		implements MainContract.Presenter {
	private RetrofitWrapper mRetrofitWrapper;

	@Inject
	public MainPresenter(RetrofitWrapper retrofitWrapper) {
		mRetrofitWrapper = retrofitWrapper;
		getDailyData();
	}

	@Override
	public void getDailyData() {
		Subscription subscription = Observable.zip(mRetrofitWrapper.fetchKanojoData(1),
				mRetrofitWrapper.fetchShortFilmData(1), new Func2<KanojyoData, ShortFilmData, KanojyoData>() {
					@Override
					public KanojyoData call(KanojyoData kanojyoData, ShortFilmData shortFilmData) {
						return reconstructKanojyoData(kanojyoData, shortFilmData);
					}
				})
				.map(new Func1<KanojyoData, List<Kanojyo>>() {
					@Override
					public List<Kanojyo> call(KanojyoData kanojyoData) {
						return kanojyoData.results;
					}
				})
				//.compose((mView.getActivcity()).<List<Kanojyo>>bindToLifecycle())
				.compose(RxUtils.<List<Kanojyo>>normalSchedulers())
				.subscribe(new Observer<List<Kanojyo>>() {
					@Override
					public void onCompleted() {
						// TODO: 17-1-12 add refresh UI
					}

					@Override
					public void onError(Throwable e) {
						Log.e("Rx", "onError: " + e);
					}

					@Override
					public void onNext(List<Kanojyo> kanojyos) {
						mView.fetchData(kanojyos);
						System.out.printf(""+kanojyos.toString());
					}
				});
		addSubscription(subscription);
	}

	/**
	 * 添加颜色属性
	 */
	private KanojyoData reconstructKanojyoData(KanojyoData kanojyoData, ShortFilmData shortFilmData) {
		for (int i = 0; i < kanojyoData.results.size(); i++) {
			Kanojyo kanojyo = kanojyoData.results.get(i);
			kanojyo.color = (int) -(Math.random() * (16777216 - 1) + 1);
			kanojyo.desc = shortFilmData.results.get(i).desc;
			kanojyo.video_url = shortFilmData.results.get(i).url;
		}
		return kanojyoData;
	}
}
