package be.cegeka.android.rx.domain;


import android.graphics.Rect;

import com.google.common.base.MoreObjects;

import rx.Observable;
import rx.functions.Func1;

import static be.cegeka.android.rx.domain.Army.GERMAN;
import static be.cegeka.android.rx.domain.Direction.toDelta;
import static be.cegeka.android.rx.domain.Orientation.TOP;
import static be.cegeka.android.rx.domain.Position.addPosition;
import static be.cegeka.android.rx.infrastructure.Sequence.nextInt;

public class Plane {

    public static final int WIDTH_IN_DP = 75;
    public static final int HEIGHT_IN_DP = 62;

    private int id = nextInt();

    private Observable<Position> position;

    private Army army;
    private Orientation orientation;
    private boolean destroyed;

    public Plane(Army army, ControlWheel controlWheel, Board board) {
        this(army, board.getCenter(), TOP, controlWheel, board);
    }

    public Plane(final Army army, Position position, final Orientation orientation, ControlWheel controlWheel, final Board board) {
        this.army = army;
        this.orientation = orientation;
        this.position = controlWheel.direction()
                                    .map(toDelta(orientation))
                                    .scan(position, addPosition())
                                    .filter(onTheBoardOrGerman(army, board))
                                    .distinctUntilChanged()
                                    .takeWhile(notDestroyed())
                                    .takeWhile(board.containsPosition());
    }

    private Func1<Position, Boolean> notDestroyed() {
        return new Func1<Position, Boolean>() {
            @Override
            public Boolean call(Position position) {
                return !destroyed;
            }
        };
    }

    private Func1<Position, Boolean> onTheBoardOrGerman(final Army army, final Board board) {
        return new Func1<Position, Boolean>() {
            @Override
            public Boolean call(Position position) {
                return army == GERMAN || board.contains(position);
            }
        };
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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("id", id).add("army", army).toString();
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void destroy() {
        this.destroyed = true;
    }
}
