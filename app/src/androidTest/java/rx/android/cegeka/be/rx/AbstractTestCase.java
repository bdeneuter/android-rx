package rx.android.cegeka.be.rx;

import junit.framework.TestCase;

import static org.mockito.MockitoAnnotations.initMocks;

public class AbstractTestCase extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        initMocks(this);
    }
}
