package be.cegeka.android.rx.domain;


import be.cegeka.android.rx.infrastructure.PixelConverter;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

import static be.cegeka.android.rx.domain.Direction.BACKWARDS;
import static be.cegeka.android.rx.domain.Direction.FORWARD;
import static be.cegeka.android.rx.domain.Direction.LEFT;
import static be.cegeka.android.rx.domain.Direction.RIGHT;
import static be.cegeka.android.rx.infrastructure.Sequence.nextInt;
import static rx.Observable.merge;

public class Plane {

    private int id = nextInt();
    private int delta;

    private Observable<Position> position;
    private Position lastPosition;

    public Plane(Board board, ControlWheel controlWheel, PixelConverter pixelConverter) {
        this(board, board.getCenter(), controlWheel, pixelConverter);
    }

    public Plane(Board board, Position position, ControlWheel controlWheel, PixelConverter pixelConverter) {
        delta = pixelConverter.toPixels(5);
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

        this.position = merge(moveLeft, moveRight, moveForward, moveBackwards)
                    .filter(board.isContained())
                    .startWith(position)
                    .distinctUntilChanged()
                    .doOnNext(setLastPosition());
    }

    public int getId() {
        return id;
    }

    private Func1<Direction, Position> moveForward() {
        return new Func1<Direction, Position>() {
            @Override
            public Position call(Direction direction) {
                return new Position(lastPosition.x, lastPosition.y - delta);
            }
        };
    }

    private Func1<Direction, Position> moveBackwards() {
        return new Func1<Direction, Position>() {
            @Override
            public Position call(Direction direction) {
                return new Position(lastPosition.x, lastPosition.y + delta);
            }
        };
    }

    private Func1<Direction, Position> moveRight() {
        return new Func1<Direction, Position>() {
            @Override
            public Position call(Direction direction) {
                return new Position(lastPosition.x + delta, lastPosition.y);
            }
        };
    }

    private Func1<Direction, Position> moveLeft() {
        return new Func1<Direction, Position>() {
            @Override
            public Position call(Direction direction) {
                return new Position(lastPosition.x - delta, lastPosition.y);
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

    public static Func1<Plane, Observable<Position>> toPositions() {
        return new Func1<Plane, Observable<Position>>() {
            @Override
            public Observable<Position> call(Plane plane) {
                return plane.position();
            }
        };
    }

}
