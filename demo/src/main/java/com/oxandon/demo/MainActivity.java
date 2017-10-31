package com.oxandon.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.oxandon.demo.view.HintViewImpl;
import com.oxandon.mvp.arch.impl.MvpMessage;
import com.oxandon.mvp.arch.impl.MvpSdk;
import com.oxandon.mvp.arch.impl.MvpUri;
import com.oxandon.mvp.arch.protocol.IMvpMessage;
import com.oxandon.mvp.arch.protocol.IMvpView;
import com.oxandon.mvp.env.MvpEvent;
import com.oxandon.mvp.ui.activity.MvpActivity;
import com.oxandon.mvp.ui.widget.IHintView;

/**
 * Created by peng on 2017/5/20.
 */

public class MainActivity extends MvpActivity implements IMvpView {

    private Button btLogin;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MvpSdk.dispatcher().attach(this);
    }

    @Override
    public ViewGroup onInflateLayout(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return (ViewGroup) inflater.inflate(R.layout.activity_main, null);
    }

    @Override
    protected void onViewCreated(@Nullable Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);
        btLogin = findViewById(R.id.btLogin);
        btLogin.setOnClickListener(v -> {
            MvpMessage.Builder builder = new MvpMessage.Builder();
            MvpUri from = new MvpUri(authority(), "login");
            MvpUri to = new MvpUri("member", "login");
            builder.from(from).to(to);
            function(builder.build());
        });
    }

    @Override
    protected void onDestroy() {
        MvpSdk.dispatcher().detach(this);
        super.onDestroy();
    }

    @Override
    public Object provide(IMvpMessage msg) {
        Object obj = null;
        String path = msg.to().path();
        if ("login".equals(path)) {
            String[] params = new String[2];
            params[0] = "fpCys001";
            params[1] = "fp1992";
            obj = params;
        }
        return obj;
    }

    @Override
    public boolean function(IMvpMessage msg) {
        MvpMessage.Builder builder = null;
        try {
            if (null == MvpSdk.dispatcher()) throw new IllegalStateException("");
            MvpSdk.dispatcher().dispatchToPresenter(msg);
        } catch (Exception e) {
            e.printStackTrace();
            builder = new MvpMessage.Builder().clone(msg).msg(e.getMessage());
            MvpEvent.exceptCast(builder.build(), e);
        }
        return null == builder;
    }

    private HintViewImpl hintView = new HintViewImpl(this);

    @Override
    public boolean dispatch(IMvpMessage msg) {
        String path = msg.to().path();
        switch (msg.what()) {
            case IMvpMessage.WHAT_START:
                btLogin.setVisibility(View.GONE);
                hintView.showLoading("请稍候...", dialog -> {
                    btLogin.setVisibility(View.VISIBLE);
                    MvpMessage.Builder builder = new MvpMessage.Builder();
                    MvpUri from = new MvpUri(authority(), "login");
                    MvpUri to = new MvpUri("member", "login");
                    builder.from(from).to(to).what(IMvpMessage.WHAT_FINISH);
                    function(builder.build());
                });
                break;
            case IMvpMessage.WHAT_PROGRESS:
                break;
            case IMvpMessage.WHAT_FAILURE:
                getHintView().showToast(path + "，msg:" + msg.msg(), 0);
                break;
            case IMvpMessage.WHAT_SUCCESS:
                if ("login".equals(path)) {
                    getHintView().showToast("登录成功", 0);
                }
                break;
            case IMvpMessage.WHAT_FINISH:
                btLogin.setVisibility(View.VISIBLE);
                hintView.hideLoading();
                break;
        }
        return false;
    }

    @Override
    public String authority() {
        return getClass().getName();
    }

    @Override
    public IHintView onBuildHintView() {
        return hintView;
    }
}