package be.cegeka.android.rx.presentation;

import be.cegeka.android.rx.domain.Position;

import static com.google.common.base.MoreObjects.toStringHelper;

public class PositionTO {

    public final int id;
    public final int x;
    public final int y;

    public PositionTO(int id, Position position) {
        this.id = id;
        this.x = position.x;
        this.y = position.y;
    }

    @Override
    public String toString() {
        return toStringHelper(this).add("id", id).add("x", x).add("y", y).toString();
    }
}
