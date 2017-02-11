package me.tomoya.kanojyongank.widget.rhythm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import java.util.List;
import me.tomoya.kanojyongank.R;

/**
 * Created by Tomoya-Hoo on 2016/10/25. RhythmView的适配器
 */

public class RhythmViewAdapter extends BaseAdapter {
	private float itemWidth;

	public void setItemWidth(float itemWidth) {
		this.itemWidth = itemWidth;
	}

	private Context        mContext;
	private List           mList;
	private LayoutInflater mInflater;
	private RhythmView     mRhythmView;

	public RhythmViewAdapter(Context context, RhythmView rhythmView, List list) {
		this.mContext = context;
		this.mList = list;
		mRhythmView = rhythmView;
		if (context != null) {
			mInflater = LayoutInflater.from(context);
		}
	}

	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		this.mRhythmView.invalidateData();
	}

	@Override
	public int getCount() {
		return mList.size() > 0 ? mList.size() : 0;
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
		RelativeLayout relativeLayout = (RelativeLayout) this.mInflater.inflate(R.layout.item_rhythm,
				null);
		relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams((int) itemWidth,
				mContext.getResources().getDimensionPixelSize(R.dimen.rhythm_item_height)));
		relativeLayout.setTranslationY(itemWidth);

		//设置第二层RelativeLayout布局的宽和高
		RelativeLayout childRelativeLayout = (RelativeLayout) relativeLayout.getChildAt(0);
		int relativeLayoutWidth = (int) itemWidth - 2 * mContext.getResources()
				.getDimensionPixelSize(R.dimen.rhythm_icon_margin);
		childRelativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(relativeLayoutWidth,
				mContext.getResources().getDimensionPixelSize(R.dimen.rhythm_item_height)
						- 2 * mContext.getResources().getDimensionPixelSize(R.dimen.rhythm_icon_margin)));

		ImageView imageIcon = (ImageView) relativeLayout.findViewById(R.id.img_icon);
		//计算ImageView的大小
		int iconSize = (relativeLayoutWidth - 2 * mContext.getResources()
				.getDimensionPixelSize(R.dimen.rhythm_icon_margin));
		ViewGroup.LayoutParams iconParams = imageIcon.getLayoutParams();
		iconParams.width = iconSize;
		iconParams.height = iconSize;
		imageIcon.setLayoutParams(iconParams);
		// TODO: 2016/10/17 设置图片
		return relativeLayout;
	}
}
