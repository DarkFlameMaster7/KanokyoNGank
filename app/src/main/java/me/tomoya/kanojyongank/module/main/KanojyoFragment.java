package me.tomoya.kanojyongank.module.main;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import me.tomoya.kanojyongank.BuildConfig;
import me.tomoya.kanojyongank.R;
import me.tomoya.kanojyongank.base.BaseFragment;
import me.tomoya.kanojyongank.bean.Kanojyo;
import me.tomoya.kanojyongank.module.gank.GankActivity;
import me.tomoya.kanojyongank.util.DateUtils;
import me.tomoya.kanojyongank.util.ScreenUtils;
import me.tomoya.kanojyongank.widget.fresco.ImgScaleType;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass. Use the {@link KanojyoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KanojyoFragment extends BaseFragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String TAG       = "KanojyoFragment";
	private static final String FLAG_DATA = "data";

	@Bind(R.id.img_vp_kanojyo)   SimpleDraweeView imgVpKanojyo;
	@Bind(R.id.text_title)       TextView         textTitle;
	@Bind(R.id.text_description) TextView         textDescriprion;

	private String  imgUrl;
	private String  desc;
	private Kanojyo kanojyo;

	@Override public View bindViews(LayoutInflater inflater) {
		final View view = inflater.inflate(R.layout.fragment_kanojyo, null, false);
		ButterKnife.bind(this, view);
		showContent(view);
		return view;
	}

	@Override public void initData() {
		if (getArguments() != null) {
			kanojyo = (Kanojyo) getArguments().getSerializable(FLAG_DATA);
			Date  date = kanojyo.publishedAt;
			int[] ymd  = DateUtils.divideDate(date);

			imgUrl = kanojyo.url;
			desc = kanojyo.desc;
		}
	}

	@Override public void initActions() {

	}

	@Override public void loadData() {
		if (BuildConfig.DEBUG) Log.e("lazy load", "involke");
	}

	@OnClick(R.id.text_description) void onClick() {
		//ActivityOptionsCompat options =
		//    ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), imgVpKanojyo,
		//        "kanoTransition");
		ActivityOptionsCompat options =
				ActivityOptionsCompat.makeCustomAnimation(getContext(), R.anim.activity_in,
						R.anim.activity_out);
		ActivityCompat.startActivity(getActivity(), GankActivity.newIntent(getContext(), kanojyo),
				options.toBundle());
		//getActivity().startActivity(GankActivity.newIntent(getContext(),kanojyo),options.toBundle());
		//GankActivity.startGankActivity(getActivity(), kanojyo);
		//getActivity().overridePendingTransition(R.anim.activity_in, R.anim.fade_out);
	}

	public KanojyoFragment() {
		// Required empty public constructor
	}

	// TODO: Rename and change types and number of parameters
	public static KanojyoFragment newInstance(Kanojyo kanojyo) {
		KanojyoFragment fragment = new KanojyoFragment();
		Bundle          args     = new Bundle();
		args.putSerializable(FLAG_DATA, kanojyo);
		fragment.setArguments(args);
		return fragment;
	}

	@Override public void onDestroyView() {
		super.onDestroyView();
		ButterKnife.unbind(this);
	}

	@Override public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
	}

	private void showContent(View view) {
		textDescriprion.setText(desc);
		Uri uri = Uri.parse(imgUrl);
		//imgVpKanojyo.setImageURI(uri);

		//设置Drawee属性 其内部mvc实现 https://www.fresco-cn.org/docs
		GenericDraweeHierarchyBuilder builder =
				GenericDraweeHierarchyBuilder.newInstance(getResources());
		GenericDraweeHierarchy draweeHierarchy =
				builder.setProgressBarImage(new ProgressBarDrawable())
						.setActualImageScaleType(new ImgScaleType())
						.setFadeDuration(300)
						.build();

		ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
			@Override
			public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
				super.onFinalImageSet(id, imageInfo, animatable);
			}

			@Override public void onFailure(String id, Throwable throwable) {
				super.onFailure(id, throwable);
				Toast.makeText(getContext(), "image load failure", Toast.LENGTH_SHORT)
						.show();
				Log.d(TAG, "onFailure: " + "id:" + id + "Reason:" + throwable);
			}
		};
		DraweeController controller = Fresco.newDraweeControllerBuilder()
				.setControllerListener(controllerListener)
				.setTapToRetryEnabled(true)
				.setUri(uri)
				.build();

		//fresco drawee not support wrap_content but can use ds instead of layoutparams
		if (ScreenUtils.isTabletDevice(getActivity())) {
			imgVpKanojyo.setAspectRatio(1.33f);
		} else {
			imgVpKanojyo.setAspectRatio(1.0f);
		}
		imgVpKanojyo.setController(controller);
		imgVpKanojyo.setHierarchy(draweeHierarchy);
	}
}