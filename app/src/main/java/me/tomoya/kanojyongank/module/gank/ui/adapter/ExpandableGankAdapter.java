package me.tomoya.kanojyongank.module.gank.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import me.tomoya.kanojyongank.R;
import me.tomoya.kanojyongank.base.BaseAdapter;
import me.tomoya.kanojyongank.bean.Gank;
import me.tomoya.kanojyongank.bean.GankData;
import me.tomoya.kanojyongank.module.gank.ui.WebActivity;
import me.tomoya.kanojyongank.util.CommonUtils;

/**
 * Created by piper on 17-2-13.
 */

public class ExpandableGankAdapter
		extends RecyclerView.Adapter<ExpandableGankAdapter.EGAViewHolder> {
	private GankData mGankData;
	private Context  mContext;
	private List<SubAdapter> listAdapter;

	@Inject
	public ExpandableGankAdapter(Context context) {
		this.mContext = context;
		listAdapter = new ArrayList<>();
	}

	public void setData(GankData gankData) {
		mGankData = gankData;
		for (String category : gankData.category) {
			SubAdapter subAdapter = new SubAdapter();
			subAdapter.setDataList(CommonUtils.getGankList(gankData.results, category));
			listAdapter.add(subAdapter);
		}
		notifyDataSetChanged();
	}

	@Override
	public EGAViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_gank_category, null, false);
		return new EGAViewHolder(view);
	}

	@Override
	public void onBindViewHolder(EGAViewHolder holder, int position) {
		holder.txtTitle.setText(mGankData.category.get(position));
		holder.rvSubtitle.setLayoutManager(new LinearLayoutManager(mContext));
		holder.rvSubtitle.setAdapter(listAdapter.get(position));
	}

	@Override
	public int getItemCount() {
		return mGankData.category.size();
	}

	private class SubAdapter extends BaseAdapter<Gank> {
		@Override
		protected void bindView(VH holder, final List<Gank> data, final int position) {
			TextView textView = holder.getView(R.id.txt_expandable_title);
			textView.setText(data.get(position).getDesc());
			holder.getView(R.id.expandable_container).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO: 17-2-13
					WebActivity.activityStart(mContext, data.get(position).getUrl(), Color.BLUE);
				}
			});
		}

		@Override
		protected int bindResourceId(int viewType) {
			return R.layout.item_expandable_gank;
		}
	}

	static class EGAViewHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.v_category_color)    View         headerColor;
		@BindView(R.id.txt_category)        TextView     txtTitle;
		@BindView(R.id.img_category_toggle) ImageView    imgToggle;
		@BindView(R.id.card_category)       CardView     cardCategory;
		@BindView(R.id.rv_subtitle)         RecyclerView rvSubtitle;
		@BindView(R.id.card_subtitle)       CardView     cardSubtitle;

		EGAViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}
	}
}
