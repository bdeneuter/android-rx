package be.cegeka.android.rx.domain;

import org.mockito.Mock;

import be.cegeka.android.rx.infrastructure.PixelConverter;
import rx.Observable;
import rx.Observer;
import rx.android.cegeka.be.rx.AbstractTestCase;

import static org.mockito.Mockito.verify;

public class PlaneTest extends AbstractTestCase {

    private ControlWheel controlWheel = new ControlWheel() {
        @Override
        public Observable<Direction> direction() {
            return Observable.<Direction>empty();
        }
    };

    @Mock
    private Observer<Position> subscriber;

    private Plane plane;

    public void testPosition() throws Exception {
        // GIVEN
        Position position = new Position(500, 600);
        plane = new Plane(new Board(1000, 1000), position, controlWheel, new PixelConverter(getContext()));

        // WHEN
        plane.position().subscribe(subscriber);

        // THEN
        verify(subscriber).onNext(position);
    }
}