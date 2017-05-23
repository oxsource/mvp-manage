package com.oxandon.demo.member.model;

import android.support.annotation.NonNull;

import com.oxandon.demo.common.HttpResult;
import com.oxandon.found.http.INetworkEngine;
import com.oxandon.found.arch.impl.MvpRepository;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;

/**
 * Created by peng on 2017/5/22.
 */

public class MemberRepository extends MvpRepository {

    public Flowable<HttpResult> login(@NonNull INetworkEngine engine, String account, String password) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("globalPlatformId", "JS*DDWL*0001");
        map.put("globalUserType", "GS");
        map.put("globalUserPart", "ZC");
        map.put("globalFromType", "app");
        map.put("globalRoleType", "CYR");
        map.put("globalLoginType", "WEB");
        map.put("roleType", "CYF");
        //图片上传类型 imageTransfer 1代表OSS方式(1.1.5版本迁移至阿里云OSS)
        map.put("imageTransfer", 1);

        Map<String, String> device = new HashMap<>();
        device.put("verName", "1.1.6");
        device.put("verCode", "9");
        device.put("phoneModel", "MX5.0");
        device.put("sdkVersion", "21");
        device.put("androidRelease", "release");
        String deviceInfo = getParcel().string(device).supply();

        map.put("deviceInfo", deviceInfo);
        map.put("loginName", account);
        map.put("password", password);
        String post = getParcel().string(map).supply();
        return engine.http(IMemberApi.class).login(post);
    }
}