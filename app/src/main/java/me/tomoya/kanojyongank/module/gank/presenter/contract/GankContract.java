package me.tomoya.kanojyongank.module.gank.presenter.contract;

import java.util.List;
import me.tomoya.kanojyongank.base.BaseContract;
import me.tomoya.kanojyongank.bean.Gank;

/**
 * Created by piper on 17-2-7.
 */

public interface GankContract {
	interface View extends BaseContract.View {
		void fetchData(List<Gank> dataList);
	}

	interface Presenter extends BaseContract.Presenter<GankContract.View> {
		void getGankData();
	}
}
