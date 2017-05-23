package com.oxandon.found.ui.widget;

import android.view.View;

/**
 * Alert对话框模板
 *
 * @author FengPeng
 * @date 2016/11/27
 */
public final class AlertTemple {
    private CharSequence title;
    private CharSequence message;
    private CharSequence negtiveText;
    private CharSequence positiveText;

    private View.OnClickListener negtiveClick;
    private View.OnClickListener positiveClick;

    public AlertTemple() {
        this("");
    }

    public AlertTemple(CharSequence message) {
        this("提示", message);
    }

    public AlertTemple(CharSequence title, CharSequence message) {
        this.title = title;
        this.message = message;
        negtiveText = "取消";
        positiveText = "确定";
    }

    public CharSequence title() {
        return title;
    }

    public CharSequence message() {
        return message;
    }

    public View.OnClickListener onNegtiveClick() {
        return negtiveClick;
    }

    public void setTitle(CharSequence title) {
        this.title = title;
    }

    public void setMessage(CharSequence message) {
        this.message = message;
    }

    public void setNegtiveText(CharSequence negtiveText) {
        this.negtiveText = negtiveText;
    }

    public void setPositiveText(CharSequence positiveText) {
        this.positiveText = positiveText;
    }

    public void setNegtiveClick(View.OnClickListener negtiveClick) {
        this.negtiveClick = negtiveClick;
    }

    public void setPositiveClick(View.OnClickListener positiveClick) {
        this.positiveClick = positiveClick;
    }

    public View.OnClickListener onPositiveClick() {
        return positiveClick;
    }

    public CharSequence negtiveText() {
        return negtiveText;
    }

    public CharSequence positiveText() {
        return positiveText;
    }
}