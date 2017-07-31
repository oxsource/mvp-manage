package com.oxandon.demo;

import android.support.multidex.MultiDexApplication;

import com.oxandon.found.arch.impl.MvpSdk;
import com.oxandon.found.env.FoundEnvironment;
import com.oxandon.mvp.annotation.Dispatcher;

/**
 * Created by peng on 2017/5/22.
 */
@Dispatcher(value = "MainDispatcher")
public class MainApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        FoundEnvironment.inject(this, true);
        MvpSdk.bind(MainMvpDispatcher.class);
    }
}