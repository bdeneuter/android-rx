package be.cegeka.android.rx.infrastructure;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import rx.Observable;
import rx.Subscriber;
import rx.subjects.PublishSubject;

import static android.hardware.SensorManager.getOrientation;
import static android.hardware.SensorManager.getRotationMatrixFromVector;

public class RotationSensorImpl extends AbstractSensor implements RotationSensor {

    private final SensorManager sensorManager;
    private final Sensor gameRotationSensor;
    private EventListener eventListener = new EventListener();

    float[] rotationMatrix = new float[16];
    float[] orientation = new float[3];

    private PublishSubject<Double> subjectX = PublishSubject.create();
    private PublishSubject<Double> subjectY = PublishSubject.create();
    private PublishSubject<Double> subjectZ = PublishSubject.create();

    public RotationSensorImpl(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        gameRotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
    }

    public Observable<Double> connectX() {
        return createObservable(new Observable.OnSubscribe<Double>() {
            @Override
            public void call(Subscriber<? super Double> subscriber) {
                subjectX.subscribe(subscriber);
            }
        });
    }

    public Observable<Double> connectY() {
        return createObservable(new Observable.OnSubscribe<Double>() {
            @Override
            public void call(Subscriber<? super Double> subscriber) {
                subjectY.subscribe(subscriber);
            }
        });
    }

    public Observable<Double> connectZ() {
        return createObservable(new Observable.OnSubscribe<Double>() {
            @Override
            public void call(Subscriber<? super Double> subscriber) {
                subjectZ.subscribe(subscriber);
            }
        });
    }

    @Override
    protected void connect() {
        sensorManager.registerListener(eventListener, gameRotationSensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void disconnect() {
        sensorManager.unregisterListener(eventListener);
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
