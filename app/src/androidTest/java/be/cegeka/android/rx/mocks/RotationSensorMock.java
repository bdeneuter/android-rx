package be.cegeka.android.rx.mocks;

import be.cegeka.android.rx.infrastructure.RotationSensor;
import rx.Observable;

public class RotationSensorMock implements RotationSensor {

    private Observable<Double> connectX;
    private Observable<Double> connectY;
    private Observable<Double> connectZ;

    public RotationSensorMock() {
        connectX = Observable.empty();
        connectY = Observable.empty();
        connectZ = Observable.empty();
    }

    public RotationSensorMock(Observable<Double> connectX, Observable<Double> connectY, Observable<Double> connectZ) {
        this.connectX = connectX;
        this.connectY = connectY;
        this.connectZ = connectZ;
    }

    @Override
    public Observable<Double> connectX() {
        return connectX;
    }

    @Override
    public Observable<Double> connectY() {
        return connectY;
    }

    @Override
    public Observable<Double> connectZ() {
        return connectZ;
    }
}
