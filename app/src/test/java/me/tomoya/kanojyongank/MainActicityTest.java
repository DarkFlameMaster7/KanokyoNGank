package me.tomoya.kanojyongank;

import me.tomoya.kanojyongank.module.gank.ui.ShowActivity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

/**
 * Created by piper on 17-2-16.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)
public class MainActicityTest {
	@Test
	public void testMainActivity() {
		ShowActivity showActivity = Robolectric.setupActivity(ShowActivity.class);
		showActivity.findViewById(R.id.ibtn_rocket).performClick();
		ShadowActivity shadowActivity = Shadows.shadowOf(showActivity);
	}
}
