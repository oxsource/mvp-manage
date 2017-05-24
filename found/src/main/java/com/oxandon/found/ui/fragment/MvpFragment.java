package com.oxandon.found.ui.fragment;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oxandon.found.arch.impl.MvpMessage;
import com.oxandon.found.arch.protocol.IMvpDispatcher;
import com.oxandon.found.arch.protocol.IMvpMessage;
import com.oxandon.found.arch.protocol.IMvpView;
import com.oxandon.found.ui.activity.MvpActivity;
import com.oxandon.found.ui.widget.AlertTemple;
import com.oxandon.found.ui.widget.IHintView;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by peng on 2017/5/22.
 */

public abstract class MvpFragment extends Fragment implements IFragment, IMvpView {
    private ViewGroup layout;
    private IHintView iHintView;
    private FragmentVisibility visibility;

    @CallSuper
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        visibility().onCreate(savedInstanceState);
        getFoundActivity().addToInterceptor(this);
        if (null != getEventBus()) {
            getEventBus().register(this);
        }
    }

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = onInflateLayout(inflater, container, savedInstanceState);
        iHintView = onBuildHintView();
        if (null != getDispatcher()) {
            getDispatcher().attach(this);
        }
        onInitViews(savedInstanceState);
        return layout;
    }

    protected FragmentVisibility visibility() {
        if (null == visibility) {
            visibility = new FragmentVisibility(this);
        }
        return visibility;
    }

    @Override
    public ViewGroup getLayout() {
        return layout;
    }

    @Override
    public IHintView getHintView() {
        return iHintView;
    }

    /*对用户可见时的回调*/
    @Override
    public void onVisible() {
    }

    /*对用户不可见时的回调*/
    @Override
    public void onInvisible() {
    }

    @Override
    public final boolean visible() {
        return visibility().visible();
    }

    @CallSuper
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        visibility().setUserVisibleHint(isVisibleToUser);
    }

    @CallSuper
    @Override
    public void onResume() {
        super.onResume();
        visibility().onResume();
    }

    @CallSuper
    @Override
    public void onPause() {
        visibility().onPause();
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        visibility().onSaveInstanceState(outState);
    }

    @Override
    public boolean backPressed() {
        return false;
    }

    @Override
    public final MvpFragment fragment() {
        return this;
    }

    @CallSuper
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != getDispatcher()) {
            getDispatcher().detach(this);
        }
        layout = null;
    }

    @CallSuper
    @Override
    public void onDestroy() {
        getFoundActivity().removeFromInterceptor(this);
        visibility().destroy();
        if (null != getEventBus()) {
            getEventBus().unregister(this);
        }
        super.onDestroy();
    }

    @Override
    public boolean dispatch(IMvpMessage msg) {
        String path = msg.to().path();
        switch (msg.what()) {
            case IMvpMessage.WHAT_START:
                onMvpStart(msg, path);
                break;
            case IMvpMessage.WHAT_FINISH:
                onMvpFinish(msg, path);
                break;
            case IMvpMessage.WHAT_PROGRESS:
                onMvpProgress(msg, path);
                break;
            case IMvpMessage.WHAT_FAILURE:
                onMvpFailure(msg, path);
                break;
            case IMvpMessage.WHAT_SUCCESS:
                onMvpSuccess(msg, path);
                break;
            case IMvpMessage.WHAT_PERMISSION:
                onMvpPermission(msg, path);
                break;
        }
        return false;
    }

    protected void onMvpStart(final IMvpMessage msg, String path) {
        String loading = msg.to().getParams(STR_LOADING, "");
        if (!TextUtils.isEmpty(loading)) {
            boolean cancel = msg.to().getParams(BOOL_LOADING, false);
            getHintView().showLoading(loading, cancel);
        }
    }

    protected void onMvpFinish(final IMvpMessage msg, String path) {
        String loading = msg.to().getParams(STR_LOADING, "");
        if (!TextUtils.isEmpty(loading)) {
            getHintView().hideLoading();
        }
    }

    protected void onMvpProgress(final IMvpMessage msg, String path) {
    }

    protected void onMvpFailure(final IMvpMessage msg, String path) {
        String errMsg = TextUtils.isEmpty(msg.msg()) ? "请求出错啦" : msg.msg();
        getHintView().showToast(errMsg, 0);
    }

    protected void onMvpSuccess(final IMvpMessage msg, String path) {

    }

    protected void onMvpPermission(final IMvpMessage msg, String path) {
        AlertTemple alert = new AlertTemple("提示", msg.msg());
        alert.setPositiveClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MvpMessage.Builder builder = new MvpMessage.Builder();
                function(builder.reverse(msg).build());
            }
        });
        getHintView().showAlert(alert, false);
    }

    protected EventBus getEventBus() {
        return null;
    }

    protected abstract IMvpDispatcher getDispatcher();

    protected final MvpActivity getFoundActivity() {
        if (!(getActivity() instanceof MvpActivity)) {
            throw new IllegalStateException("FoundFragment must bind to FoundActivity");
        }
        return (MvpActivity) getActivity();
    }
}