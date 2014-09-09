package be.cegeka.android.rx.domain;

import org.mockito.InOrder;
import org.mockito.Mock;

import be.cegeka.android.rx.infrastructure.PixelConverter;
import be.cegeka.android.rx.mocks.RotationSensorMock;
import rx.Observable;
import rx.Observer;
import rx.android.cegeka.be.rx.AbstractTestCase;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static rx.Observable.just;

public class PlaneTest extends AbstractTestCase {

    @Mock
    private Observer<Position> subscriber;

    private Plane plane;
    private PixelConverter pixelConverter;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        pixelConverter = new PixelConverter(getContext());
    }

    public void testPosition_ThenReturnInitialPosition() {
        // GIVEN
        Position position = new Position(500, 600);
        plane = new Plane(new Board(1000, 1000), position, new ControlWheel(new RotationSensorMock()), pixelConverter);

        // WHEN
        plane.position().subscribe(subscriber);

        // THEN
        verify(subscriber).onNext(position);
    }

    public void testPosition_listenToControlWheel() {
        // GIVEN
        Position position = new Position(500, 600);
        double moveRight = 15d;
        plane = new Plane(new Board(1000, 1000), position, new ControlWheel(new RotationSensorMock(just(moveRight), Observable.<Double>empty(), Observable.<Double>empty())), pixelConverter);

        // WHEN
        plane.position().subscribe(subscriber);

        // THEN
        InOrder inOrder = inOrder(subscriber);
        inOrder.verify(subscriber).onNext(position);
        inOrder.verify(subscriber).onNext(new Position(position.x + pixelConverter.toPixels(10), position.y));
    }
}