package me.tomoya.kanojyongank.base;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by piper on 17-2-10.
 */

public class RxPresenter<T extends BaseContract.View> implements BaseContract.Presenter<T> {
	protected T                     mView;
	protected CompositeSubscription compositeSubscription;

	@Override
	public void attachView(T view) {
		this.mView = view;
	}

	protected CompositeSubscription getCompositeSubscription() {
		if (compositeSubscription == null) {
			compositeSubscription = new CompositeSubscription();
		}
		return compositeSubscription;
	}

	protected void addSubscription(Subscription subscription) {
		if (compositeSubscription == null) {
			compositeSubscription = new CompositeSubscription();
		}
		compositeSubscription.add(subscription);
	}

	@Override
	public void detachView() {
		if (compositeSubscription != null) {
			compositeSubscription.unsubscribe();
		}
		mView = null;
	}
}
