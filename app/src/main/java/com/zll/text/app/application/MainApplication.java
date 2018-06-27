package com.zll.text.app.application;

import com.zx.zll.rxjava.application.BaseApplication;
import com.zx.zll.rxjava.request.HttpClient;

/**
 * Created by Moon on 2018/6/22.
 */

public class MainApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        HttpClient.url = "https://lcinvest.lingyongdai.com";
//        HttpClient.url = "http://210.22.126.122:8003";
    }
}
