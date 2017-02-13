package me.tomoya.kanojyongank.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import javax.inject.Inject;
import me.tomoya.kanojyongank.GankApplication;
import me.tomoya.kanojyongank.R;
import me.tomoya.kanojyongank.annotation.PropertiesInject;
import me.tomoya.kanojyongank.di.components.ActivityComponent;
import me.tomoya.kanojyongank.di.components.AppComponent;
import me.tomoya.kanojyongank.di.components.DaggerActivityComponent;
import me.tomoya.kanojyongank.di.modules.ActivityModule;
import me.tomoya.kanojyongank.widget.SlideLayout;

public abstract class BaseActivity<T extends BaseContract.Presenter> extends RxAppCompatActivity {

	@Inject protected T mPresenter;

	@Nullable @BindView(R.id.toolbar) protected Toolbar toolbar;

	/**
	 * contentview id
	 */
	protected int     mContentViewId;
	/**
	 * staus of statusbar
	 */
	protected boolean mIsStatusBarTranslucent;
	/**
	 * enable exit by slide
	 */
	protected boolean mEnableSlideExit;
	/**
	 * has navigation or not
	 */
	protected boolean mHasNavigationView;

	private Unbinder mUbinder;

	public AppComponent getAppComponent() {
		return GankApplication.getAppComponent();
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//get annotations and properties
		if (getClass().isAnnotationPresent(PropertiesInject.class)) {
			PropertiesInject annotation = getClass().getAnnotation(PropertiesInject.class);
			mContentViewId = annotation.contentViewId();
			mEnableSlideExit = annotation.enableSlideExit();
			mHasNavigationView = annotation.hasNavigationView();
			mIsStatusBarTranslucent = annotation.isStatusBarTranslucent();
		} else {
			throw new RuntimeException("Activity must add annotations of PropertiesInject.class");
		}

		setContentView(mContentViewId);
		mUbinder = ButterKnife.bind(this);
		//status
		if (mIsStatusBarTranslucent) {
			Window window = this.getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		if (toolbar != null) {
			setSupportActionBar(toolbar);
		}

		if (mEnableSlideExit) {
			SlideLayout slideLayout = new SlideLayout(this);
			slideLayout.bind(this);
		}

		if (mHasNavigationView) {
			initNavigation();
		}
	}

	public void initNavigation() {

	}

	protected ActivityComponent getActivityComponent() {
		return DaggerActivityComponent.builder()
				.appComponent(getAppComponent())
				.activityModule(new ActivityModule(this))
				.build();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		mUbinder.unbind();
	}
}
