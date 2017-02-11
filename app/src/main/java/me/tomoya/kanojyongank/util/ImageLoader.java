package me.tomoya.kanojyongank.util;

import android.content.res.Resources;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.util.Log;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import me.tomoya.kanojyongank.widget.fresco.ImgScaleType;

/**
 * Created by piper on 17-2-10.
 */

public class ImageLoader {
	public static void loadImageWithProgress(SimpleDraweeView simpleDraweeView, String imgUrl,
			Resources resources) {
		Uri uri = Uri.parse(imgUrl);
		//imgVpKanojyo.setImageURI(uri);

		//设置Drawee属性 其内部mvc实现 https://www.fresco-cn.org/docs
		GenericDraweeHierarchyBuilder builder = GenericDraweeHierarchyBuilder.newInstance(resources);
		GenericDraweeHierarchy draweeHierarchy = builder.setProgressBarImage(new ProgressBarDrawable())
				.setActualImageScaleType(new ImgScaleType())
				.setFadeDuration(300)
				.build();

		ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
			@Override
			public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
				super.onFinalImageSet(id, imageInfo, animatable);
			}

			@Override
			public void onFailure(String id, Throwable throwable) {
				super.onFailure(id, throwable);
				Log.d("imageloader", "onFailure: " + "id:" + id + "Reason:" + throwable);
			}
		};
		DraweeController controller = Fresco.newDraweeControllerBuilder()
				.setControllerListener(controllerListener)
				.setTapToRetryEnabled(true)
				.setUri(uri)
				.build();
		simpleDraweeView.setController(controller);
		simpleDraweeView.setHierarchy(draweeHierarchy);
	}
}
