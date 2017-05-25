package com.oxandon.found.arch.impl;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.oxandon.found.arch.protocol.IMvpMessage;
import com.oxandon.found.arch.protocol.IMvpView;

import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by peng on 2017/5/22.
 */

public class MvpSubscriber<T> extends DisposableSubscriber<T> {
    private MvpPresenter presenter;
    private IMvpMessage message;

    public MvpSubscriber(@NonNull MvpPresenter presenter, @NonNull IMvpMessage message) {
        this.presenter = presenter;
        this.message = message;
    }

    public MvpSubscriber(@NonNull MvpPresenter presenter, @NonNull IMvpMessage message, String loading, boolean cancel) {
        message.from().appendParams(IMvpView.STR_LOADING, loading);
        message.from().appendParams(IMvpView.BOOL_LOADING, cancel);
        this.presenter = presenter;
        this.message = message;
    }

    @Override
    protected void onStart() {
        super.onStart();
        MvpMessage.Builder builder = new MvpMessage.Builder();
        IMvpMessage msg = builder.reverse(message()).what(IMvpMessage.WHAT_START).build();
        presenter().dispatcher().dispatchToView(msg);
    }

    @CallSuper
    @Override
    public void onNext(T o) {
    }

    @CallSuper
    @Override
    public void onError(Throwable t) {
        String text = t.getMessage();
        if (TextUtils.isEmpty(text)) {
            text = defaultErrorMsg();
        }
        MvpMessage.Builder builder = new MvpMessage.Builder();
        builder.reverse(message()).what(IMvpMessage.WHAT_FAILURE).msg(text);
        presenter().dispatcher().dispatchToView(builder.build());
        doFinishedWork();
    }

    @Override
    public void onComplete() {
        doFinishedWork();
    }

    protected void doFinishedWork() {
        //从消息任务对列中移除任务
        presenter().removeTask(message());
        //构建任务完成消息并通知
        MvpMessage.Builder builder = new MvpMessage.Builder();
        IMvpMessage msg = builder.reverse(message()).what(IMvpMessage.WHAT_FINISH).build();
        presenter().dispatcher().dispatchToView(msg);
        message = null;
        presenter = null;
    }

    protected String defaultErrorMsg() {
        return "请求出错";
    }

    protected MvpPresenter presenter() {
        return presenter;
    }

    protected IMvpMessage message() {
        return message;
    }
}