package me.tomoya.kanojyongank.base;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

/**
 * Created by piper on 17-2-5. Base adapter of RecyclerView,can also use to construct an adapter
 * quickly
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseAdapter.VH> {
	private List<T> mDataList;

	public void setDataList(List<T> dataList) {
		this.mDataList = dataList;
	}

	public void addDataList(List<T> newData) {
		this.mDataList.addAll(newData);
		this.notifyDataSetChanged();
	}

	/**
	 * set properties of views in holder
	 */
	protected abstract void bindView(VH holder, List<T> data, int position);

	/**
	 * bind layout
	 */
	protected abstract @LayoutRes int bindResourceId(int viewType);

	public int getItemViewType(int position) {
		return position;
	}

	@Override
	public VH onCreateViewHolder(ViewGroup parent, int viewType) {
		return VH.getVH(parent, bindResourceId(viewType));
	}

	@Override
	public void onBindViewHolder(VH holder, int position) {
		bindView(holder, mDataList, position);
	}

	@Override
	public int getItemCount() {
		return mDataList.size();
	}

	protected static class VH extends RecyclerView.ViewHolder {
		private SparseArray<View> mViews;
		private View              mConvertView;

		VH(View itemView) {
			super(itemView);
			mConvertView = itemView;
			mViews = new SparseArray<>();
		}

		/**
		 * initialize ViewHolder
		 */
		static VH getVH(ViewGroup parent, @LayoutRes int layoutId) {

			View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, null, false);
			return new VH(view);
		}

		/**
		 * Get child view of item by id
		 */
		public <T extends View> T getView(int id) {

			View view = mViews.get(id);
			if (view == null) {
				view = mConvertView.findViewById(id);
				mViews.put(id, view);
			}
			return (T) view;
		}
	}
}
