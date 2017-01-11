package com.tomoya.kanojyongank.module.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment {
    private static final String TAG = "basefragment";
    /*
    * 控件初始化flag
    * */
    private boolean isViewPrepared;
    /*
    * 数据加载flag
    * */
    private boolean hasFetchData;

    public abstract View bindViews(LayoutInflater inflater);
    public abstract void initData();
    public abstract void initActions();

    public abstract void loadData();


    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = bindViews(inflater);
        initActions();
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

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void lazyFetchDateIfPrepared(){
        if (getUserVisibleHint() && !hasFetchData && isViewPrepared) {
            hasFetchData = true;
            loadData();
        }
    }
}
