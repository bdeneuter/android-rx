package be.cegeka.android.rx.domain;

import rx.functions.Func1;

import static be.cegeka.android.rx.domain.Orientation.BOTTOM;

public enum Direction {

    LEFT(new Position(-10, 0)),
    RIGHT(new Position(10, 0)),
    FORWARD(new Position(0, -10)),
    BACKWARDS(new Position(0, 10));

    private Position delta;

    private Direction(Position delta) {
        this.delta = delta;
    }

    public Position delta(Orientation orientation) {
        if (this == FORWARD && orientation == BOTTOM) {
            return BACKWARDS.delta;
        }
        if (this == BACKWARDS && orientation == BOTTOM) {
            return FORWARD.delta;
        }
        return delta;
    }

    public static Func1<Direction, Position> toDelta(final Orientation orientation) {
        return new Func1<Direction, Position>() {
            @Override
            public Position call(Direction direction) {
                return direction.delta(orientation);
            }
        };
    }

}
