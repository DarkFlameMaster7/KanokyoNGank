package me.tomoya.kanojyongank.di.modules;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

/**
 * Created by piper on 17-2-7.
 */
@Module
public class AppModule {
	private Application mApplication;

	public AppModule(Application application) {
		this.mApplication = application;
	}

	@Singleton
	@Provides
	Application provideApplication() {
		return mApplication;
	}

	@Singleton
	@Provides
	SharedPreferences provideSharePreferences() {
		return PreferenceManager.getDefaultSharedPreferences(mApplication);
	}
}
