package com.zll.text.app.activity;

import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zll.text.app.R;
import com.zll.text.app.adapter.BaseAdapter;
import com.zll.text.app.adapter.MainAdapter;
import com.zll.text.app.ports.ApiService;
import com.zx.zll.rxjava.request.HttpClient;
import com.zx.zll.rxjava.request.Subscriber;
import com.zx.zll.rxjava.request.Transformer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener, BaseAdapter.OnClickListener {

    private MainAdapter adapter;

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        refreshLayout.setEnableOverScrollDrag(true);
        loadingLayout = findViewById(R.id.loading);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);//如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new MainAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setOnClickListener(this);

        findByData();
    }

    private void findByData() {
        Map<String, Object> map = new HashMap<>();
        map.put("pageIndex", pageIndex);
        map.put("pageSize", pageNum);
        map.put("appKey", "AndroidAppKey");
        map.put("timestamp", System.currentTimeMillis() + "");
        map.put("deviceId", Settings.System.getString(getContentResolver(), Settings.System.ANDROID_ID) + "");
        map.put("token", "N05FlLyFViE8N89N4yxLeT6TB6HP1f00032795");
        map.put("sign", map);
        HttpClient.getInstance()
                .createService(ApiService.class)
                .getBanner(map)
                .compose(Transformer.<JSONObject>call())
                .subscribe(new Subscriber<JSONObject>(loadingLayout, refreshLayout,pageIndex) {
                    @Override
                    protected void onSuccess(JSONObject jsonObject) {
                        JSONArray jsonArray = jsonObject.getJSONArray("contentActivityModel");
                        refreshLayout.setNoMoreData(false);
                        adapter.addRecyclerData(JSON.parseObject(jsonArray.toString(), new TypeReference<List<Map<String, Object>>>() {
                        }), pageIndex);
                    }

                    @Override
                    protected void onError(String message) {

                    }
                });
    }

    @Override
    public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
        pageIndex = 1;
        findByData();
    }

    @Override
    public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
        pageIndex++;
        findByData();
    }

    @Override
    public void onItemClick(View view, int position, List t) {

    }
}