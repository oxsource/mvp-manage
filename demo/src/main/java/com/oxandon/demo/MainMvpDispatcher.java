package com.oxandon.demo;

import android.support.annotation.NonNull;

import com.oxandon.demo.member.presenter.MemberPresenter;
import com.oxandon.found.arch.impl.MvpDispatcher;
import com.oxandon.found.arch.protocol.IMvpPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by peng on 2017/5/20.
 */

public class MainMvpDispatcher extends MvpDispatcher {

    @NonNull
    @Override
    protected List<Class<? extends IMvpPresenter>> support() {
        List<Class<? extends IMvpPresenter>> list = new ArrayList<>();
        list.add(MemberPresenter.class);
        return list;
    }
}
