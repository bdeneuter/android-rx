package be.cegeka.android.rx.infrastructure;

import java.util.concurrent.atomic.AtomicInteger;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

import static rx.schedulers.Schedulers.computation;
import static rx.schedulers.Schedulers.io;

public abstract class AbstractSensor {

    private AtomicInteger numberOfSubscribers = new AtomicInteger(0);

    protected <T> Observable createObservable(final Observable.OnSubscribe<T> onSubscribe) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(final Subscriber<? super T> subscriber) {
                onSubscribe.call(subscriber);
                startSensor();
                subscriber.add(Subscriptions.create(stopSensor()));
            }
        }).subscribeOn(io()).observeOn(computation());
    }

    private Action0 stopSensor() {
        return new Action0() {
            @Override
            public void call() {
                if (numberOfSubscribers.decrementAndGet() == 0) {
                    disconnect();
                }
            }
        };
    }

    protected abstract void disconnect();

    private void startSensor() {
        if (numberOfSubscribers.incrementAndGet() == 1) {
            connect();
        }
    }

    protected abstract void connect();

}
