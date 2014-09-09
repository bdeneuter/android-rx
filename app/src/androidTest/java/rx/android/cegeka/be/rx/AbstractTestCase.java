package rx.android.cegeka.be.rx;

import android.test.AndroidTestCase;

import be.cegeka.android.rx.infrastructure.RotationSensor;
import rx.Observable;

import static org.mockito.MockitoAnnotations.initMocks;

public class AbstractTestCase extends AndroidTestCase {

    protected RotationSensor rotationSensorMock = new RotationSensor() {
        @Override
        public Observable<Double> connectX() {
            return Observable.empty();
        }

        @Override
        public Observable<Double> connectY() {
            return Observable.empty();
        }

        @Override
        public Observable<Double> connectZ() {
            return Observable.empty();
        }
    };

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getContext().getCacheDir().toString());
        initMocks(this);
    }
}
