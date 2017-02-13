package me.tomoya.kanojyongank.di.modules;

import android.app.Activity;
import android.content.Context;
import dagger.Module;
import dagger.Provides;
import me.tomoya.kanojyongank.di.scopes.GScope;

/**
 * Created by piper on 17-2-10.
 */
@Module
public class ActivityModule {
	private Activity mActivity;

	public ActivityModule(Activity activity) {
		this.mActivity = activity;
	}

	@Provides
	@GScope
	public Activity provideActivity() {
		return mActivity;
	}
	@Provides
	@GScope
	public Context provideContext() {
		return mActivity;
	}
}
