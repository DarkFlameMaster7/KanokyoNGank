package me.tomoya.kanojyongank.base;

/**
 * Created by piper on 17-2-7.
 */

public interface BaseView<T extends BasePresenter> {
	void setPresenter(T presenter);
}
