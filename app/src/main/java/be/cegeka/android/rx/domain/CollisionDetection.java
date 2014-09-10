package be.cegeka.android.rx.domain;

import android.util.Log;

import com.google.common.base.Optional;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

import static rx.Observable.combineLatest;

public class CollisionDetection {

    private Plane plane;
    private Observable<Plane> planes;

    CollisionDetection(Plane plane, Observable<Plane> planes) {
        this.plane = plane;
        this.planes = planes;
    }

    public Subscription start() {
        Observable<Bounds> boundsOtherPlanes = planes.filter(otherPlanes(plane)).flatMap(toBounds());

        return combineLatest(plane.bounds(), boundsOtherPlanes, detectCollision())
                .filter(isPresent())
                .map(toCollision())
                .filter(isCollision())
                .subscribe(new Action1<Collision>() {
                    @Override
                    public void call(Collision collision) {
                        Log.d("Collision", "Collision detected " + collision);
                        collision.destroyPlanes();
                    }
                });
    }

    private Func1<Optional<Collision>, Collision> toCollision() {
        return new Func1<Optional<Collision>, Collision>() {
            @Override
            public Collision call(Optional<Collision> optional) {
                return optional.get();
            }
        };
    }

    private Func1<Collision, Boolean> isCollision() {
        return new Func1<Collision, Boolean>() {
            @Override
            public Boolean call(Collision collision) {
                return collision.isCollision();
            }
        };
    }

    private Func1<Optional<Collision>, Boolean> isPresent() {
        return new Func1<Optional<Collision>, Boolean>() {
            @Override
            public Boolean call(Optional<Collision> optional) {
                return optional.isPresent();
            }
        };
    }

    private Func2<Bounds, Bounds, Optional<Collision>> detectCollision() {
        return new Func2<Bounds, Bounds, Optional<Collision>>() {
            @Override
            public Optional<Collision> call(Bounds bounds, Bounds bounds2) {
            if (bounds.intersect(bounds2)) {
                return Optional.of(new Collision(bounds, bounds2));
            }
            return Optional.absent();
            }
        };
    }

    private Func1<Plane, Observable<Bounds>> toBounds() {
        return new Func1<Plane, Observable<Bounds>>() {
            @Override
            public Observable<Bounds> call(Plane plane) {
                return plane.bounds();
            }
        };
    }

    private Func1<Plane, Boolean> otherPlanes(final Plane plane) {
        return new Func1<Plane, Boolean>() {
            @Override
            public Boolean call(Plane other) {
                return !plane.equals(other);
            }
        };
    }

}
