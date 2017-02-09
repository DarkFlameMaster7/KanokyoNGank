package me.tomoya.kanojyongank.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.test.mock.MockApplication;
import android.view.Window;
import android.view.WindowManager;

import butterknife.ButterKnife;
import javax.inject.Inject;
import me.tomoya.kanojyongank.GankApplication;
import me.tomoya.kanojyongank.annotation.PropertiesInject;
import me.tomoya.kanojyongank.di.components.AppComponent;
import me.tomoya.kanojyongank.network.KanojyoRetrofit;
import me.tomoya.kanojyongank.network.api.GankApi;
import me.tomoya.kanojyongank.widget.SlideLayout;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import retrofit2.Retrofit;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public abstract class BaseActivity extends RxAppCompatActivity {
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

	@Inject Retrofit retrofit;

	public static GankApi gankApi = new KanojyoRetrofit().getGankApi();
	CompositeSubscription compositeSubscription;

	public AppComponent getAppComponent() {
		return ((GankApplication) getApplication()).getAppComponent();
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
			throw new RuntimeException("Class must add annotations of PropertiesInject.class");
		}

		setContentView(mContentViewId);
		ButterKnife.bind(this);
		//status
		if (mIsStatusBarTranslucent) {
			Window window = this.getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
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

	public CompositeSubscription getCompositeSubscription() {
		if (compositeSubscription == null) {
			compositeSubscription = new CompositeSubscription();
		}
		return compositeSubscription;
	}

	public void addSubscription(Subscription subscription) {
		if (compositeSubscription == null) {
			compositeSubscription = new CompositeSubscription();
		}
		compositeSubscription.add(subscription);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (compositeSubscription != null) {
			compositeSubscription.unsubscribe();
		}
	}
}
