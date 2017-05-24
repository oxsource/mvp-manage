package com.oxandon.found.arch.impl;

import com.oxandon.found.arch.protocol.IMvpDispatcher;

/**
 * Created by peng on 2017/5/24.
 */

public class MvpSdk {
    private static IMvpDispatcher dispatcher;

    public static void bind(IMvpDispatcher dispatcher) {
        MvpSdk.dispatcher = dispatcher;
    }

    public static IMvpDispatcher getDispatcher() {
        return dispatcher;
    }
}