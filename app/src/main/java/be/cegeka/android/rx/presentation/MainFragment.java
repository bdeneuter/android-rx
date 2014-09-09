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
import static be.cegeka.android.rx.infrastructure.BeanProvider.gameService;
import static com.google.common.collect.Lists.newArrayList;
import static rx.android.schedulers.AndroidSchedulers.mainThread;
import static rx.schedulers.Schedulers.computation;

public class MainFragment extends Fragment {

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
;
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

    private void handleNewPlane(final Plane plane) {
        final ImageView view = createView(plane);
        subscriptions.add(
                plane.position()
                     .subscribeOn(computation())
                     .observeOn(mainThread())
                     .subscribe(new Observer<Position>() {

                         @Override
                         public void onCompleted() {
                            Log.d("MainFragment", "view removed for plane " + plane);
                            getView().removeView(view);
                         }

                         @Override
                         public void onError(Throwable e) {

                         }

                         @Override
                         public void onNext(Position position) {
                            view.setX(position.x - view.getWidth() / 2);
                            view.setY(position.y - view.getHeight() / 2);
                            view.setVisibility(View.VISIBLE);
                         }
                     })

        );
    }

    private ImageView createView(Plane plane) {
        ImageView view = new ImageView(getActivity());
        view.setId(plane.getId());
        view.setImageResource(plane.getArmy() == ALLIED ? R.drawable.custom_plane : R.drawable.custom_enemy);
        view.setVisibility(INVISIBLE);
        getView().addView(view, 0, new LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        for(Subscription subscription: subscriptions) {
            subscription.unsubscribe();
        }
        getView().removeAllViews();
    }

    @Override
    public ViewGroup getView() {
        return (ViewGroup) super.getView();
    }
}
