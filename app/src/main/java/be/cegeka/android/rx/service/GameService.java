package be.cegeka.android.rx.service;


import be.cegeka.android.rx.domain.Board;
import be.cegeka.android.rx.domain.Game;
import rx.Observable;
import rx.Subscriber;

public class GameService {

    private Board board;

    public GameService(Board board) {
        this.board = board;
    }

    public Observable<Game> createNewGame() {
        return Observable.create(new Observable.OnSubscribe<Game>() {
            @Override
            public void call(Subscriber<? super Game> subscriber) {
                subscriber.onNext(new Game(board));
                subscriber.onCompleted();
            }
        });
    }

}
