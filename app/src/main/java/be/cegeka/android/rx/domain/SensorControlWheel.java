package be.cegeka.android.rx.domain;

import be.cegeka.android.rx.infrastructure.RotationSensor;
import rx.Observable;
import rx.functions.Func1;

import static be.cegeka.android.rx.domain.Direction.BACKWARDS;
import static be.cegeka.android.rx.domain.Direction.FORWARD;
import static be.cegeka.android.rx.domain.Direction.LEFT;
import static be.cegeka.android.rx.domain.Direction.RIGHT;
import static java.lang.Math.abs;
import static rx.Observable.merge;

public class SensorControlWheel implements ControlWheel {

    private RotationSensor rotationSensor;

    public SensorControlWheel(RotationSensor rotationSensor) {
        this.rotationSensor = rotationSensor;
    }

    public Observable<Direction> direction() {
        Observable<Direction> leftOrRight = rotationSensor.connectX()
                                                          .filter(angleGreaterThen(10))
                                                          .map(toLeftOrRight());
        Observable<Direction> forwardOrBackwards = rotationSensor.connectY()
                                                                 .filter(angleGreaterThen(5))
                                                                 .map(toForwardOrBackwards());
        return merge(leftOrRight, forwardOrBackwards);
    }

    private Func1<Double, Boolean> angleGreaterThen(final double threshold) {
        return new Func1<Double, Boolean>() {
              @Override
              public Boolean call(Double angle) { return abs(angle) > threshold; }
        };
    }

    private Func1<Double, Direction> toLeftOrRight() {
        return new Func1<Double, Direction>() {
            @Override
            public Direction call(Double angle) {
                return angle > 0 ? RIGHT : LEFT;
            }
        };
    }

    private Func1<Double, Direction> toForwardOrBackwards() {
        return new Func1<Double, Direction>() {
            @Override
            public Direction call(Double angle) {
                return angle < 0 ? BACKWARDS : FORWARD;
            }
        };
    }

}
