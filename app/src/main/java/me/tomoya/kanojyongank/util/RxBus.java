package me.tomoya.kanojyongank.util;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by piper on 17-2-16.
 */

public class RxBus {
	private volatile static RxBus instance;

	private final Subject<Object, Object> bus;

	public RxBus() {
		//会把订阅发生的时间点之后来自observable的数据发给观察者
		bus = new SerializedSubject<>(PublishSubject.create());
	}

	public static RxBus getInstance() {
		if (instance == null) {
			synchronized (RxBus.class) {
				if (instance == null) {
					instance = new RxBus();
				}
			}
		}
		return instance;
	}

	public void post(Object o) {
		bus.onNext(o);
	}

	public <T> Observable<T> toObservable(Class<T> eventType) {
		return bus.ofType(eventType);
	}
}
