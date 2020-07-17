package com.news.studentpioneer;

import android.app.Application;

import io.customerly.Customerly;

public class CustomApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Customerly.configure(this, "f63e20bb");
    }
}