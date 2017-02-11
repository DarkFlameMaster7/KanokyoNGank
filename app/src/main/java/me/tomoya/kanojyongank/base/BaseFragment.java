package me.tomoya.kanojyongank.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.tomoya.kanojyongank.annotation.PropertiesInject;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment {
	private Activity mActivity;
	private Context  mContext;
	private Unbinder mUnbinder;

	private int     mContentViewId;
	/*
	* 控件初始化flag
	* */
	private boolean isViewPrepared;
	/*
	* 数据加载flag
	* */
	private boolean hasFetchData;

	public abstract void initData();

	public abstract void initViewAndActions();

	public abstract void loadData();

	public BaseFragment() {
		// Required empty public constructor
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		mContext = context;
		mActivity = (Activity) context;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getClass().isAnnotationPresent(PropertiesInject.class)) {
			PropertiesInject annotation = getClass().getAnnotation(PropertiesInject.class);
			mContentViewId = annotation.contentViewId();
		} else {
			throw new RuntimeException("Class must add annotations of PropertiesInject.class");
		}
		initData();
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			lazyFetchDateIfPrepared();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = LayoutInflater.from(mContext).inflate(mContentViewId, null);
		mUnbinder = ButterKnife.bind(this, view);
		initViewAndActions();
		isViewPrepared = true;
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		lazyFetchDateIfPrepared();
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		hasFetchData = false;
		isViewPrepared = false;
		mUnbinder.unbind();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public void lazyFetchDateIfPrepared() {
		if (getUserVisibleHint() && !hasFetchData && isViewPrepared) {
			hasFetchData = true;
			loadData();
		}
	}
}
