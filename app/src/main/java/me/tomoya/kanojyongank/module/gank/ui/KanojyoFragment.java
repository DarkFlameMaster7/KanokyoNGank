package me.tomoya.kanojyongank.module.gank.ui;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.OnClick;
import com.facebook.drawee.view.SimpleDraweeView;
import me.tomoya.kanojyongank.R;
import me.tomoya.kanojyongank.annotation.PropertiesInject;
import me.tomoya.kanojyongank.base.BaseFragment;
import me.tomoya.kanojyongank.bean.Kanojyo;
import me.tomoya.kanojyongank.util.ImageLoader;
import me.tomoya.kanojyongank.util.ScreenUtils;

import static me.tomoya.kanojyongank.common.GContants.FLAG_DATA;
import static me.tomoya.kanojyongank.util.Preconditions.checkNotNull;

/**
 * A simple {@link Fragment} subclass. Use the {@link KanojyoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@PropertiesInject(contentViewId = R.layout.fragment_kanojyo)
public class KanojyoFragment extends BaseFragment {

	private static final String TAG = "KanojyoFragment";

	@BindView(R.id.img_vp_kanojyo)   SimpleDraweeView imgVpKanojyo;
	@BindView(R.id.text_title)       TextView         textTitle;
	@BindView(R.id.text_description) TextView         textDescriprion;
	@BindView(R.id.card_kano)        CardView         cardKano;

	@BindString(R.string.transition_kano) String transitionName;

	private String  imgUrl;
	private String  desc;
	private Kanojyo kanojyo;

	@Override
	public void initData() {
		if (getArguments() != null) {
			kanojyo = (Kanojyo) getArguments().getSerializable(FLAG_DATA);
			checkNotNull(kanojyo);
			imgUrl = kanojyo.url;
			desc = kanojyo.desc;
		}
	}

	@Override
	public void initViewAndActions() {
		textDescriprion.setText(desc);
		ImageLoader.loadImageWithProgress(imgVpKanojyo, imgUrl, getResources());
		//fresco drawee does not support wrap_content
		if (ScreenUtils.isTabletDevice(getActivity())) {
			imgVpKanojyo.setAspectRatio(1.33f);
		} else {
			imgVpKanojyo.setAspectRatio(1.0f);
		}
	}

	@Override
	public void loadData() {
		// TODO: 17-2-16 load
	}

	@OnClick(R.id.card_kano)
	void onClick() {
		ActivityCompat.startActivity(getActivity(), GankActivity.newIntent(getContext(), kanojyo),
				getActivityOptions(mActivity, imgVpKanojyo, transitionName).toBundle());
	}

	@OnClick(R.id.img_vp_kanojyo)
	void onImgClick() {
		ActivityCompat.startActivity(mContext, PhotoActivity.newIntent(mContext, imgUrl),
				getActivityOptions(mActivity, imgVpKanojyo, transitionName).toBundle());
	}

	private ActivityOptionsCompat getActivityOptions(Activity activity, View view,
			String transitionName) {
		ActivityOptionsCompat options;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view,
					transitionName);
		} else {
			options = ActivityOptionsCompat.makeCustomAnimation(activity, R.anim.activity_in,
					R.anim.activity_out);
		}
		return options;
	}

	public KanojyoFragment() {
		// Required empty public constructor
	}

	public static KanojyoFragment newInstance(Kanojyo kanojyo) {
		KanojyoFragment fragment = new KanojyoFragment();
		Bundle args = new Bundle();

		args.putSerializable(FLAG_DATA, kanojyo);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
	}
}
