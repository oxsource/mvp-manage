package com.oxandon.found.mvp.protocol;

import com.oxandon.found.parcel.IParcelFormat;

/**
 * Created by peng on 2017/5/20.
 */

public interface IMvpModel<T extends IMvpPresenter> extends IMvp {
    IParcelFormat getParcel();
}
