package be.cegeka.android.rx.domain;

import rx.Observable;

public interface ControlWheel {

    Observable<Direction> direction();

}
