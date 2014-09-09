package be.cegeka.android.rx.infrastructure;

import java.util.concurrent.atomic.AtomicInteger;

import rx.Observable;
import rx.functions.Action0;

import static rx.schedulers.Schedulers.computation;
import static rx.schedulers.Schedulers.io;

public abstract class AbstractSensor {

    private AtomicInteger numberOfSubscribers = new AtomicInteger(0);

    public <T> Observable createObservable(final Observable.OnSubscribe<T> onSubscribe) {
        return Observable.create(onSubscribe)
                         .doOnSubscribe(startSensor())
                         .doOnUnsubscribe(stopSensor())
                         .subscribeOn(io())
                         .observeOn(computation());
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


    private Action0 startSensor() {
        return new Action0() {
            @Override
            public void call() {
                if (numberOfSubscribers.incrementAndGet() == 1) {
                    connect();
                }
            }
        };
    }

    protected abstract void connect();

    protected abstract void disconnect();
}
