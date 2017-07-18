package com.oxandon.found.arch.anno;

import android.support.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by peng on 2017/5/20.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
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
