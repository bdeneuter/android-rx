package be.cegeka.android.rx;

import android.app.Application;

import be.cegeka.android.rx.infrastructure.BeanProvider;

public class RxApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        BeanProvider.init(getApplicationContext());
    }
}
