package rx.android.cegeka.be.rx.service;

import junit.framework.TestCase;

import org.mockito.InOrder;
import org.mockito.Mock;

import be.cegeka.android.rx.domain.Game;
import be.cegeka.android.rx.service.GameService;
import rx.Subscriber;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class GameServiceTest extends TestCase {

    @Mock
    private Subscriber<Game> subscriber;

    private GameService gameService = new GameService();

    public void testCreateNewGame() {

        // WHEN
        gameService.createNewGame().subscribe(subscriber);

        // THEN
        InOrder inOrder = inOrder(subscriber);
        inOrder.verify(subscriber).onNext(isA(Game.class));
        inOrder.verify(subscriber).onCompleted();
        verify(subscriber, never()).onError(any(Exception.class));
    }

}
