package me.tomoya.kanojyongank.module.gank.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import java.util.ArrayList;
import java.util.List;
import me.tomoya.kanojyongank.bean.Kanojyo;
import me.tomoya.kanojyongank.module.gank.ui.KanojyoFragment;

/**
 * Created by Tomoya-Hoo on 2016/10/28.
 */

public class KanojyoPagerAdapter extends FragmentStatePagerAdapter {

	private List<Fragment> mFragments = new ArrayList<>();
	private List<Kanojyo> kanojyoList;

	public KanojyoPagerAdapter(FragmentManager fm, List<Kanojyo> kanojyoList) {
		super(fm);
		this.kanojyoList = kanojyoList;
		addFragments();
	}

	private void addFragments() {
		for (int i = 0; i < kanojyoList.size(); i++) {
			Kanojyo kanojyo = kanojyoList.get(i);
			mFragments.add(KanojyoFragment.newInstance(kanojyo));
		}
	}

	@Override
	public Fragment getItem(int position) {
		return mFragments.get(position);
	}

	@Override
	public int getCount() {
		return mFragments.size();
	}

	public void refresh() {
		addFragments();
		notifyDataSetChanged();
	}
}
