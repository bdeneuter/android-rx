package rx.android.cegeka.be.rx.domain;

import org.mockito.Mock;

import be.cegeka.android.rx.domain.Plane;
import be.cegeka.android.rx.domain.Position;
import rx.Subscriber;
import rx.android.cegeka.be.rx.AbstractTestCase;

import static org.mockito.Mockito.verify;

public class PlaneTest extends AbstractTestCase {

    @Mock
    private Subscriber<Position> subscriber;

    private Plane plane;

    public void testPosition() throws Exception {
        // GIVEN
        Position position = new Position(500, 600);
        plane = new Plane(position);

        // WHEN
        plane.position().subscribe(subscriber);

        // THEN
        verify(subscriber).onNext(position);
    }
}
