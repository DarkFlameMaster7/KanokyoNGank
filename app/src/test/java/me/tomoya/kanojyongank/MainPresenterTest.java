package me.tomoya.kanojyongank;

import me.tomoya.kanojyongank.module.gank.presenter.MainPresenter;
import me.tomoya.kanojyongank.network.RetrofitWrapper;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Created by piper on 17-2-16.
 */

public class MainPresenterTest {

	@Test
	public void testGetData() throws Exception {
		RetrofitWrapper retrofitWrapper = Mockito.mock(RetrofitWrapper.class);
		MainPresenter mainPresenter = new MainPresenter(retrofitWrapper);
		mainPresenter.getDailyData();

	}
}
