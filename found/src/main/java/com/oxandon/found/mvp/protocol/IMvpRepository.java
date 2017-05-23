package com.oxandon.found.mvp.protocol;

import com.oxandon.found.parcel.IParcelFormat;

/**
 * Created by peng on 2017/5/23.
 */

public interface IMvpRepository extends IMvp {
    IParcelFormat getParcel();
}
