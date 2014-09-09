package be.cegeka.android.rx.domain;


import be.cegeka.android.rx.infrastructure.PixelConverter;
import rx.Observable;
import rx.functions.Func1;

import static be.cegeka.android.rx.infrastructure.Sequence.nextInt;

public class Plane {

    private int id = nextInt();
    private int delta;

    public Plane(Board board, ControlWheel controlWheel, PixelConverter pixelConverter) {
        this(board, board.getCenter(), controlWheel, pixelConverter);
    }

    public Plane(Board board, Position position, ControlWheel controlWheel, PixelConverter pixelConverter) {
        delta = pixelConverter.toPixels(10);
    }

    public int getId() {
        return id;
    }

    public Observable<Position> position() {
        throw new IllegalStateException("Implement me!!!!");
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
