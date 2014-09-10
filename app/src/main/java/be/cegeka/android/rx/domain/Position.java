package be.cegeka.android.rx.domain;

import com.google.common.base.MoreObjects;

import rx.functions.Func2;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

public class Position {

    public final int x, y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position add(Position position) {
        return new Position(x + position.x, y + position.y);
    }

    public static Func2<Position, Position, Position> addPosition() {
        return new Func2<Position, Position, Position>() {
            @Override
            public Position call(Position position, Position position2) {
                return position.add(position2);
            }
        };
    }

    @Override
    public boolean equals(Object o) {
        return reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("x", x).add("y", y).toString();
    }
}
