package be.cegeka.android.rx.presentation;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import be.cegeka.android.rx.R;
import be.cegeka.android.rx.domain.Game;
import be.cegeka.android.rx.domain.Position;
import rx.Observable;
import rx.functions.Action1;

import static be.cegeka.android.rx.domain.Game.toPlane;
import static be.cegeka.android.rx.domain.Plane.toPositions;
import static be.cegeka.android.rx.infrastructure.BeanProvider.gameService;

public class MainFragment extends Fragment {

    private static final float IMAGE_WIDTH_DP = 150;
    private static final float IMAGE_HEIGHT_DP = 125;

    private Observable<Game> game;
    private float deltaX;
    private float deltaY;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        final float scale = getResources().getDisplayMetrics().density;
        deltaX = (IMAGE_WIDTH_DP * scale)/2;
        deltaY = (IMAGE_HEIGHT_DP * scale)/2;

        /* Cache the stream so that only one game is created for the retained fragment.
         * Each subscriber that subscribes to this Observable will receive the same instance of the game.
         */
        game = gameService().createNewGame().cache();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        final ImageView planeView = (ImageView) getView().findViewById(R.id.plane);

        /*
        * Show the plane image to the position of the plane.
        *
        *   1. Map the game to the plane.
        *   2. Flat map the stream of positions on the plane
        *   3. Subscribe to the stream of positions of the plane. Adjust the location of the plane image on each item emitted.
        *
        * ATTENTION!!! Subscribe on a background thread and observe on the main thread
        * (HINT: use Schedulers and AndroidSchedulers)
        * */
        game.map(toPlane()).flatMap(toPositions()).subscribe(new Action1<Position>() {
            @Override
            public void call(Position position) {
                planeView.setX(position.x - deltaX);
                planeView.setY(position.y - deltaY);
            }
        });
    }


}
