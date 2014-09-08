package rx.android.cegeka.be.rx.presentation;

import android.graphics.Point;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import be.cegeka.android.rx.R;
import be.cegeka.android.rx.presentation.MainActivity;

import static com.jayway.awaitility.Awaitility.await;
import static org.fest.assertions.api.Assertions.assertThat;

public class MainActivityTest extends ActivityInstrumentationTestCase2 {

    private int screenWidth;
    private int screenHeight;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        calculateScreenSize();
    }

    public void testPlaneShouldBeCentered() {
        await().until(new Runnable() {
            @Override
            public void run() {
                View planeView = getActivity().findViewById(R.id.plane);
                assertThat(planeView.getX()).isEqualTo((screenWidth - planeView.getWidth())/2);
                assertThat(planeView.getY()).isEqualTo((screenHeight - planeView.getHeight())/2);
            }
        });
    }

    private void calculateScreenSize() {
        Point size = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
    }
}
