package rx.android.cegeka.be.rx.service;

import org.mockito.InOrder;
import org.mockito.Mock;

import be.cegeka.android.rx.domain.Board;
import be.cegeka.android.rx.domain.Game;
import be.cegeka.android.rx.infrastructure.PixelConverter;
import be.cegeka.android.rx.service.GameService;
import rx.Subscriber;
import rx.android.cegeka.be.rx.AbstractTestCase;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class GameServiceTest extends AbstractTestCase {

    public static final int WIDTH = 500;
    public static final int HEIGHT = 600;

    @Mock
    private Subscriber<Game> observer;

    private GameService gameService;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        gameService = new GameService(new Board(WIDTH, HEIGHT), rotationSensorMock, new PixelConverter(getContext()));
    }

    public void testCreateNewGame() {

        // WHEN
        gameService.createNewGame().subscribe(observer);

        // THEN
        InOrder inOrder = inOrder(observer);
        inOrder.verify(observer).onNext(isA(Game.class));
        inOrder.verify(observer).onCompleted();
        verify(observer, never()).onError(any(Exception.class));
    }

}
