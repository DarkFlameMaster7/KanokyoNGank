package com.tomoya.kanojyongank.module.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tomoya.kanojyongank.R;
import com.tomoya.kanojyongank.bean.Gank;
import com.tomoya.kanojyongank.callback.OnItemClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Tomoya-Hoo on 2016/11/4.
 */

public class GankListAdapter extends RecyclerView.Adapter<GankListAdapter.GankViewHolder> {
    private Context mContext;
    private List<Gank> mList;
    private OnItemClickListener onItemClickListener;

    public GankListAdapter(Context context, List<Gank> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public GankViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_gank, parent, false);
        GankViewHolder viewHolder = new GankViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(GankViewHolder holder, int position) {
        Gank gank = mList.get(position);
        if (position == 0) {
            showCategory(holder);
        } else {
            Gank pre = mList.get(position - 1);
            if (gank.getType().equals(pre.getType())) {
                hideCategory(holder);
                if (isItemSameAsNext(position, getItemCount(), gank)) {
                    setBg(mContext, holder.llGankParent, R.drawable.bg_gank_center);
                    holder.divider.setVisibility(View.VISIBLE);
                }
                if (!isItemSameAsNext( position, getItemCount(), gank)) {
                    setBg(mContext, holder.llGankParent, R.drawable.bg_gank_bottom);
                }

            } else {
                showCategory(holder);
                if (isItemSameAsNext( position, getItemCount(),gank)) {
                    setBg(mContext, holder.llGankParent, R.drawable.bg_gank_top);
                    holder.divider.setVisibility(View.VISIBLE);
                }
            }

        }
        holder.itemView.setTag(position);
        holder.tvCategory.setText(gank.getType());
        holder.tvTitle.setText(gank.getDesc());
    }


    private boolean isItemSameAsNext(int position, int size, Gank gank) {
        return position < size - 1 && gank.getType().equals(mList.get(position + 1).getType());
    }

    private void setBg(Context context, View view, int resDrawable) {
        view.setBackground(ContextCompat.getDrawable(context, resDrawable));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    class GankViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_category)
        TextView tvCategory;
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.ll_gank_parent)
        LinearLayout llGankParent;
        @Bind(R.id.divider)
        View divider;

        public GankViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.ll_gank_parent)
        void onClick(View view) {
            onItemClickListener.onItemClick(view, (Integer) itemView.getTag());

        }
    }

    public void setItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void showCategory(GankViewHolder holder) {
        if (!isVisibleOf(holder.tvCategory)) {
            holder.tvCategory.setVisibility(View.VISIBLE);
        }
    }

    private void hideCategory(GankViewHolder holder) {
        if (isVisibleOf(holder.tvCategory)) {
            holder.tvCategory.setVisibility(View.GONE);
        }
    }


    private boolean isVisibleOf(View view) {
        return view.getVisibility() == View.VISIBLE;
    }

}
