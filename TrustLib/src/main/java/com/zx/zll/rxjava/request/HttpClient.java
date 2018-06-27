package com.zx.zll.rxjava.request;

import com.google.gson.GsonBuilder;
import com.zx.zll.rxjava.application.BaseApplication;
import com.zx.zll.rxjava.request.intercept.InterceptorCache;
import com.zx.zll.rxjava.tools.BaseTools;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络请求类
 * Created by Moon on 2018/6/22.
 */

public class HttpClient {
    private static HttpClient instance;
    private OkHttpClient.Builder okBuilder;
    private Retrofit.Builder rfBuilder;
    public static String url;

    public static HttpClient getInstance() {
        if (null == instance) {
            synchronized (HttpClient.class) {
                if (null == instance) {
                    instance = new HttpClient();
                }
            }
        }
        return instance;
    }

    public HttpClient() {
        InterceptorCache cacheInterceptor = new InterceptorCache();
        Cache cache = new Cache(BaseTools.makeFile("netCache"), 1024 * 1024 * 100);//将网络数据缓存到本地
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        if (BaseTools.isApkInDebug(BaseApplication.getContext())) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        }
        okBuilder = new OkHttpClient.Builder();
        okBuilder.retryOnConnectionFailure(true)//连接失败后是否重新连接
                .connectTimeout(15, TimeUnit.SECONDS)//超时时间秒为单位
                .addInterceptor(cacheInterceptor)//设置本地缓存
                .addInterceptor(interceptor)
                .addNetworkInterceptor(cacheInterceptor)//设置网络缓存
                .cache(cache)//设置网络缓存
                .build();
    }

    /**
     * 设置网络请求域名
     *
     * @param url
     * @return
     */
    public HttpClient setBaseUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * 创建Retrofit
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T createService(Class<T> clazz) {
        rfBuilder = new Retrofit.Builder();
        return rfBuilder.baseUrl(url)
                .client(okBuilder.build())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))//请求的结果转为实体类
                .addConverterFactory(FastJsonConverterFactory.create())//请求的结果直返回JSON
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//适配RxJava2.0, RxJava1.x则为RxJavaCallAdapterFactory.create()
                .build()
                .create(clazz);
    }

    /**
     * 设置拦截器
     *
     * @return
     */
    public HttpClient setInterceptor(Interceptor interceptor) {
        okBuilder.addInterceptor(interceptor);
        return this;
    }

    /**
     * 获取Retrofit对象
     *
     * @return
     */
    public Retrofit.Builder getRfBuilder() {
        return rfBuilder;
    }
}
