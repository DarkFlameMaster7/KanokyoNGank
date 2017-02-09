package me.tomoya.kanojyongank.di.modules;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import me.tomoya.kanojyongank.network.RetrofitWrapper;

/**
 * Created by piper on 17-2-7.
 */
@Module
public class NetModule {

	public NetModule() {
	}

	@Singleton
	@Provides
	RetrofitWrapper provideRetrofit() {
		return new RetrofitWrapper();
	}
}
