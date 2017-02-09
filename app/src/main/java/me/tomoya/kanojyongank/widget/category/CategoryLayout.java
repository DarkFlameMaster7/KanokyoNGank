package me.tomoya.kanojyongank.widget.category;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import me.tomoya.kanojyongank.bean.Gank;

/**
 * Created by Tomoya-Hoo on 2016/11/6.
 */

public class CategoryLayout extends LinearLayout {
    private Context mContext;
    CategoryAdapter<Gank> mAdapter;
    /**
     * 线的颜色
     */
    private int lineColor = 0xff303F9F;
    /**
     * 每一个 item 与 item 之间的 marginTop 的大小
     */
    private SparseArray marginArray = new SparseArray<Integer>();


    public CategoryLayout(Context context) {
        super(context);
    }

    public CategoryLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CategoryLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        setOrientation(VERTICAL);
    }
    public void setAdapter(CategoryAdapter<Gank> adapter){
        mAdapter = adapter;

    }
    public void create(){
        for (int i = 0; i < mAdapter.getCount(); i++) {
            addItem(i);
        }
    }

    private void addItem( int position) {
        if (marginArray.valueAt(position) != null) {
            if ((Integer) marginArray.valueAt(position) > 0) {
                addView(createLine((Integer) marginArray.valueAt(position)));
            }
        } else {
            addView(createLine(10));
        }
        addView(mAdapter.getView(position,null,null));
        addView(createLine(0));

    }
    public CategoryLayout setItemMarginTop(int index, int value) {
        marginArray.put(index, value);
        return this;
    }


    public CategoryLayout setItemMarginTop(int value) {
//
//        if (mAdapter.getCount() <= 0) {
//            throw new RuntimeException("list is null");
//        }

        for (int i = 0; i < mAdapter.getCount(); i++) {
            marginArray.put(i, value);
        }
        return this;
    }
    private View createLine(int margin) {
        View view = new View(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
        params.topMargin = margin;
        view.setLayoutParams(params);
        view.setBackgroundColor(lineColor);
        return view;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
