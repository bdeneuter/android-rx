package be.cegeka.android.rx.domain;


import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public class Plane {

    private Position position;

    public Plane(Position initialPosition) {
        position = initialPosition;
    }

    public Observable<Position> position() {
        return Observable.create(new Observable.OnSubscribe<Position>() {
            @Override
            public void call(Subscriber<? super Position> subscriber) {
                subscriber.onNext(position);
                subscriber.onCompleted();
            }
        });
    }

    public static Func1<Plane, Observable<Position>> toPositions() {
        return new Func1<Plane, Observable<Position>>() {
            @Override
            public Observable<Position> call(Plane plane) {
                return plane.position();
            }
        };
    }

}
