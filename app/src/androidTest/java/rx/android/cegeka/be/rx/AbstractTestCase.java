package rx.android.cegeka.be.rx;

import android.test.AndroidTestCase;

import static org.mockito.MockitoAnnotations.initMocks;

public class AbstractTestCase extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getContext().getCacheDir().toString());
        initMocks(this);
    }
}
