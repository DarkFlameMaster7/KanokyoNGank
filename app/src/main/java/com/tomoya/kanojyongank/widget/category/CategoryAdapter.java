package com.tomoya.kanojyongank.widget.category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tomoya.kanojyongank.R;
import com.tomoya.kanojyongank.bean.Gank;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Tomoya-Hoo on 2016/11/6.
 */

public class CategoryAdapter<T> extends BaseAdapter {
    private List<T> mList;
    private LayoutInflater inflater;
    private Context mContext;

    public CategoryAdapter(Context context, List<T> list) {
        this.mList = list;
        this.mContext = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.item_gank, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        T t = mList.get(i);
        if (t instanceof Gank) {
            holder.tvCategory.setText(((Gank) t).getType());
            holder.tvTitle.setText(((Gank) t).getDesc());
        }

        return view;
    }

    static class ViewHolder {
        @Bind(R.id.tv_category)
        TextView tvCategory;
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.ll_gank_parent)
        LinearLayout llGankParent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
