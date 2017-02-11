package me.tomoya.kanojyongank.base;

/**
 * Created by piper on 17-2-10.
 */

public interface BaseContract {
	interface Presenter<T extends BaseContract.View> {
		void attachView(T view);

		void detachView();
	}

	interface View {

		void useNightMode();
	}
}
