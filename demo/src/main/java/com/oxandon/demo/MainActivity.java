package com.oxandon.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.oxandon.demo.view.HintViewImpl;
import com.oxandon.found.arch.impl.MvpMessage;
import com.oxandon.found.arch.impl.MvpSdk;
import com.oxandon.found.arch.impl.MvpUri;
import com.oxandon.found.arch.protocol.IMvpMessage;
import com.oxandon.found.arch.protocol.IMvpView;

/**
 * Created by peng on 2017/5/20.
 */

public class MainActivity extends Activity implements IMvpView {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MvpSdk.dispatcher().attach(this);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MvpMessage.Builder builder = new MvpMessage.Builder();
                MvpUri from = new MvpUri(authority(), "loginResult");
                MvpUri to = new MvpUri("member", "login");
                builder.from(from).to(to);
                function(builder.build());
            }
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
        if ("loginResult".equals(path)) {
            String[] params = new String[2];
            params[0] = "fpCys001";
            params[1] = "fp1992";
            obj = params;
        }
        return obj;
    }

    @Override
    public boolean function(IMvpMessage msg) {
        return MvpSdk.dispatcher().dispatchToPresenter(msg);
    }

    private HintViewImpl hintView = new HintViewImpl(this);

    @Override
    public boolean dispatch(IMvpMessage msg) {
        String path = msg.to().path();
        switch (msg.what()) {
            case IMvpMessage.WHAT_START:
                hintView.showLoading("请稍候...", false);
                break;
            case IMvpMessage.WHAT_PROGRESS:
                break;
            case IMvpMessage.WHAT_FAILURE:
                toast(path + "，msg:" + msg.msg());
                break;
            case IMvpMessage.WHAT_SUCCESS:
                if ("loginResult".equals(path)) {
                    toast("登录成功");
                }
                break;
            case IMvpMessage.WHAT_FINISH:
                hintView.hideLoading();
                break;
        }
        return false;
    }

    @Override
    public String authority() {
        return getClass().getName();
    }

    private void toast(String text) {
        Toast.makeText(getBaseContext(), text, Toast.LENGTH_SHORT).show();
    }
}