package com.oxandon.demo.member.presenter;

import android.text.TextUtils;

import com.oxandon.demo.common.HttpResult;
import com.oxandon.demo.member.model.MemberRepository;
import com.oxandon.found.arch.anno.Mvp;
import com.oxandon.found.arch.anno.RequestMapping;
import com.oxandon.found.arch.impl.HttpPresenter;
import com.oxandon.found.arch.impl.MvpMessage;
import com.oxandon.found.arch.impl.MvpSubscriber;
import com.oxandon.found.arch.protocol.IMvpDispatcher;
import com.oxandon.found.arch.protocol.IMvpMessage;
import com.oxandon.mvp.annotation.Controller;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by peng on 2017/5/20.
 */
@Mvp(value = "member", type = Mvp.PRESENTER)
@Controller(module = "application", value = "MainApplicationDispatcher")
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
        MvpMessage.Builder builder = new MvpMessage.Builder();
        builder.reverse(message);
        Object obj = dispatcher().provideFromView(builder.build());
        Flowable<HttpResult> flowAble = Flowable.just(obj).concatMap(new Function<Object, Publisher<? extends HttpResult>>() {
            @Override
            public Publisher<? extends HttpResult> apply(@NonNull Object obj) throws Exception {
                String[] params = getParcel().opt(obj, String[].class);
                checkArgument(null == params || params.length != 2, "用户名或密码不能为空");
                checkArgument(TextUtils.isEmpty(params[0]), "用户名不能为空");
                checkArgument(TextUtils.isEmpty(params[1]), "密码不能为空");
                return repository.login(getNetwork(), params[0], params[1]);
            }
        });
        doRxSubscribe(flowAble, new MvpSubscriber<HttpResult>(this, message) {

            @Override
            public void onNext(HttpResult result) {
                super.onNext(result);
                MvpMessage.Builder builder = new MvpMessage.Builder();
                if (result.getCode() == HttpResult.SUCCESS_CODE) {
                    builder.what(IMvpMessage.WHAT_SUCCESS);
                } else {
                    builder.what(IMvpMessage.WHAT_FAILURE);
                }
                builder.reverse(message).msg(result.getMessage());
                dispatcher().dispatchToView(builder.build());
            }
        });
    }
}