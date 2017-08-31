package com.oxandon.demo;

import android.support.annotation.NonNull;
import com.oxandon.mvp.arch.impl.MvpDispatcher;
import com.oxandon.mvp.arch.protocol.IMvpPresenter;
import java.util.ArrayList;
import java.util.List;
import com.oxandon.demo.member.presenter.MemberPresenter;

public class MainDispatcher extends MvpDispatcher{

	@NonNull
	@Override
	protected List<Class<? extends IMvpPresenter>> support() {
		List<Class<? extends IMvpPresenter>> list = new ArrayList<>();
		/****module member***/
		list.add(MemberPresenter.class);
		return list;
	}
}