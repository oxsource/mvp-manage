package com.oxandon.demo;

import android.app.Application;

import com.oxandon.found.env.FoundEnvironment;

/**
 * Created by peng on 2017/5/22.
 */

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FoundEnvironment.inject(this, true);
    }
}
