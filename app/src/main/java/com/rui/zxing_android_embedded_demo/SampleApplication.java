package com.rui.zxing_android_embedded_demo;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 *
 */
public class SampleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}
