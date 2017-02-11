package me.tomoya.kanojyongank.module.gank.presenter.contract;

import me.tomoya.kanojyongank.base.BaseContract;

/**
 * Created by piper on 17-2-7.
 */

public interface KanoContract {
	interface View extends BaseContract.View {

	}

	interface Presenter extends BaseContract.Presenter<KanoContract.View> {

	}
}
