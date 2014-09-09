package be.cegeka.android.rx.domain;

import be.cegeka.android.rx.infrastructure.PixelConverter;
import be.cegeka.android.rx.infrastructure.RotationSensor;
import rx.functions.Func1;

public class Game {

    private Plane plane;

    public Game(Board board, RotationSensor rotationSensor, PixelConverter pixelConverter) {
        plane = new Plane(board, new ControlWheel(rotationSensor), pixelConverter);
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
