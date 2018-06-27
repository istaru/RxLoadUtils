package com.zx.zll.rxjava.request;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zx.zll.rxjava.request.exception.ApiException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * RxJava，当请求成功时会比Retrofit先拿到服务器的返回值
 * Created by Moon on 2018/6/22.
 * 一RxJava2.0 有两种观察者模式：
 * (1) Observer ( 观察者 ) 、 Observable ( 被观察者 )
 * (2) Subscriber （观察者） 、 Flowable （被观察者）
 * <p>
 * 二选择线程：
 * Schedulers.io() 代表io操作的线程, 通常用于网络,读写文件等io密集型的操作；
 * Schedulers.computation() 代表CPU计算密集型的操作, 例如需要大量计算的操作；
 * Schedulers.newThread() 代表一个常规的新线程；
 * AndroidSchedulers.mainThread() 代表Android的主线程
 */
public class Transformer {

    /**
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> call() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .map(new Function<T, T>() {
                            @Override
                            public T apply(T t) throws Exception {
                                int code;
                                String message;
                                JSONObject object = JSONObject.parseObject(t.toString());
                                code = object.getInteger("code");
                                message = object.getString("message");
                                JSONArray jsonArray = object.getJSONArray("contentActivityModel");
                                if((null != jsonArray || !jsonArray.isEmpty()) && jsonArray.size() == 0){
                                    if(jsonArray.size() == 0){
                                        code = 1;
                                        message = "呜呜呜，什么都还没有";
                                        throw new ApiException(code, message);
                                    }
                                }
                                if (code != 200) {
                                    throw new ApiException(code, message);
                                }
                                return t;
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
