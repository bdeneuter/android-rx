package be.cegeka.android.rx.infrastructure;


import android.content.Context;

public class PixelConverter {

    private final float scale;

    public PixelConverter(Context context) {
        scale = context.getResources().getDisplayMetrics().density;
    }

    public float toPixels(float valueInDp) {
        return valueInDp * scale;
    }

    public int toPixels(int valueInDp) {
        return (int) (valueInDp * scale);
    }

}
