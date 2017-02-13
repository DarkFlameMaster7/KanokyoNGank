package me.tomoya.kanojyongank.module.gank.presenter.contract;

import me.tomoya.kanojyongank.base.BaseContract;
import me.tomoya.kanojyongank.bean.GankData;

/**
 * Created by piper on 17-2-7.
 */

public interface GankContract {
	interface View extends BaseContract.View {
		void fetchData(GankData data);
	}

	interface Presenter extends BaseContract.Presenter<GankContract.View> {

		void getGankData(int year, int month, int day);
	}
}
