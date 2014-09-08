package be.cegeka.android.rx.infrastructure;

import java.util.concurrent.atomic.AtomicInteger;

public class Sequence {

    private static AtomicInteger sequence = new AtomicInteger(1);

    public static int nextInt() {
        return sequence.getAndIncrement();
    }

}
