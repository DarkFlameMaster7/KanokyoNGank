package me.tomoya.kanojyongank.di.components;

import android.app.Application;
import android.content.SharedPreferences;
import dagger.Component;
import javax.inject.Singleton;
import me.tomoya.kanojyongank.di.modules.AppModule;
import me.tomoya.kanojyongank.di.modules.NetModule;
import me.tomoya.kanojyongank.network.RetrofitWrapper;

/**
 * Created by piper on 17-2-7.
 */
@Singleton
@Component(modules = {AppModule.class, NetModule.class})
public interface AppComponent {

	Application getApplication();

	RetrofitWrapper getRetrofitWrapper();

	SharedPreferences getSharedPreference();
}
