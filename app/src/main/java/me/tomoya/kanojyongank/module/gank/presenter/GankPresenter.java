package me.tomoya.kanojyongank.module.gank.presenter;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import me.tomoya.kanojyongank.base.RxPresenter;
import me.tomoya.kanojyongank.bean.Gank;
import me.tomoya.kanojyongank.bean.GankData;
import me.tomoya.kanojyongank.module.gank.presenter.contract.GankContract;
import me.tomoya.kanojyongank.network.RetrofitWrapper;
import me.tomoya.kanojyongank.util.RxUtils;
import rx.Observer;
import rx.Subscription;

/**
 * Created by piper on 17-2-10.
 */

public class GankPresenter extends RxPresenter<GankContract.View>
		implements GankContract.Presenter {
	private RetrofitWrapper mRetrofitWrapper;

	@Inject
	public GankPresenter(RetrofitWrapper retrofitWrapper) {
		this.mRetrofitWrapper = retrofitWrapper;
	}


	@Override
	public void getGankData(int year, int month, int day) {
		Subscription subscription = mRetrofitWrapper.fetchGankData(year, month, day)
				//.map(new Func1<GankData, List<Gank>>() {
				//	@Override
				//	public List<Gank> call(GankData gankData) {
				//		return addAllData2List(gankData.results);
				//	}
				//})
				//.compose(this.<List<Gank>>bindToLifecycle())
				.compose(RxUtils.<GankData>normalSchedulers())
				.subscribe(new Observer<GankData>() {
					@Override
					public void onCompleted() {

					}

					@Override
					public void onError(Throwable e) {
						Log.e("Gank", "onError: " + e.toString());
					}

					@Override
					public void onNext(GankData gankData) {
						mView.fetchData(gankData);
					}
				});
		addSubscription(subscription);
	}

	private List<Gank> addAllData2List(GankData.Result results) {
		List<Gank> ganks = new ArrayList<>();
		if (results.androidList != null) ganks.addAll(results.androidList);
		if (results.iOSList != null) ganks.addAll(results.iOSList);
		if (results.appList != null) ganks.addAll(results.appList);
		if (results.拓展资源List != null) ganks.addAll(results.拓展资源List);
		if (results.瞎推荐List != null) ganks.addAll(results.瞎推荐List);
		if (results.休息视频List != null) ganks.addAll(0, results.休息视频List);
		return ganks;
	}
}
