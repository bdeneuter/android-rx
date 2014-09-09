package be.cegeka.android.rx.presentation;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import be.cegeka.android.rx.R;
import be.cegeka.android.rx.domain.Game;
import be.cegeka.android.rx.domain.Plane;
import be.cegeka.android.rx.domain.Position;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

import static be.cegeka.android.rx.domain.Game.toPlane;
import static be.cegeka.android.rx.domain.Plane.toPositions;
import static be.cegeka.android.rx.infrastructure.BeanProvider.gameService;
import static be.cegeka.android.rx.infrastructure.BeanProvider.pixelConverter;
import static rx.android.schedulers.AndroidSchedulers.mainThread;
import static rx.schedulers.Schedulers.computation;
import static rx.subscriptions.Subscriptions.empty;

public class MainFragment extends Fragment {

    private static final float IMAGE_WIDTH_DP = 75;
    private static final float IMAGE_HEIGHT_DP = 62;

    private Observable<Game> game;

    private float deltaX;
    private float deltaY;

    private Subscription subscription = empty();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        deltaX = pixelConverter().toPixels(IMAGE_WIDTH_DP)/2;
        deltaY = pixelConverter().toPixels(IMAGE_HEIGHT_DP)/2;

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
        subscription = game.map(toPlane())
                            .flatMap(toPositions())
                            .subscribeOn(computation())
                            .observeOn(mainThread())
                            .subscribe(new Action1<Position>() {
                                @Override
                                public void call(Position position) {
                                    planeView.setX(position.x - deltaX);
                                    planeView.setY(position.y - deltaY);
                                }
                            });
    }

    private Func1<Plane, Observable<PositionTO>> toPositionTOs() {
        return new Func1<Plane, Observable<PositionTO>>() {
            @Override
            public Observable<PositionTO> call(final Plane plane) {
                return plane.position().map(toPositionTO(plane.getId()));
            }
        };
    }

    private Func1<Position, PositionTO> toPositionTO(final int planeId) {
        return new Func1<Position, PositionTO>() {
            @Override
            public PositionTO call(Position position) {
                return new PositionTO(planeId, position);
            }
        };
    }

    @Override
    public void onPause() {
        super.onPause();
        subscription.unsubscribe();
    }
}
