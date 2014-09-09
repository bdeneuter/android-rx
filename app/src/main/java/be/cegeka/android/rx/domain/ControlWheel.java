package be.cegeka.android.rx.domain;

import be.cegeka.android.rx.infrastructure.RotationSensor;
import rx.Observable;

public class ControlWheel {

    private RotationSensor rotationSensor;

    public ControlWheel(RotationSensor rotationSensor) {
        this.rotationSensor = rotationSensor;
    }

    public Observable<Direction> direction() {
        throw new IllegalStateException("Implement me !!!");
    }

}
