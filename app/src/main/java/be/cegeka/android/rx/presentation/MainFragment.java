package be.cegeka.android.rx.presentation;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import be.cegeka.android.rx.R;
import be.cegeka.android.rx.domain.Game;
import be.cegeka.android.rx.domain.Plane;
import be.cegeka.android.rx.domain.Position;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Action1;

import static android.view.View.INVISIBLE;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.FrameLayout.LayoutParams;
import static be.cegeka.android.rx.domain.Army.ALLIED;
import static be.cegeka.android.rx.domain.Game.toPlanes;
import static be.cegeka.android.rx.domain.Orientation.BOTTOM;
import static be.cegeka.android.rx.infrastructure.BeanProvider.gameService;
import static be.cegeka.android.rx.infrastructure.BeanProvider.pixelConverter;
import static com.google.common.collect.Lists.newArrayList;
import static rx.android.schedulers.AndroidSchedulers.mainThread;
import static rx.schedulers.Schedulers.computation;

public class MainFragment extends Fragment {

    public static final int DURATION = 1500;
    private Observable<Game> game;
    private List<Subscription> subscriptions = newArrayList();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

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
        observePlanes();
        startGame();
    }

    @Override
    public void onPause() {
        super.onPause();
        for(Subscription subscription: subscriptions) {
            subscription.unsubscribe();
        }
        getView().removeAllViews();
    }

    private void observePlanes() {
        subscriptions.add(
                game.flatMap(toPlanes())
                        .subscribeOn(computation())
                        .observeOn(mainThread())
                        .subscribe(new Action1<Plane>() {
                            @Override
                            public void call(Plane plane) {
                                handleNewPlane(plane);
                            }
                        }));
    }

    private void startGame() {
        subscriptions.add(
                game.subscribeOn(computation())
                        .subscribe(new Action1<Game>() {
                            @Override
                            public void call(Game game) {
                                subscriptions.add(game.start());
                            }
                        }));
    }

    private void handleNewPlane(final Plane plane) {
        final ImageView view = createView(plane);
        handleDestruction(plane, view);
        handlePositionChanges(plane, view);
    }

    private void handlePositionChanges(final Plane plane, final ImageView view) {
        subscriptions.add(
                plane.position()
                     .subscribeOn(computation())
                     .observeOn(mainThread())
                     .subscribe(new Observer<Position>() {

                         @Override
                         public void onCompleted() {
                             Log.d("Plane", "Handle onComplete position stream " + plane.getId());
                             removeView(view);
                         }

                         @Override
                         public void onError(Throwable e) {

                         }

                         @Override
                         public void onNext(Position position) {
                             view.setX(pixelConverter().toPixels(position.x) - view.getWidth() / 2);
                             view.setY(pixelConverter().toPixels(position.y) - view.getHeight() / 2);
                             view.setVisibility(View.VISIBLE);
                         }
                     })

        );
    }

    private void removeView(final ImageView view) {
        view.animate().scaleX(0).setDuration(300).start();
        view.animate().scaleY(0).setDuration(300).withEndAction(new Runnable() {
            @Override
            public void run() {
                getView().removeView(view);
            }
        }).start();
    }

    private void handleDestruction(final Plane plane, final ImageView view) {
        subscriptions.add(plane.destroyed()
                               .subscribeOn(computation())
                               .observeOn(mainThread())
                                .subscribe(new Observer<Boolean>() {
                                    @Override
                                    public void onCompleted() {
                                        Log.d("Plane", "Handle onComplete destroy stream " + plane.getId());
                                        createExplosionFor(view);
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(Boolean aBoolean) {

                                    }
                                }));
    }

    private void createExplosionFor(View plane) {
        final ImageView view = new ImageView(getActivity());
        view.setImageResource(R.drawable.custom_exlposion);
        view.setX(plane.getX());
        view.setY(plane.getY());
        view.setScaleX(0);
        view.setScaleY(0);
        getView().addView(view, 0, new LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        view.animate().setDuration(DURATION).scaleX(1).withEndAction(new Runnable() {
            @Override
            public void run() {
                getView().removeView(view);
            }
        }).start();
        view.animate().setDuration(1500).scaleY(1).start();
    }

    private ImageView createView(Plane plane) {
        ImageView view = new ImageView(getActivity());
        view.setId(plane.getId());
        view.setImageResource(plane.getArmy() == ALLIED ? R.drawable.custom_allies : R.drawable.custom_germany);
        view.setVisibility(INVISIBLE);
        view.setRotation(calculateRotation(plane));
        getView().addView(view, 0, new LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        return view;
    }

    private float calculateRotation(Plane plane) {
        if (plane.getOrientation() == BOTTOM) {
            return 180;
        }
        return 0;
    }

    @Override
    public ViewGroup getView() {
        return (ViewGroup) super.getView();
    }
}
