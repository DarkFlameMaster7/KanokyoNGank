package me.tomoya.kanojyongank.di.modules;

import dagger.Module;
import dagger.Provides;
import javax.inject.Scope;
import me.tomoya.kanojyongank.network.api.GankApi;
import retrofit2.Retrofit;

/**
 * Created by piper on 17-2-7.
 */
@Module
public class ApiModule {
	Retrofit retrofit;

	public ApiModule(Retrofit retrofit) {
		this.retrofit = retrofit;
	}

	@Provides
	GankApi providesGankApi() {
		return retrofit.create(GankApi.class);
	}
}
