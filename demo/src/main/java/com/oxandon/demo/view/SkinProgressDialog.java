package com.oxandon.demo.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.TextView;

import com.oxandon.demo.R;

/**
 * Created by peng on 2017/4/26.
 */

public class SkinProgressDialog extends AlertDialog {
    private TextView tvContent;
    private CharSequence message;
    private final String DEFAULT_MSG = "请稍候...";

    public static SkinProgressDialog build(@NonNull Context context) {
        return new SkinProgressDialog(context);
    }

    protected SkinProgressDialog(@NonNull Context context) {
        super(context, R.style.SkinProgressDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.skin_progress_dialog);
        tvContent = (TextView) findViewById(R.id.tvContent);
        message = TextUtils.isEmpty(message) ? DEFAULT_MSG : message;
        tvContent.setText(message);
    }

    @Override
    public void setMessage(CharSequence message) {
        if (!TextUtils.isEmpty(message) && message.length() > 16) {
            message = DEFAULT_MSG;
        }
        this.message = message;
    }

    @Override
    public void show() {
        Context context = getContext();
        if (context == null) {
            return;
        }
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (activity.isFinishing()) {
                return;
            }
        }
        super.show();
    }
}