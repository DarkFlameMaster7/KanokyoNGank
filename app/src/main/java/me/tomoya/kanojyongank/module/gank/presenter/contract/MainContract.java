package me.tomoya.kanojyongank.module.gank.presenter.contract;

import java.util.List;
import me.tomoya.kanojyongank.base.BaseContract;
import me.tomoya.kanojyongank.bean.Kanojyo;
import me.tomoya.kanojyongank.module.gank.ui.ShowActivity;

/**
 * Created by piper on 17-2-7.
 */

public interface MainContract {

	interface View extends BaseContract.View {
		ShowActivity getActivcity();

		void fetchData(List<Kanojyo> dataList);
	}

	interface Presenter extends BaseContract.Presenter<MainContract.View> {
		void getDailyData();
	}
}
