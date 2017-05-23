package com.oxandon.found.ui.activity;

import com.oxandon.found.ui.fragment.FoundFragment;

/**
 * Created by peng on 2017/5/22.
 */

public interface IActivityInterceptor {
    /**
     * 返回按键按下时时间处理
     *
     * @return 默认false，拦截时返回true
     */
    boolean backPressed();

    /**
     * 具体的Fragment
     *
     * @return
     */
    FoundFragment fragment();
}
