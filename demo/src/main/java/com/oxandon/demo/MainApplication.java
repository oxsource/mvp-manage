package com.oxandon.demo;

import android.support.multidex.MultiDexApplication;

import com.oxandon.mvp.annotation.Dispatcher;
import com.oxandon.mvp.arch.impl.MvpSdk;
import com.oxandon.mvp.env.FoundEnvironment;

/**
 * Created by peng on 2017/5/22.
 */
@Dispatcher(value = "MainDispatcher", live = true)
public class MainApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        FoundEnvironment.inject(this, true);
        MvpSdk.bind(MainDispatcher.class);
    }
}