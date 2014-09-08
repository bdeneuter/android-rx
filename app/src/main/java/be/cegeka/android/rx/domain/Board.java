package be.cegeka.android.rx.domain;

public class Board {

    public final int width;
    public final int height;

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Position getCenter() {
        return new Position(width / 2, height / 2);
    }
}
