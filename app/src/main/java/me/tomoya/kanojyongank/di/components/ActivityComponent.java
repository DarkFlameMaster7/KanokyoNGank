package me.tomoya.kanojyongank.di.components;

import android.app.Activity;
import android.content.Context;
import dagger.Component;
import me.tomoya.kanojyongank.di.modules.ActivityModule;
import me.tomoya.kanojyongank.di.scopes.GScope;
import me.tomoya.kanojyongank.module.gank.ui.GankActivity;
import me.tomoya.kanojyongank.module.gank.ui.ShowActivity;
import me.tomoya.kanojyongank.module.gank.ui.WebActivity;
import me.tomoya.kanojyongank.module.splash.SplashActivity;

/**
 * Created by piper on 17-2-10.
 */
@GScope
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
	Activity getActivity();

	Context getContext();

	void inject(ShowActivity showActivity);

	void inject(GankActivity showActivity);

	void inject(WebActivity showActivity);

	void inject(SplashActivity showActivity);
}
