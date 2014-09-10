package be.cegeka.android.rx.domain;

import android.graphics.Rect;

public class Bounds {

    public final Plane plane;
    public final Rect rect;

    public Bounds(Plane plane, Rect rect) {
        this.plane = plane;
        this.rect = rect;
    }

    public boolean intersect(Bounds otherBounds) {
        return rect.intersect(otherBounds.rect);
    }
}
