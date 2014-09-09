package rx.android.cegeka.be.rx;

import android.test.AndroidTestCase;

import org.mockito.Mock;

import be.cegeka.android.rx.infrastructure.RotationSensor;
import rx.Observable;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AbstractTestCase extends AndroidTestCase {

    @Mock
    protected RotationSensor rotationSensorMock;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getContext().getCacheDir().toString());
        initMocks(this);
        when(rotationSensorMock.connectX()).thenReturn(Observable.<Double>empty());
        when(rotationSensorMock.connectY()).thenReturn(Observable.<Double>empty());
        when(rotationSensorMock.connectZ()).thenReturn(Observable.<Double>empty());
    }
}
