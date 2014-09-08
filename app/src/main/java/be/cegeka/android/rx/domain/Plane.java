package be.cegeka.android.rx.domain;

import rx.Observable;

public class Plane {

    private Position position;

    public Plane(Position initialPosition) {
        position = initialPosition;
    }

    public Observable<Position> position() {
        throw new IllegalStateException("Implement me please!!!!");
    }

}
