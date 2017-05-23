package com.oxandon.found.env;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.oxandon.found.arch.impl.MvpMessage;
import com.oxandon.found.arch.protocol.IMvpMessage;
import com.oxandon.found.arch.protocol.IMvpUri;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by peng on 2017/5/23.
 */

public class MvpEvent {
    /**
     * 单播
     *
     * @param uri
     * @param what
     * @param obj
     */
    public static void singleCast(IMvpUri uri, int what, Object obj) {
        MvpMessage.Builder builder = new MvpMessage.Builder();
        builder.to(uri).what(what).obj(obj);
        EventBus.getDefault().post(builder.build());
    }

    /**
     * 广播
     *
     * @param uri
     * @param what
     * @param obj
     */
    public static void multiCast(IMvpUri uri, int what, Object obj) {
        MvpMessage.Builder builder = new MvpMessage.Builder();
        builder.from(uri).what(what).obj(obj);
        EventBus.getDefault().post(builder.build());
    }

    public static boolean singleCast(IMvpMessage message, @NonNull IMvpUri uri) {
        return uri.samePath(message.to());
    }

    public static boolean multiCast(IMvpMessage message, @NonNull IMvpUri uri) {
        return uri.samePath(message.from());
    }

    public static String catchException(Throwable e, String split, String msg) {
        if (null != e && !TextUtils.isEmpty(e.getMessage())) {
            msg = split + e.getMessage();
        }
        return msg;
    }
}