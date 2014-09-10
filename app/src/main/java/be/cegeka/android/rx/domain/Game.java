package be.cegeka.android.rx.domain;

import java.util.List;
import java.util.Random;

import be.cegeka.android.rx.infrastructure.RotationSensor;
import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.observables.ConnectableObservable;

import static be.cegeka.android.rx.domain.Army.ALLIED;
import static be.cegeka.android.rx.domain.Army.GERMAN;
import static be.cegeka.android.rx.domain.Orientation.BOTTOM;
import static com.google.common.collect.Lists.newArrayList;
import static java.util.concurrent.TimeUnit.SECONDS;
import static rx.Observable.from;
import static rx.Observable.just;
import static rx.Observable.merge;
import static rx.Observable.timer;

public class Game {

    private Random random = new Random();
    private Board board;
    private CollisionDetection collisionDetection;

    private Plane plane;
    private List<Plane> enemies;
    private ConnectableObservable<Plane> planes;

    public Game(Board board, RotationSensor rotationSensor) {
        this.board = board;
        plane = new Plane(ALLIED, new SensorControlWheel(rotationSensor), board);
        enemies = createEnemies(15);
        planes = merge(just(plane), from(enemies).zipWith(timer(5, 3, SECONDS), toPlane())).publish();
        collisionDetection = new CollisionDetection();
    }

    private Func2<Plane, Long, Plane> toPlane() {
        return new Func2<Plane, Long, Plane>() {
            @Override
            public Plane call(Plane plane, Long aLong) {
                return plane;
            }
        };
    }

    public Subscription start() {
        Subscription subscription = collisionDetection.start(plane, planes());
        planes().connect();
        return subscription;
    }

    public ConnectableObservable<Plane> planes() {
        return planes;
    }

    private List<Plane> createEnemies(int count) {
        List<Plane> enemies = newArrayList();
        for (int i=0; i<count; i++) {
            enemies.add(new Plane(GERMAN, new Position(randomXPositionOnTheBoard(), 1), BOTTOM, new AIControlWheel(), board));
        }
        return enemies;
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
