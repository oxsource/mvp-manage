package com.oxandon.demo.member.model;

import com.oxandon.demo.common.HttpResult;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by peng on 2017/5/22.
 */

public interface IMemberApi {

    @FormUrlEncoded
    @POST("owner/ownerlogin")
    Flowable<HttpResult> login(@Field("postdata") String post);
}