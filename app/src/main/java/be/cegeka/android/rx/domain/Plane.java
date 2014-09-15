package be.cegeka.android.rx.domain;


import android.graphics.Rect;
import android.util.Log;

import com.google.common.base.MoreObjects;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.subjects.PublishSubject;

import static be.cegeka.android.rx.domain.Army.GERMAN;
import static be.cegeka.android.rx.domain.Direction.toDelta;
import static be.cegeka.android.rx.domain.Orientation.TOP;
import static be.cegeka.android.rx.infrastructure.Sequence.nextInt;

public class Plane {

    public static final int WIDTH_IN_DP = 75;
    public static final int HEIGHT_IN_DP = 62;

    private int id = nextInt();

    private Observable<Position> position;

    private Army army;
    private Orientation orientation;
    private Board board;
    private PublishSubject<Boolean> destroyed = PublishSubject.create();

    public Plane(Army army, ControlWheel controlWheel, Board board) {
        this(army, board.getCenter(), TOP, controlWheel, board);
    }

    public Plane(final Army army, Position position, final Orientation orientation, ControlWheel controlWheel, final Board board) {
        this.army = army;
        this.orientation = orientation;
        this.board = board;
        this.position = controlWheel.direction()
                                    .map(toDelta(orientation))
                                    .scan(position, addPosition())
                                    .distinctUntilChanged()
                                    .takeUntil(destroyed)
                                    .takeWhile(board.containsPosition());
    }

    public int getId() {
        return id;
    }

    public Army getArmy() {
        return army;
    }

    public Observable<Position> position() {
        return position;
    }

    public Observable<Boolean> destroyed() {
        return destroyed;
    }

    public Observable<Bounds> bounds() {
        return position().map(new Func1<Position, Bounds>() {
            @Override
            public Bounds call(Position position) {
                int deltaX = WIDTH_IN_DP / 2;
                int deltaY = HEIGHT_IN_DP / 2;
                return new Bounds(Plane.this, new Rect(position.x  - deltaX, position.y - deltaY, position.x + deltaX, position.y + deltaY));
            }
        });
    }

    public Func2<Position, Position, Position> addPosition() {
        return new Func2<Position, Position, Position>() {
            @Override
            public Position call(Position oldPosition, Position delta) {

                Position newPosition = oldPosition.add(delta);
                if (army == GERMAN || board.contains(newPosition)) {
                    return newPosition;
                }
                return oldPosition;
            }
        };
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("id", id).add("army", army).toString();
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void destroy() {
        destroyed.onNext(true);
        Log.d("Plane", "Before onComplete destroy");
        destroyed.onCompleted();
    }
}
