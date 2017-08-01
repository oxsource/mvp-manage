package com.oxandon.mvp.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 控制器注解
 * Created by peng on 2017/7/31.
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Controller {
    /**
     * which module
     *
     * @return
     */
    String module() default "";

    /**
     * name
     *
     * @return
     */
    String value();
}