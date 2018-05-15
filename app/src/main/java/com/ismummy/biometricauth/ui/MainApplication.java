package com.ismummy.biometricauth.ui;

import android.app.Application;

import com.ismummy.biometricauth.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Main application
 */

@SuppressWarnings("ALL")
public class MainApplication extends Application {

    private static final ExecutorService executorService = Executors.newCachedThreadPool();
    private static MainApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/Lato-Regular.ttf")
                .setFontAttrId(R.attr.fontPath).build());
    }

     public static ExecutorService getExecutorService() {
        return executorService;
    }

    public static MainApplication getInstance() {
        return instance;
    }
}
