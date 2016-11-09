package com.tomoya.kanojyongank.module;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.tomoya.kanojyongank.R;
import com.tomoya.kanojyongank.bean.Kanojyo;
import com.tomoya.kanojyongank.bean.KanojyoData;
import com.tomoya.kanojyongank.module.adapter.KanojyoListAdapter;
import com.tomoya.kanojyongank.module.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.rv_kanojyo)
    RecyclerView rvKanojyo;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    private List<Kanojyo> kanojyoTachi;
    private KanojyoListAdapter kanojyoListAdapter;
    private int page =1;
    private Context context;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                startActivity(new Intent(context,ShowActivity.class));
            }
        });
        kanojyoTachi = new ArrayList<>();
        loadData(page);
        initRecyclerView();
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void initRecyclerView() {
        final StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        rvKanojyo.setLayoutManager(staggeredGridLayoutManager);
        kanojyoListAdapter = new KanojyoListAdapter(this, kanojyoTachi);
        kanojyoListAdapter.setOnKanijyoClickedListerer(new KanojyoListAdapter.OnKanijyoClickedListerer() {
            @Override
            public void onKanojyoClick(View view,int position) {
//                Intent intent = new Intent(context,ShowActivity.class);
                //                intent.putExtra("position", position);
               startActivity(new Intent(context,ShowActivity.class));
            }
        });
        rvKanojyo.setAdapter(kanojyoListAdapter);

    }


    private void loadData(int page) {

        subscription = gankApi.getKanojyoData(page)
                .map(new Func1<KanojyoData, List<Kanojyo>>() {
                    @Override
                    public List<Kanojyo> call(KanojyoData kanojyoData) {
                        return kanojyoData.results;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Kanojyo>>() {
                    @Override
                    public void onCompleted() {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Rx", "onError: " + e);
                    }

                    @Override
                    public void onNext(List<Kanojyo> kanojyos) {
                        kanojyoTachi.addAll(kanojyos);
                        kanojyoListAdapter.notifyDataSetChanged();
                    }
                });
        addSubscription(subscription);
    }


    @Override
    public void onRefresh() {
        loadData(9999);
    }
}
