package com.oxandon.found.mvp.anno;

import android.support.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by peng on 2017/5/20.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Mvp {
    int PRESENTER = 0;
    int MODEL = 1;
    int VIEW = 2;

    @IntDef(value = {PRESENTER, MODEL, VIEW})
    @Retention(RetentionPolicy.RUNTIME)
    @interface TYPE {
    }

    String value();

    @TYPE int type();
}
