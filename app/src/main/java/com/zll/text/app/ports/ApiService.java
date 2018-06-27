package com.zll.text.app.ports;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Moon on 2018/6/25.
 */

public interface ApiService {

    @FormUrlEncoded
    @POST("/home/getContentActivityLyd")
    Observable<JSONObject> getBanner(@FieldMap Map<String, Object> params);
}
