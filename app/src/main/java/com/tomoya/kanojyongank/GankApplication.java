package com.tomoya.kanojyongank;

import android.app.Application;
import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Supplier;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

/**
 * Created by piper on 17-1-8.
 */

public class GankApplication extends Application {
    @Override public void onCreate() {
        super.onCreate();
        initFresco();
    }

    /**
     * initialize fresco
     */
    private void initFresco() {
        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(getApplicationContext())
                                                         .setMaxCacheSize(100 * 1024 * 1024)
                                                         .build();
        ImagePipelineConfig imagePipelineConfig = ImagePipelineConfig.newBuilder(this)
                                                                     .setMainDiskCacheConfig(
                                                                             diskCacheConfig)
                                                                     .build();
        Fresco.initialize(getApplicationContext(), imagePipelineConfig);
    }
}
