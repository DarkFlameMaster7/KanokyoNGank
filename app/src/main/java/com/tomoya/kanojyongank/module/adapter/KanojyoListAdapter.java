package com.tomoya.kanojyongank.module.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.tomoya.kanojyongank.R;
import com.tomoya.kanojyongank.bean.Kanojyo;
import com.tomoya.kanojyongank.widget.KanojyoImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Tomoya-Hoo on 2016/10/13.
 */

public class KanojyoListAdapter extends RecyclerView.Adapter<KanojyoListAdapter.KanojyoViewHolder> implements View
        .OnClickListener {
    private List<Kanojyo> mList;
    private Context mContext;
    OnKanijyoClickedListerer onKanijyoClickedListerer;

    public KanojyoListAdapter(Context mContext, List<Kanojyo> list) {
        this.mContext = mContext;
        this.mList = list;
    }

    @Override
    public KanojyoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kanojyo, parent, false);
        item.setOnClickListener(this);
        return new KanojyoViewHolder(item);
    }

    @Override
    public void onBindViewHolder(KanojyoViewHolder holder, int position) {
        Kanojyo kanojyo = mList.get(position);
        String url = kanojyo.url;
        Glide.with(mContext).load(url).centerCrop().into(holder.imgKanojyo);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
    //View
    @Override
    public void onClick(View view) {
        onKanijyoClickedListerer.onKanojyoClick(view, (Integer) view.getTag());
    }

    static class KanojyoViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.img_kanojyo)
        KanojyoImageView imgKanojyo;

        public KanojyoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setOnKanijyoClickedListerer(OnKanijyoClickedListerer onKanijyoClickedListerer) {
        this.onKanijyoClickedListerer = onKanijyoClickedListerer;
    }

    public interface OnKanijyoClickedListerer {
        void onKanojyoClick(View view,int position);
    }

}
