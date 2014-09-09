package be.cegeka.android.rx.domain;

import org.mockito.Mock;

import be.cegeka.android.rx.mocks.RotationSensorMock;
import rx.Observable;
import rx.Observer;
import rx.android.cegeka.be.rx.AbstractTestCase;

import static be.cegeka.android.rx.domain.Direction.BACKWARDS;
import static be.cegeka.android.rx.domain.Direction.FORWARD;
import static be.cegeka.android.rx.domain.Direction.LEFT;
import static be.cegeka.android.rx.domain.Direction.RIGHT;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static rx.Observable.just;

public class ControlWheelTest extends AbstractTestCase {

    @Mock
    private Observer<Direction> observer;

    private ControlWheel controlWheel;

    public void testDirection_WhenRotateX_PositiveAngleGreaterThen10_ThenMoveRight() throws Exception {
        // GIVEN
        double rotation = 15;
        controlWheel = new ControlWheel(new RotationSensorMock(just(rotation), Observable.<Double>empty(), Observable.<Double>empty()));

        // WHEN
        controlWheel.direction().subscribe(observer);

        // THEN
        verify(observer).onNext(RIGHT);

    }

    public void testDirection_WhenRotateX_PositiveAngleSmallerThen10_ThenIgnore() throws Exception {
        // GIVEN
        double rotation = 9;
        controlWheel = new ControlWheel(new RotationSensorMock(just(rotation), Observable.<Double>empty(), Observable.<Double>empty()));

        // WHEN
        controlWheel.direction().subscribe(observer);

        // THEN
        verify(observer, never()).onNext(RIGHT);

    }

    public void testDirection_WhenRotateX_NegativeAngleGreaterThen10_ThenMoveLeft() throws Exception {
        // GIVEN
        double rotation = -15;
        controlWheel = new ControlWheel(new RotationSensorMock(just(rotation), Observable.<Double>empty(), Observable.<Double>empty()));

        // WHEN
        controlWheel.direction().subscribe(observer);

        // THEN
        verify(observer).onNext(LEFT);

    }

    public void testDirection_WhenRotateX_NegativeAngleSmallerThen10_ThenIgnore() throws Exception {
        // GIVEN
        double rotation = -9;
        controlWheel = new ControlWheel(new RotationSensorMock(just(rotation), Observable.<Double>empty(), Observable.<Double>empty()));

        // WHEN
        controlWheel.direction().subscribe(observer);

        // THEN
        verify(observer, never()).onNext(LEFT);

    }

    public void testDirection_WhenRotateY_PositiveAngleGreaterThen5_ThenMoveForward() throws Exception {
        // GIVEN
        double rotation = 8;
        controlWheel = new ControlWheel(new RotationSensorMock(Observable.<Double>empty(), just(rotation), Observable.<Double>empty()));

        // WHEN
        controlWheel.direction().subscribe(observer);

        // THEN
        verify(observer).onNext(FORWARD);

    }

    public void testDirection_WhenRotateY_PositiveAngleSmallerThen5_ThenIgnore() throws Exception {
        // GIVEN
        double rotation = 4;
        controlWheel = new ControlWheel(new RotationSensorMock(Observable.<Double>empty(), just(rotation), Observable.<Double>empty()));

        // WHEN
        controlWheel.direction().subscribe(observer);

        // THEN
        verify(observer, never()).onNext(FORWARD);

    }

    public void testDirection_WhenRotateY_NegativeAngleGreaterThen5_ThenMoveBackwards() throws Exception {
        // GIVEN
        double rotation = -8;
        controlWheel = new ControlWheel(new RotationSensorMock(Observable.<Double>empty(), just(rotation), Observable.<Double>empty()));

        // WHEN
        controlWheel.direction().subscribe(observer);

        // THEN
        verify(observer).onNext(BACKWARDS);

    }

    public void testDirection_WhenRotateY_NegativeAngleSmallerThen5_ThenIgnore() throws Exception {
        // GIVEN
        double rotation = -4;
        controlWheel = new ControlWheel(new RotationSensorMock(Observable.<Double>empty(), just(rotation), Observable.<Double>empty()));

        // WHEN
        controlWheel.direction().subscribe(observer);

        // THEN
        verify(observer, never()).onNext(BACKWARDS);

    }

}