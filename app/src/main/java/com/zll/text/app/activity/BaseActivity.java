package com.zll.text.app.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.zx.zll.rxjava.views.LoadingLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Moon on 2018/6/25.
 */

public abstract class BaseActivity extends AppCompatActivity {
    public AppCompatActivity context;
    public RefreshLayout refreshLayout;
    public RecyclerView recyclerView;
    public LoadingLayout loadingLayout;
    public int pageIndex = 1;
    public final static int pageNum = 15;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        EventBus.getDefault().register(context);
        setContentView(getLayout());
        initView();
    }

    /**
     * setContentView方法
     */
    protected abstract int getLayout();

    /**
     * 初始化控件
     */
    public abstract void initView();

    /**
     * 全局接收网络变化
     *
     * @param netType
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void netChange(String netType) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(context);
    }
}
