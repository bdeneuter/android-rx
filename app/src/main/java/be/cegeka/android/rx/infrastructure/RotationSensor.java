package be.cegeka.android.rx.infrastructure;

import rx.Observable;

public interface RotationSensor {

    Observable<Double> connectX();

    Observable<Double> connectY();

    Observable<Double> connectZ();

}
