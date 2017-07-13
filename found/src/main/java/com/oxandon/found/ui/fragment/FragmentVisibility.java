package com.oxandon.found.ui.fragment;

import android.os.Bundle;
import android.support.annotation.IntDef;

import com.oxandon.found.env.FoundEnvironment;
import com.oxandon.found.log.FoundLog;

/**
 * 一、Fragment默认在onResume时可见
 * 二、使用ViewPager后情况特殊：
 * 1、ViewPagerAdapter中第一个Fragment是先setUserVisibleHint(true)后才创建View
 * 2、ViewPagerAdapter中其他Fragment是先创建View后setUserVisibleHint(true)
 * 3、使用ViewPagerAdapter异常时先执行Resume再执行setUserVisibleHint(true)
 * 三、使用hide和show方式时回调onHiddenChanged
 * Created by peng on 2017/5/22.
 */

public class FragmentVisibility {
    private final static String FLAG_USE_HINT = "flag_use_hint";
    public final static int VISIBLE_NORMAL = 0x21;
    public final static int VISIBLE_HINT = 0x22;
    public final static int VISIBLE_HINT_NORMAL = 0x23;
    public final static int VISIBLE_HIDE_CHANGE = 0x24;
    private boolean visibleToUser = false;

    @IntDef(value = {VISIBLE_NORMAL, VISIBLE_HINT, VISIBLE_HINT_NORMAL, VISIBLE_HIDE_CHANGE})
    @interface VisibleType {
    }

    private boolean withViewPager = false;
    private boolean hintNormalShow = false;
    private boolean hintNormalHide = false;
    private IFragment fragment;

    public FragmentVisibility(IFragment fragment) {
        this.fragment = fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        if (null != savedInstanceState) {
            withViewPager = savedInstanceState.getBoolean(FLAG_USE_HINT, withViewPager);
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(FLAG_USE_HINT, withViewPager);
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        withViewPager = true;
        printVisibleLayout("setUserVisibleHint", isVisibleToUser);
        if (isVisibleToUser && !visible()) {
            if (null == fragment.getLayout()) {
                hintNormalShow = true;
            } else {
                printVisibleToUser(VISIBLE_HINT, true);
                onVisible();
            }
            hintNormalHide = true;
        } else if (!isVisibleToUser && visible()) {
            if (null != fragment.getLayout()) {
                printVisibleToUser(VISIBLE_HINT, false);
                hintNormalHide = false;
                onInvisible();
            }
        }
    }

    public void onResume() {
        printVisibleLayout("onResume", true);
        if (!withViewPager && !visible()) {
            if (!hideChange[0]) {
                printVisibleToUser(VISIBLE_NORMAL, true);
                onVisible();
            }
            hideChange[0] = false;
        } else {
            if (hintNormalShow && !visible()) {
                printVisibleToUser(VISIBLE_HINT_NORMAL, true);
                hintNormalShow = false;
                hintNormalHide = true;
                onVisible();
            }
        }
    }

    public void onPause() {
        printVisibleLayout("onPause", false);
        if (!withViewPager && visible()) {
            if (!hideChange[1]) {
                printVisibleToUser(VISIBLE_NORMAL, false);
                onInvisible();
            }
            hideChange[1] = false;
        } else {
            if (hintNormalHide && visible()) {
                printVisibleToUser(VISIBLE_HINT_NORMAL, false);
                hintNormalHide = false;
                hintNormalShow = true;
                onInvisible();
            }
        }
    }

    private boolean[] hideChange = new boolean[2];

    public void onHiddenChanged(boolean hidden) {
        printVisibleLayout("onHiddenChanged", false);
        if (hidden) {
            hideChange[0] = true;
            printVisibleToUser(VISIBLE_HIDE_CHANGE, false);
            onInvisible();
        } else {
            hideChange[1] = true;
            printVisibleToUser(VISIBLE_HIDE_CHANGE, true);
            onVisible();
        }
    }

    /*对用户可见时的回调*/
    public void onVisible() {
        visibleToUser = true;
        fragment.onVisible();
    }

    /*对用户不可见时的回调*/
    public void onInvisible() {
        visibleToUser = false;
        fragment.onInvisible();
    }

    public boolean visible() {
        return visibleToUser;
    }

    public void destroy() {
        fragment = null;
    }

    private String visibleType(@VisibleType int type) {
        String content = "";
        switch (type) {
            case VISIBLE_NORMAL:
                content += "NORMAL";
                break;
            case VISIBLE_HINT:
                content += "HINT";
                break;
            case VISIBLE_HINT_NORMAL:
                content += "HINT_NORMAL";
                break;
            case VISIBLE_HIDE_CHANGE:
                content += "HIDE_CHANGE";
                break;
        }
        return content;
    }

    //测试打印onVisibleToUser和onInvisibleToUser生命周期
    private void printVisibleToUser(@VisibleType int type, boolean visible) {
        if (FoundEnvironment.isDebug() && null != fragment) {
            String content = fragment.getClass().getSimpleName() + "-->Visible=" + visible + "，Type=";
            String location = visibleType(type);
            FoundLog.d(content + location);
        }
    }

    private void printVisibleLayout(String method, boolean visible) {
        if (FoundEnvironment.isDebug() && null != fragment) {
            boolean flag = null == fragment.getLayout();
            FoundLog.d(fragment.getClass().getSimpleName() + "-->getLayout()==null: " + flag + ",Method=" + method + "," + "Visible=" + visible);
        }
    }
}