package me.tomoya.kanojyongank.module.gank.presenter;

import android.net.Uri;
import android.util.Log;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import javax.inject.Inject;
import me.tomoya.kanojyongank.base.RxPresenter;
import me.tomoya.kanojyongank.module.gank.presenter.contract.PhotoContract;
import me.tomoya.kanojyongank.network.RetrofitWrapper;
import rx.Subscription;

/**
 * Created by piper on 17-2-17.
 */

public class PhotoPresenter extends RxPresenter<PhotoContract.View>
		implements PhotoContract.Presenter {
	private Subscription    subscription;
	private RetrofitWrapper mRetrofitWrapper;
	@Inject
	public PhotoPresenter(RetrofitWrapper retrofitWrapper) {
		this.mRetrofitWrapper = retrofitWrapper;
	}

	@Override
	public void savePhoto(String imgUrl) {
		ImagePipeline imagePipeLine = Fresco.getImagePipeline();
		Uri uri = Uri.parse(imgUrl);
		boolean isInCache = imagePipeLine.isInBitmapMemoryCache(uri);
		Log.e("save", "savePhoto: "+isInCache);

		//if (subscription!=null) {
		//	subscription.unsubscribe();
		//}
		//subscription = mRetrofitWrapper.downloadImageFromNew(imgUrl)
		//		.compose(RxUtils.<ResponseBody>normalSchedulers())
		//		.map(new Func1<ResponseBody, Bitmap>() {
		//			@Override
		//			public Bitmap call(ResponseBody responseBody) {
		//
		//				return null;
		//			}
		//		})
		//		.subscribe();
	}
}
