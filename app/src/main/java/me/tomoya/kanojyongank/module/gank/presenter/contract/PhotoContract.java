package me.tomoya.kanojyongank.module.gank.presenter.contract;

import me.tomoya.kanojyongank.base.BaseContract;

/**
 * Created by piper on 17-2-17.
 */

public interface PhotoContract {
	interface View extends BaseContract.View{

	}
	interface Presenter extends BaseContract.Presenter<PhotoContract.View>{
		void savePhoto(String imgUrl);
	}
}
