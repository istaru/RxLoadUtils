package com.zx.zll.rxjava.request.intercept;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 添加全局header，token等等
 * Created by Moon on 2018/4/18.
 */
public class InterceptorHeader implements Interceptor {

    private Map<String, Object> params = new HashMap<>();

    public InterceptorHeader(Map<String, Object> params) {
        this.params = params;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            builder.addHeader(entry.getKey(), entry.getValue().toString());
        }
        return chain.proceed(builder.build());
    }
}