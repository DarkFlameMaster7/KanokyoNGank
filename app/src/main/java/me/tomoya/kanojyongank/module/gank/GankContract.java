package me.tomoya.kanojyongank.module.gank;

import me.tomoya.kanojyongank.base.BasePresenter;
import me.tomoya.kanojyongank.base.BaseView;
import me.tomoya.kanojyongank.module.main.MainContract;

/**
 * Created by piper on 17-2-7.
 */

public interface GankContract {
	interface View extends BaseView<MainContract.Presenter> {
	}

	interface Presenter extends BasePresenter {

	}
}
