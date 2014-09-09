package be.cegeka.android.rx.infrastructure;

import android.test.AndroidTestCase;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;

import static com.jayway.awaitility.Awaitility.await;
import static org.fest.assertions.api.Assertions.assertThat;

public class AbstractSensorTest extends AndroidTestCase {

    private SensorMock sensor = new SensorMock();

    public void testCreateObservable_WhenSubscribe_ThenConnect() throws Exception {

        // GIVEN
        Observable observable = sensor.createObservable(observable());

        // WHEN
        Subscription subscription = observable.subscribe(subscriber());

        // THEN
        await().until(new Runnable() {
            @Override
            public void run() {
                assertThat(sensor.connected).isTrue();
            }
        });
    }

    public void testCreateObservable_WhenUnsubscribe_ThenDisconnect() throws Exception {

        // GIVEN
        Observable observable = sensor.createObservable(observable());
        Subscription subscription = observable.subscribe(subscriber());
        await().until(new Runnable() {
            @Override
            public void run() {
                assertThat(sensor.connected).isTrue();
            }
        });

        // WHEN
        subscription.unsubscribe();

        // THEN
        await().until(new Runnable() {
            @Override
            public void run() {
                assertThat(sensor.connected).isFalse();
            }
        });
    }

    public void testCreateObservable_WhenUnsubscribeButOtherSubscriber_ThenConnect() throws Exception {

        // GIVEN
        Observable observable = sensor.createObservable(observable());
        Subscription subscription = observable.subscribe(subscriber());
        Subscription otherSubscription = observable.subscribe(subscriber());

        // WHEN
        subscription.unsubscribe();

        // THEN
        await().until(new Runnable() {
            @Override
            public void run() {
                assertThat(sensor.connected).isTrue();
            }
        });
    }

    private Action1 subscriber() {
        return new Action1() {
            @Override
            public void call(Object o) {

            }
        };
    }

    private Observable.OnSubscribe<Object> observable() {
        return new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {

            }
        };
    }

    private class SensorMock extends AbstractSensor {

        public boolean connected = false;

        @Override
        protected void disconnect() {
            connected = false;
        }

        @Override
        protected void connect() {
            connected = true;
        }
    }
}