package com.oxandon.found.mvp.impl;

import com.oxandon.found.mvp.protocol.IMvpModel;
import com.oxandon.found.mvp.protocol.IMvpPresenter;
import com.oxandon.found.parcel.IParcelFormat;
import com.oxandon.found.parcel.ParcelFormatImpl;

/**
 * Created by peng on 2017/5/22.
 */

public class MvpModel<T extends IMvpPresenter> implements IMvpModel<T> {
    private IParcelFormat parcelFormat;

    public MvpModel() {
        parcelFormat = new ParcelFormatImpl();
    }

    @Override
    public String authority() {
        return getClass().getName();
    }

    @Override
    public IParcelFormat getParcel() {
        return parcelFormat;
    }
}