package be.cegeka.android.rx.infrastructure;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import be.cegeka.android.rx.domain.Board;
import be.cegeka.android.rx.service.GameService;

public class BeanProvider {

    private static Board board;
    private static Context applicationContext;
    private static GameService gameService;
    private static RotationSensor rotationSensor;
    private static PixelConverter pixelConverter;

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

    public static RotationSensor rotationSensor() {
        if (rotationSensor == null) {
            rotationSensor = new RotationSensor(applicationContext);
        }
        return rotationSensor;
    }

    public static GameService gameService() {
        if (gameService == null) {
            gameService = new GameService(board(), rotationSensor(), pixelConverter());
        }
        return gameService;
    }

    public static PixelConverter pixelConverter() {
        if (pixelConverter == null) {
            pixelConverter = new PixelConverter(applicationContext);
        }
        return pixelConverter;
    }
}
