package be.cegeka.android.rx.domain;

import rx.Observable;
import rx.functions.Func1;

import static be.cegeka.android.rx.domain.Direction.BACKWARDS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static rx.Observable.timer;

public class AIControlWheel implements ControlWheel {

    private Observable<Direction> direction =
            timer(0, 100, MILLISECONDS).map(new Func1<Long, Direction>() {
                @Override
                public Direction call(Long aLong) {
                    return BACKWARDS;
                }
            });

    @Override
    public Observable<Direction> direction() {
        return direction;
    }
}
