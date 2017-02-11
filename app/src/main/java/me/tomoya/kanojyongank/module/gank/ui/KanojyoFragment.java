package me.tomoya.kanojyongank.module.gank.ui;

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.Date;
import me.tomoya.kanojyongank.BuildConfig;
import me.tomoya.kanojyongank.R;
import me.tomoya.kanojyongank.annotation.PropertiesInject;
import me.tomoya.kanojyongank.base.BaseFragment;
import me.tomoya.kanojyongank.bean.Kanojyo;
import me.tomoya.kanojyongank.util.DateUtils;
import me.tomoya.kanojyongank.util.ImageLoader;
import me.tomoya.kanojyongank.util.ScreenUtils;

/**
 * A simple {@link Fragment} subclass. Use the {@link KanojyoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@PropertiesInject(contentViewId = R.layout.fragment_kanojyo)
public class KanojyoFragment extends BaseFragment {

	private static final String TAG       = "KanojyoFragment";
	private static final String FLAG_DATA = "data";

	@BindView(R.id.img_vp_kanojyo)   SimpleDraweeView imgVpKanojyo;
	//@BindView(R.id.text_title)       TextView         textTitle;
	@BindView(R.id.text_description) TextView         textDescriprion;

	private String  imgUrl;
	private String  desc;
	private Kanojyo kanojyo;

	@Override
	public void initData() {
		if (getArguments() != null) {
			kanojyo = (Kanojyo) getArguments().getSerializable(FLAG_DATA);
			Date date = kanojyo.publishedAt;
			int[] ymd = DateUtils.divideDate(date);
			imgUrl = kanojyo.url;
			desc = kanojyo.desc;
		}
	}

	@Override
	public void initViewAndActions() {
		textDescriprion.setText(desc);
		ImageLoader.loadImageWithProgress(imgVpKanojyo, imgUrl, getResources());
		//fresco drawee not support wrap_content but can use ds instead of layoutparams
		if (ScreenUtils.isTabletDevice(getActivity())) {
			imgVpKanojyo.setAspectRatio(1.33f);
		} else {
			imgVpKanojyo.setAspectRatio(1.0f);
		}
	}

	@Override
	public void loadData() {
		if (BuildConfig.DEBUG) Log.e("lazy load", "involke");
	}

	@OnClick(R.id.text_description)
	void onClick() {
		ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(getContext(),
				R.anim.activity_in, R.anim.activity_out);
		ActivityCompat.startActivity(getActivity(), GankActivity.newIntent(getContext(), kanojyo),
				options.toBundle());
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
