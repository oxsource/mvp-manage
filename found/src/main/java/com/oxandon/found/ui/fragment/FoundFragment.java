package com.oxandon.found.ui.fragment;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oxandon.found.ui.activity.FoundActivity;
import com.oxandon.found.ui.widget.IHintView;

/**
 * Created by peng on 2017/5/22.
 */

public abstract class FoundFragment extends Fragment implements IFragment {
    private ViewGroup layout;
    private IHintView iHintView;
    private FragmentVisibility visibility;

    @CallSuper
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        visibility = new FragmentVisibility(this);
        visibility.onCreate(savedInstanceState);
        getFoundActivity().addToInterceptor(this);
    }

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = onInflateLayout(inflater, container, savedInstanceState);
        iHintView = onBuildHintView();
        onInitViews(savedInstanceState);
        return layout;
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
        return visibility.visible();
    }

    @CallSuper
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        visibility.setUserVisibleHint(isVisibleToUser);
    }

    @CallSuper
    @Override
    public void onResume() {
        super.onResume();
        visibility.onResume();
    }

    @CallSuper
    @Override
    public void onPause() {
        visibility.onPause();
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        visibility.onSaveInstanceState(outState);
    }

    @Override
    public boolean backPressed() {
        return false;
    }

    @Override
    public final FoundFragment fragment() {
        return this;
    }

    @CallSuper
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        layout = null;
    }

    @CallSuper
    @Override
    public void onDestroy() {
        getFoundActivity().removeFromInterceptor(this);
        visibility.destroy();
        super.onDestroy();
    }

    protected final FoundActivity getFoundActivity() {
        if (!(getActivity() instanceof FoundActivity)) {
            throw new IllegalStateException("FoundFragment must bind to FoundActivity");
        }
        return (FoundActivity) getActivity();
    }
}