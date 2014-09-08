package be.cegeka.android.rx.infrastructure;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.concurrent.atomic.AtomicInteger;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subjects.PublishSubject;
import rx.subscriptions.Subscriptions;

import static android.hardware.SensorManager.getOrientation;
import static android.hardware.SensorManager.getRotationMatrixFromVector;
import static rx.schedulers.Schedulers.computation;
import static rx.schedulers.Schedulers.io;

public class RotationSensor {

    private final SensorManager sensorManager;
    private final Sensor gameRotationSensor;
    private EventListener eventListener = new EventListener();

    float[] rotationMatrix = new float[16];
    float[] orientation = new float[3];

    private PublishSubject<Double> subjectX = PublishSubject.create();
    private PublishSubject<Double> subjectY = PublishSubject.create();
    private PublishSubject<Double> subjectZ = PublishSubject.create();

    private AtomicInteger numberOfSubscribers = new AtomicInteger(0);

    public RotationSensor(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        gameRotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
    }

    public Observable<Double> connectX() {
        return toObservable(subjectX);
    }

    public Observable<Double> connectY() {
        return toObservable(subjectY);
    }

    public Observable<Double> connectZ() {
        return toObservable(subjectZ);
    }

    private Observable<Double> toObservable(final PublishSubject subject) {
        return Observable.create(new Observable.OnSubscribe<Double>() {
            @Override
            public void call(final Subscriber<? super Double> subscriber) {
                subscriber.add(subject.subscribe(subscriber));
                startListening();
                subscriber.add(Subscriptions.create(stopListening()));
            }
        }).subscribeOn(io())
          .observeOn(computation());
    }

    private Action0 stopListening() {
        return new Action0() {
            @Override
            public void call() {
                if (numberOfSubscribers.decrementAndGet() == 0) {
                    sensorManager.unregisterListener(eventListener);
                }
            }
        };
    }

    private void startListening() {
        if (numberOfSubscribers.incrementAndGet() == 1) {
            sensorManager.registerListener(eventListener, gameRotationSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    private class EventListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if(sensorEvent.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
                getRotationMatrixFromVector(rotationMatrix, sensorEvent.values);
                getOrientation(rotationMatrix, orientation);
                subjectX.onNext(Math.toDegrees(orientation[2]));
                subjectY.onNext(Math.toDegrees(orientation[1]));
                subjectZ.onNext(Math.toDegrees(orientation[0]));
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }
}
