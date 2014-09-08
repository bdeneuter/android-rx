package be.cegeka.android.rx.infrastructure;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import be.cegeka.android.rx.domain.Board;

public class BeanProvider {

    private static Board board;
    private static Context applicationContext;

    public static void init(Context applicationContext) {
        BeanProvider.applicationContext = applicationContext;
    }

    public static Board board() {
        if (board == null) {
            WindowManager wm = (WindowManager) applicationContext.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            board = new Board(point.x, point.y);
        }
        return board;
    }

}
