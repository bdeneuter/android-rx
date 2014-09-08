package be.cegeka.android.rx.presentation;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.cegeka.android.rx.R;
import be.cegeka.android.rx.domain.Game;
import be.cegeka.android.rx.domain.Position;
import rx.Observable;
import rx.functions.Action1;

import static be.cegeka.android.rx.domain.Game.toPlane;
import static be.cegeka.android.rx.domain.Plane.toPositions;
import static be.cegeka.android.rx.infrastructure.BeanProvider.gameService;
import static rx.android.schedulers.AndroidSchedulers.mainThread;
import static rx.schedulers.Schedulers.computation;

public class MainFragment extends Fragment {

    private Observable<Game> game;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        /*
        Retain the game. By caching it
         */
        game = gameService().createNewGame().cache();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final View planeView = view.findViewById(R.id.plane);
        game.map(toPlane())
            .flatMap(toPositions())
            .subscribeOn(computation())
            .observeOn(mainThread())
            .subscribe(new Action1<Position>() {
                @Override
                public void call(Position position) {
                    planeView.setX(position.x - planeView.getWidth()/2);
                    planeView.setY(position.y - planeView.getHeight()/2);
                }
            });
    }


}
