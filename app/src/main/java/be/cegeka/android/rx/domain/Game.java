package be.cegeka.android.rx.domain;

import rx.functions.Func1;

public class Game {

    private Plane plane;

    public Game(Board board) {
        plane = new Plane(board.getCenter());
    }

    public Plane getPlane() {
        return plane;
    }

    public static Func1<Game, Plane> toPlane() {
        return new Func1<Game, Plane>() {
            @Override
            public Plane call(Game game) {
                return game.getPlane();
            }
        };
    }
}
