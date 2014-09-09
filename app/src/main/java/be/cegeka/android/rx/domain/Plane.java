package be.cegeka.android.rx.domain;


import com.google.common.base.MoreObjects;

import be.cegeka.android.rx.infrastructure.PixelConverter;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

import static be.cegeka.android.rx.domain.Army.ALLIED;
import static be.cegeka.android.rx.domain.Direction.BACKWARDS;
import static be.cegeka.android.rx.domain.Direction.FORWARD;
import static be.cegeka.android.rx.domain.Direction.LEFT;
import static be.cegeka.android.rx.domain.Direction.RIGHT;
import static be.cegeka.android.rx.domain.Orientation.TOP;
import static be.cegeka.android.rx.infrastructure.Sequence.nextInt;
import static rx.Observable.merge;

public class Plane {

    private int id = nextInt();
    private int speed;

    private Observable<Position> position;

    private Position lastPosition;
    private Orientation orientation;
    private Army army;

    public Plane(Army army, ControlWheel controlWheel, Board board, PixelConverter pixelConverter) {
        this(army, board.getCenter(), TOP, controlWheel, board, pixelConverter);
    }

    public Plane(Army army, Position position, Orientation orientation, ControlWheel controlWheel, final Board board, PixelConverter pixelConverter) {
        this.army = army;
        this.orientation = orientation;
        speed = pixelConverter.toPixels(10);
        lastPosition = position;

        Observable<Position> moveLeft = controlWheel.direction()
                                                    .filter(direction(LEFT))
                                                    .map(moveLeft());
        Observable<Position> moveRight = controlWheel.direction()
                                                     .filter(direction(RIGHT))
                                                     .map(moveRight());
        Observable<Position> moveForward = controlWheel.direction()
                                                       .filter(direction(FORWARD))
                                                       .map(moveForward());
        Observable<Position> moveBackwards = controlWheel.direction()
                                                         .filter(direction(BACKWARDS))
                                                         .map(moveBackwards());

        Observable<Position> movements = merge(moveLeft, moveRight, moveForward, moveBackwards);
        if (army == ALLIED) {
            movements.filter(board.containsPosition());
        }

        this.position = movements.startWith(position)
                                 .distinctUntilChanged()
                                 .doOnNext(setLastPosition())
                                 .takeWhile(board.containsPosition());
    }

    public int getId() {
        return id;
    }

    public Army getArmy() {
        return army;
    }

    private Func1<Direction, Position> moveForward() {
        return new Func1<Direction, Position>() {
            @Override
            public Position call(Direction direction) {
                if (orientation == TOP) {
                    return new Position(lastPosition.x, lastPosition.y - speed);
                } else {
                    return new Position(lastPosition.x, lastPosition.y + speed);
                }
            }
        };
    }

    private Func1<Direction, Position> moveBackwards() {
        return new Func1<Direction, Position>() {
            @Override
            public Position call(Direction direction) {
                if (orientation == TOP) {
                    return new Position(lastPosition.x, lastPosition.y + speed);
                } else {
                    return new Position(lastPosition.x, lastPosition.y - speed);
                }
            }
        };
    }

    private Func1<Direction, Position> moveRight() {
        return new Func1<Direction, Position>() {
            @Override
            public Position call(Direction direction) {
                return new Position(lastPosition.x + speed, lastPosition.y);
            }
        };
    }

    private Func1<Direction, Position> moveLeft() {
        return new Func1<Direction, Position>() {
            @Override
            public Position call(Direction direction) {
                return new Position(lastPosition.x - speed, lastPosition.y);
            }
        };
    }

    private Action1<Position> setLastPosition() {
        return new Action1<Position>() {
            @Override
            public void call(Position position) {
                lastPosition = position;
            }
        };
    }

    private Func1<Direction, Boolean> direction(final Direction expected) {
        return new Func1<Direction, Boolean>() {
            @Override
            public Boolean call(Direction direction) {
                return direction == expected;
            }
        };
    }

    public Observable<Position> position() {
        return position;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("id", id).add("army", army).add("position", lastPosition).toString();
    }
}
