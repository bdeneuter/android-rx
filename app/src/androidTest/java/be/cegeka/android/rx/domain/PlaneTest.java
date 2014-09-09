package be.cegeka.android.rx.domain;

import org.mockito.Mock;

import be.cegeka.android.rx.infrastructure.PixelConverter;
import rx.Observable;
import rx.Subscriber;
import rx.android.cegeka.be.rx.AbstractTestCase;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PlaneTest extends AbstractTestCase {

    @Mock
    private ControlWheel controlWheel;

    @Mock
    private Subscriber<Position> subscriber;

    private Plane plane;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        when(controlWheel.direction()).thenReturn(Observable.<Direction>empty());
    }

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