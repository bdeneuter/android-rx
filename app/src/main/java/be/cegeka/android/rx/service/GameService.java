package be.cegeka.android.rx.service;


import be.cegeka.android.rx.domain.Game;
import rx.Observable;
import rx.Subscriber;

public class GameService {

    public Observable<Game> createNewGame() {
        return Observable.create(new Observable.OnSubscribe<Game>() {
            @Override
            public void call(Subscriber<? super Game> subscriber) {
                subscriber.onNext(new Game());
                subscriber.onCompleted();
            }
        });
    }

}
