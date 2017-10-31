package com.oxandon.demo.member.presenter;

import android.text.TextUtils;

import com.oxandon.demo.common.HttpResult;
import com.oxandon.demo.member.model.MemberRepository;
import com.oxandon.mvp.annotation.Controller;
import com.oxandon.mvp.annotation.RequestMapping;
import com.oxandon.mvp.arch.impl.HttpPresenter;
import com.oxandon.mvp.arch.impl.MvpMessage;
import com.oxandon.mvp.arch.impl.MvpSubscriber;
import com.oxandon.mvp.arch.protocol.IMvpDispatcher;
import com.oxandon.mvp.arch.protocol.IMvpMessage;

import io.reactivex.Flowable;

/**
 * Created by peng on 2017/5/20.
 */
@Controller(module = "member", value = "member")
public class MemberPresenter extends HttpPresenter {
    private MemberRepository repository;

    public MemberPresenter(IMvpDispatcher dispatcher) {
        super(dispatcher);
        repository = new MemberRepository();
    }

    @Override
    protected String getCurrentHost() {
        return "http://dida.yesdididi.com/dida/";
    }

    @RequestMapping("login")
    public void doLogin(final IMvpMessage message) {
        Object obj = dispatcher().provideFromView(message);
        Flowable<HttpResult> flowAble = Flowable.just(obj).concatMap(obj1 -> {
            String[] params = getParcel().opt(obj1, String[].class);
            checkArgument(null == params || params.length != 2, "用户名或密码不能为空");
            checkArgument(TextUtils.isEmpty(params[0]), "用户名不能为空");
            checkArgument(TextUtils.isEmpty(params[1]), "密码不能为空");
            Thread.sleep(5000);
            return repository.login(getNetwork(), params[0], params[1]);
        });
        doRxSubscribe(message, flowAble, new MvpSubscriber<HttpResult>(this, message) {
            @Override
            public void onNext(HttpResult result) {
                super.onNext(result);
                MvpMessage.Builder builder = new MvpMessage.Builder();
                if (result.getCode() == HttpResult.SUCCESS_CODE) {
                    builder.what(IMvpMessage.WHAT_SUCCESS);
                } else {
                    builder.what(IMvpMessage.WHAT_FAILURE);
                }
                builder.clone(message).msg(result.getMessage());
                dispatcher().dispatchToView(builder.build());
            }
        });
    }
}