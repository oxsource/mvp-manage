package com.oxandon.demo.view;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import com.oxandon.found.ui.widget.AlertTemple;
import com.oxandon.found.ui.widget.IHintView;

/**
 * 界面提示接口实现类
 *
 * @author FengPeng
 * @date 2016/11/28
 */
public class HintViewImpl implements IHintView {
    private Activity activity;
    private AlertDialog alertDialog;
    private AlertDialog progressDialog;


    public HintViewImpl(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void showLoading(CharSequence sequence, boolean cancel) {
        hideLoading();
        if (null == activity) {
            return;
        }
        if (progressDialog == null) {
            progressDialog = SkinProgressDialog.build(activity);
        }
        progressDialog.setMessage(sequence);
        progressDialog.setCancelable(cancel);
        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        if (null != progressDialog && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void showAlert(@NonNull final AlertTemple iAlertTemple, boolean cancel) {
        hideAlert();
        if (null == activity) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder = builder.setCancelable(cancel);
        builder = builder.setTitle(iAlertTemple.title());
        builder = builder.setMessage(iAlertTemple.message());
        if (!TextUtils.isEmpty(iAlertTemple.positiveText())) {
            builder = builder.setPositiveButton(iAlertTemple.positiveText(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    hideAlert();
                    if (iAlertTemple.onPositiveClick() != null) {
                        iAlertTemple.onPositiveClick().onClick(null);
                    }
                }
            });
        }
        if (!TextUtils.isEmpty(iAlertTemple.negtiveText())) {
            builder = builder.setNegativeButton(iAlertTemple.negtiveText(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    hideAlert();
                    if (iAlertTemple.onNegtiveClick() != null) {
                        iAlertTemple.onNegtiveClick().onClick(null);
                    }
                }
            });
        }
        alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void hideAlert() {
        if (null != alertDialog && alertDialog.isShowing()) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }

    @Override
    public void showToast(CharSequence sequence, int what) {
        if (null == activity) {
            return;
        }
        Toast toast = Toast.makeText(activity, sequence, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}