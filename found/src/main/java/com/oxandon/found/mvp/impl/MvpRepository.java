package com.oxandon.found.mvp.impl;

import com.oxandon.found.mvp.protocol.IMvpRepository;
import com.oxandon.found.parcel.IParcelFormat;
import com.oxandon.found.parcel.ParcelFormatImpl;

/**
 * Created by peng on 2017/5/23.
 */

public class MvpRepository implements IMvpRepository {

    private IParcelFormat parcelFormat;

    public MvpRepository() {
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