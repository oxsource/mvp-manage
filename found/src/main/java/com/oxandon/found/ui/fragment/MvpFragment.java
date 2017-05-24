package com.oxandon.found.ui.fragment;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oxandon.found.arch.protocol.IMvpDispatcher;
import com.oxandon.found.arch.protocol.IMvpView;
import com.oxandon.found.ui.activity.MvpActivity;
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