package me.tomoya.kanojyongank;

import android.app.Application;
import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import me.tomoya.kanojyongank.di.components.AppComponent;
import me.tomoya.kanojyongank.di.components.DaggerAppComponent;
import me.tomoya.kanojyongank.di.modules.AppModule;
import me.tomoya.kanojyongank.di.modules.NetModule;

/**
 * Created by piper on 17-1-8.
 */

public class GankApplication extends Application {
	private static GankApplication instance;
	public static  AppComponent    appComponent;

	@Override public void onCreate() {
		super.onCreate();
		instance = this;

		appComponent = DaggerAppComponent.builder()
				.appModule(new AppModule(instance))
				.netModule(new NetModule())
				.build();
		initFresco();
	}

	public static AppComponent getAppComponent() {
		if (appComponent == null) {
			appComponent = DaggerAppComponent.builder()
					.appModule(new AppModule(instance))
					.netModule(new NetModule())
					.build();
		}
		return appComponent;
	}

	public static synchronized GankApplication getInstance() {
		return instance;
	}

	/**
	 * initialize fresco
	 */
	private void initFresco() {
		DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(getApplicationContext())
				.setMaxCacheSize(100 * 1024 * 1024)
				.build();
		ImagePipelineConfig imagePipelineConfig = ImagePipelineConfig.newBuilder(this)
				.setMainDiskCacheConfig(diskCacheConfig)
				.build();
		Fresco.initialize(getApplicationContext(), imagePipelineConfig);
	}
}
