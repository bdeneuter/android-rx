package be.cegeka.android.rx.domain;

import be.cegeka.android.rx.infrastructure.PixelConverter;
import be.cegeka.android.rx.infrastructure.RotationSensor;
import rx.Observable;
import rx.functions.Func1;

import static java.util.concurrent.TimeUnit.SECONDS;
import static rx.Observable.timer;

public class Game {

    private Board board;
    private PixelConverter pixelConverter;

    private Plane plane;
    private Observable<Plane> enemies;

    public Game(Board board, RotationSensor rotationSensor, PixelConverter pixelConverter) {
        this.board = board;
        this.pixelConverter = pixelConverter;

        plane = new Plane(board, new SensorControlWheel(rotationSensor), pixelConverter);
        enemies = timer(5, 10, SECONDS).map(toEnemy());
    }

    private Func1<Long, Plane> toEnemy() {
        return new Func1<Long, Plane>() {
            @Override
            public Plane call(Long aLong) {
                return new Plane(board, new Position(board.getCenter().x, 1), new AIControlWheel(), pixelConverter);
            }
        };
    }

    public Observable<Plane> getEnemies() {
        return enemies;
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
