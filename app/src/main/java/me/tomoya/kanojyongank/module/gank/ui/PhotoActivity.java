package me.tomoya.kanojyongank.module.gank.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;
import butterknife.BindView;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.DraweeTransition;
import com.facebook.drawee.view.SimpleDraweeView;
import me.tomoya.kanojyongank.R;
import me.tomoya.kanojyongank.annotation.PropertiesInject;
import me.tomoya.kanojyongank.base.SimpleActivity;
import me.tomoya.kanojyongank.common.GContants;
import me.tomoya.kanojyongank.util.ImageLoader;
import me.tomoya.kanojyongank.util.ScreenUtils;
import uk.co.senab.photoview.PhotoViewAttacher;

@PropertiesInject(contentViewId = R.layout.activity_photo, isStatusBarTranslucent = true)
public class PhotoActivity extends SimpleActivity {
	private String mImgUrl;
	PhotoViewAttacher mAttcher;

	@BindView(R.id.photo_kano)     SimpleDraweeView photoKano;
	//@BindView(R.id.progress_photo) ProgressBar      progressPhoto;
	@BindView(R.id.activity_photo) FrameLayout      activityPhoto;

	public static Intent newIntent(Context context, String imgUrl) {
		Intent intent = new Intent(context, PhotoActivity.class);
		intent.putExtra(GContants.FLAG_PHOTODATA_IMG, imgUrl);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
		initView();
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			getWindow().setSharedElementEnterTransition(
					DraweeTransition.createTransitionSet(ScalingUtils.ScaleType.CENTER_CROP,
							ScalingUtils.ScaleType.CENTER_CROP));
			getWindow().setSharedElementReturnTransition(
					DraweeTransition.createTransitionSet(ScalingUtils.ScaleType.CENTER_CROP,
							ScalingUtils.ScaleType.CENTER_CROP));
		}
	}

	private void initData() {
		Intent intent = getIntent();
		mImgUrl = intent.getStringExtra(GContants.FLAG_PHOTODATA_IMG);
	}

	private void initView() {
		ImageLoader.loadImage(photoKano, mImgUrl, getResources());
		if (ScreenUtils.isTabletDevice(this)) {
			photoKano.setAspectRatio(1.0f);
		} else {
			photoKano.setAspectRatio(0.82f);
		}
		mAttcher = new PhotoViewAttacher(photoKano);
		mAttcher.update();
		//activityPhoto.setBackgroundColor(mColor);
		photoKano.setOnTouchListener(new OnKanoTouchListener());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_photo, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Drawable drawable = item.getIcon();
		if (drawable instanceof Animatable) {
			((Animatable) drawable).start();
		}
		switch (item.getItemId()) {
			case R.id.menu_action_share:
				Toast.makeText(this, "Shared!", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.menu_action_save:
				Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private class OnKanoTouchListener implements View.OnTouchListener {
		int startX;
		int startY;//起始点
		int dX;
		int dY;//位移

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) v.getLayoutParams();
			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					int currX = (int) event.getRawX();
					int currY = (int) event.getRawY();
					dX = currX - startX;
					dY = currY - startY;
					layoutParams.topMargin = dY / 2;
					layoutParams.bottomMargin = -dY / 2;
					layoutParams.leftMargin = dX / 2;
					layoutParams.rightMargin = -dX / 2;
					if (dY >= 400) {
						activityPhoto.getBackground().setAlpha(128);
					} else {
						double ratioAlpha = (Math.abs(dY) / 400.0) * 127;
						double alpha = ((Math.abs(dY)/400.0)*47);
						Log.e("value", "onTouch: "+alpha+"    "+ratioAlpha );
						activityPhoto.getBackground().setAlpha(175-(int)alpha);
					}
					v.requestLayout();
					break;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_CANCEL:
					if (dY > 200 || dX > 200) {
						onBackPressed();
					} else {

					}
					break;
			}
			return true;
		}
	}
}
