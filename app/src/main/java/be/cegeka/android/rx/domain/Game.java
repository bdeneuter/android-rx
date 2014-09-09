package be.cegeka.android.rx.domain;

import java.util.Random;

import be.cegeka.android.rx.infrastructure.RotationSensor;
import rx.Observable;
import rx.functions.Func1;

import static be.cegeka.android.rx.domain.Army.ALLIED;
import static be.cegeka.android.rx.domain.Army.GERMAN;
import static be.cegeka.android.rx.domain.Orientation.BOTTOM;
import static java.util.concurrent.TimeUnit.SECONDS;
import static rx.Observable.just;
import static rx.Observable.merge;
import static rx.Observable.timer;

public class Game {

    private Random random = new Random();
    private Board board;

    private Plane plane;

    public Game(Board board, RotationSensor rotationSensor) {
        this.board = board;
        plane = new Plane(ALLIED, new SensorControlWheel(rotationSensor), board);
    }

    public Observable<Plane> planes() {
        return merge(just(plane), enemies());
    }

    private Observable<Plane> enemies() {
        return timer(5, 3, SECONDS).map(toEnemy());
    }

    private Func1<Long, Plane> toEnemy() {
        return new Func1<Long, Plane>() {
            @Override
            public Plane call(Long aLong) {
                return new Plane(GERMAN, new Position(randomXPositionOnTheBoard(), 1), BOTTOM, new AIControlWheel(), board);
            }
        };
    }

    private int randomXPositionOnTheBoard() {
        return random.nextInt(board.width + 1);
    }

    public static Func1<Game, Observable<Plane>> toPlanes() {
        return new Func1<Game, Observable<Plane>>() {
            @Override
            public Observable<Plane> call(Game game) {
                return game.planes();
            }
        };
    }
}
