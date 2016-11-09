package com.tomoya.kanojyongank.module.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tomoya.kanojyongank.R;
import com.tomoya.kanojyongank.bean.Kanojyo;
import com.tomoya.kanojyongank.module.GankActivity;
import com.tomoya.kanojyongank.module.base.BaseFragment;
import com.tomoya.kanojyongank.util.DateUtils;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link KanojyoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KanojyoFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String FLAG_DATA = "data";

    @Bind(R.id.img_vp_kanojyo)
    ImageView imgVpKanojyo;
    @Bind(R.id.text_title)
    TextView textTitle;
    @Bind(R.id.text_description)
    TextView textDescriprion;


    private String imgUrl;
    private String desc;
    private Kanojyo kanojyo;


    @Override
    public View initViews(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_kanojyo, null, false);
        ButterKnife.bind(this, view);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = (int) (width / 1.33f);
        ViewGroup.LayoutParams layoutParams = imgVpKanojyo.getLayoutParams();
        layoutParams.height = height;
        Glide.with(this).load(imgUrl).crossFade().centerCrop().into(imgVpKanojyo);
        textDescriprion.setText(desc);
        return view;
    }

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
    public void initActions() {

    }

    @Override
    public void loadData() {
        Log.e("lazy load", "involke");
    }
    @OnClick(R.id.text_description)
    void onClick(){
        GankActivity.startGankActivity(getActivity(),kanojyo);
        getActivity().overridePendingTransition(R.anim.activity_in, R.anim.fade_out);
    }

    public KanojyoFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static KanojyoFragment newInstance(Kanojyo kanojyo) {
        KanojyoFragment fragment = new KanojyoFragment();
        Bundle args = new Bundle();
        args.putSerializable(FLAG_DATA, kanojyo);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }
}
