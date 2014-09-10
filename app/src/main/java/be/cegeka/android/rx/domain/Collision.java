package be.cegeka.android.rx.domain;

import com.google.common.base.MoreObjects;

public class Collision {

    public final Bounds bounds;

    public final Bounds otherBounds;

    public Collision(Bounds bounds, Bounds otherBounds) {
        this.bounds = bounds;
        this.otherBounds = otherBounds;
    }

    public boolean isCollision() {
        return bounds.intersect(otherBounds);
    }

    public void destroyPlanes() {
        bounds.plane.destroy();
        otherBounds.plane.destroy();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("plane", bounds.plane).add("other", otherBounds.plane).toString();
    }
}
