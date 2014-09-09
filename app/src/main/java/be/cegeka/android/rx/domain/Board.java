package be.cegeka.android.rx.domain;

import android.graphics.Rect;

import rx.functions.Func1;

public class Board {

    public final int width;
    public final int height;
    public Rect rect;

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        rect = new Rect(0, 0, width, height);
    }

    public boolean contains(Position position) {
        return rect.contains(position.x, position.y);
    }

    public Position getCenter() {
        return new Position(width / 2, height / 2);
    }

    public Func1<Position, Boolean> containsPosition() {
        return new Func1<Position, Boolean>() {
            @Override
            public Boolean call(Position position) {
                return contains(position);
            }
        };
    }
}
