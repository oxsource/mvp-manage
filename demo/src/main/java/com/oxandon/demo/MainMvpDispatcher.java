package com.oxandon.demo;

import android.support.annotation.NonNull;

import com.oxandon.demo.member.presenter.MemberPresenter;
import com.oxandon.found.mvp.impl.MvpDispatcher;
import com.oxandon.found.mvp.protocol.IMvpPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by peng on 2017/5/20.
 */

public class MainMvpDispatcher extends MvpDispatcher {
    private static ThreadLocal<MainMvpDispatcher> local = new ThreadLocal<>();
    private static MainMvpDispatcher instance;

    public static MainMvpDispatcher get() {
        if (null == local.get()) {
            synchronized (MainMvpDispatcher.class) {
                if (null == instance) {
                    instance = new MainMvpDispatcher();
                }
            }
            local.set(instance);
        }
        return local.get();
    }

    @NonNull
    @Override
    protected List<Class<? extends IMvpPresenter>> support() {
        List<Class<? extends IMvpPresenter>> list = new ArrayList<>();
        list.add(MemberPresenter.class);
        return list;
    }
}
