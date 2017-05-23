package com.oxandon.demo.common;

import android.support.annotation.NonNull;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by peng on 2017/5/22.
 */

public class HttpResult {
    @JSONField(serialize = false, deserialize = false)
    public static final int SUCCESS_CODE = 1;            //成功
    @JSONField(serialize = false, deserialize = false)
    public static final int FAILURE_CODE = 0;            //错误
    @JSONField(serialize = false, deserialize = false)
    public static final int REFRESH_CODE = 2;            //数据需要刷新
    @JSONField(serialize = false, deserialize = false)
    public static final int ALREADY_EXISTS = 15;        //已存在
    @JSONField(serialize = false, deserialize = false)
    public static final int NOT_LOGIN = 999;            //未登录
    @JSONField(serialize = false, deserialize = false)
    public static final int NOT_INFO_ADD = 120;            //未维护
    @JSONField(serialize = false, deserialize = false)
    public static final int NOT_INFO_AUDIT = 130;        //未审核
    @JSONField(serialize = false, deserialize = false)
    public static final int DIALOG_CODE = 140;            //对话框
    @JSONField(serialize = false, deserialize = false)
    public int what = -1;//自定义附加消息

    private String message;

    private int code;

    private int itemCount;

    private String data;

    //额外对象，主要用于线程间携带对象
    @JSONField(serialize = false, deserialize = false)
    private Object obj;

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public String getMessage() {
        return null == message ? "操作失败,返回的消息为空" : message;
    }

    public String getMessage(@NonNull String defaultText) {
        return null == message ? defaultText : message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getWhat() {
        return what;
    }

    public void setWhat(int what) {
        this.what = what;
    }
}
