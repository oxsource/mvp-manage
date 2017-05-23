package com.oxandon.found.arch.impl;

import android.support.annotation.CallSuper;

import com.oxandon.found.http.INetworkEngine;
import com.oxandon.found.http.NetworkEngineImpl;
import com.oxandon.found.http.NetworkUtil;
import com.oxandon.found.arch.protocol.IMvpDispatcher;
import com.oxandon.found.arch.protocol.IMvpMessage;
import com.oxandon.found.parcel.IParcelFormat;
import com.oxandon.found.parcel.ParcelFormatImpl;

/**
 * 基于HTTP请求的PRESENTER
 * Created by peng on 2017/5/22.
 */

public abstract class HttpPresenter extends MvpPresenter {
    private final INetworkEngine iNetwork;
    private final IParcelFormat iParcel;

    public HttpPresenter(IMvpDispatcher dispatcher) {
        super(dispatcher);
        iNetwork = onBuildNetwork();
        iParcel = onBuildParcel();
        iNetwork.host(getCurrentHost());
        iNetwork.notifyHostChanged();
    }

    @CallSuper
    @Override
    public boolean onIntercept(IMvpMessage msg) throws Exception {
        if (needCheckNetwork() && NetworkUtil.getNetworkType() == NetworkUtil.NETWORK_NONE) {
            MvpMessage.Builder builder = new MvpMessage.Builder();
            builder.reverse(msg).what(IMvpMessage.WHAT_FAILURE).msg("无法连接网络");
            dispatcher().dispatchToView(builder.build());
            return true;
        }
        return super.onIntercept(msg);
    }

    protected boolean needCheckNetwork() {
        return true;
    }

    public INetworkEngine getNetwork() {
        return iNetwork;
    }

    public IParcelFormat getParcel() {
        return iParcel;
    }

    protected INetworkEngine onBuildNetwork() {
        return new NetworkEngineImpl();
    }

    protected IParcelFormat onBuildParcel() {
        return new ParcelFormatImpl();
    }

    protected abstract String getCurrentHost();
}