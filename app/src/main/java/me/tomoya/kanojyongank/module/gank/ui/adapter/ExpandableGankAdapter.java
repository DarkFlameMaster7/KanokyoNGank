package me.tomoya.kanojyongank.module.gank.ui.adapter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
	private GankData         mGankData;
	private Context          mContext;
	private List<SubAdapter> listAdapter;
	private int              mHeadColor;

	@Inject
	public ExpandableGankAdapter(Context context) {
		this.mContext = context;
		listAdapter = new ArrayList<>();
		Random random = new Random();
		int r = random.nextInt(200) + 128;
		int g = random.nextInt(200) + 128;
		int b = random.nextInt(200) + 128;
		mHeadColor = Color.argb(255, r, g, b);
	}

	public void setData(GankData gankData) {
		gankData.category.remove("福利");
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
		View view = LayoutInflater.from(mContext).inflate(R.layout.item_gank_category, parent, false);
		return new EGAViewHolder(view);
	}

	@Override
	public void onBindViewHolder(final EGAViewHolder holder, int position) {
		final ImageView imgToggle = holder.imgToggle;
		final CardView subCard = holder.cardSubtitle;
		CardView mainCard = holder.cardCategory;
		RecyclerView childRv = holder.rvSubtitle;
		TextView categoryTitle = holder.txtTitle;
		//set header color
		View headerColorView = holder.headerColor;
		float factor = 1 + position * 0.12f;
		headerColorView.setBackgroundColor(Color.argb(255, (int) (Color.red(mHeadColor) / factor),
				(int) (Color.green(mHeadColor) / factor), (int) (Color.blue(mHeadColor) / factor)));
		// sub recyclerview
		subCard.getLayoutParams().height = 0;
		//subCard.requestLayout();
		categoryTitle.setText(mGankData.category.get(position));
		childRv.setLayoutManager(new LinearLayoutManager(mContext));
		childRv.setAdapter(listAdapter.get(position));
		mainCard.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (subCard.getMeasuredHeight() > 0) {
					imgToggle.animate().rotation(0f).setDuration(200).start();
					subCollapse(subCard);
				} else {
					imgToggle.animate().rotation(-90f).setDuration(200).start();
					subExpand(subCard);
				}
			}
		});
	}

	@Override
	public int getItemCount() {
		return listAdapter.size();
	}

	private void subExpand(final View view) {
		int widthMeasure = View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.AT_MOST);
		int heightMeasure = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		view.measure(widthMeasure, heightMeasure);
		ValueAnimator animator = ValueAnimator.ofInt(0, view.getMeasuredHeight());

		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				view.getLayoutParams().height = (int) animation.getAnimatedValue();
				view.requestLayout();
			}
		});
		animator.setInterpolator(new AccelerateInterpolator());
		animator.setDuration(200);
		animator.start();
	}

	private void subCollapse(final View view) {
		ValueAnimator animator = ValueAnimator.ofInt(view.getMeasuredHeight(), 0);
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				view.getLayoutParams().height = (int) animation.getAnimatedValue();
				view.requestLayout();
			}
		});
		animator.setInterpolator(new AccelerateInterpolator());
		animator.setDuration(200);
		animator.start();
	}

	private class SubAdapter extends BaseAdapter<Gank> {
		@Override
		protected void bindView(VH holder, final List<Gank> data, final int position) {
			TextView textView = holder.getView(R.id.txt_expandable_title);

			String via = mContext.getString(R.string.via_fomat, data.get(position).getWho());
			SpannableString spannableString = new SpannableString(via);
			spannableString.setSpan(new TextAppearanceSpan(mContext, R.style.ViaTextStyle), 0,
					via.length(), 0);
			SpannableStringBuilder builder = new SpannableStringBuilder(
					data.get(position).getDesc()).append(spannableString);
			CharSequence destTxt = builder.subSequence(0, builder.length());

			textView.setText(destTxt);
			holder.getView(R.id.expandable_container).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO: 17-2-13
					WebActivity.activityStart(mContext, data.get(position).getUrl(), mHeadColor);
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
