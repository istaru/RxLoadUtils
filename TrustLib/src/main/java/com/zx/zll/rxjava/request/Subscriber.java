package com.zx.zll.rxjava.request;

import android.view.View;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.zx.zll.rxjava.R;
import com.zx.zll.rxjava.request.exception.ApiException;
import com.zx.zll.rxjava.views.LoadingLayout;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Retrofit2
 * Created by Moon on 2018/6/22.
 */

public abstract class Subscriber<T> implements Observer<T> {

    private RefreshLayout refreshLayout;
    private LoadingLayout loadingLayout;
    private int pageIndex;

    public Subscriber() {
    }

    /**
     * 数据非列表时展示请求动画
     *
     * @param loadingLayout
     */
    public Subscriber(LoadingLayout loadingLayout) {
        this.loadingLayout = loadingLayout;
    }

    /**
     * 数据是列表时展示刷新和请求动画
     *
     * @param loadingLayout
     * @param refreshLayout
     * @param pageIndex     1为下拉刷新，判断是否显示空布局
     */
    public Subscriber(LoadingLayout loadingLayout, RefreshLayout refreshLayout, int pageIndex) {
        this.loadingLayout = loadingLayout;
        this.refreshLayout = refreshLayout;
        this.pageIndex = pageIndex;
    }

    /**
     * 开始请求
     *
     * @param d 可以做到切断的操作，让Observer观察者不再接收上游事件
     */
    @Override
    public void onSubscribe(Disposable d) {
    }

    /**
     * 请求处理循环
     *
     * @param t
     */
    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    /**
     * 请求出错
     *
     * @param e
     */
    @Override
    public void onError(final Throwable e) {
        if (null != refreshLayout) {
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadMore();
        }
        String msg = e.getMessage();
        int code;
        if (e instanceof ApiException) {//自定义异常
            ApiException exception = ((ApiException) e);
            code = exception.getCode();
            msg = exception.getMsg();
            if (code == 1) {
                if (pageIndex == 1) {
                    loadingLayout.showEmpty();
                }
                refreshLayout.finishLoadMoreWithNoMoreData();
            }
        } else {
            if (e.getMessage().contains("Unsatisfiable Request (only-if-cached)")) {//网络异常
                msg = "网络异常，请检查网络";
                if (null != loadingLayout) {
                    loadingLayout.showError();
                }
            }
        }
        final String finalMsg = msg;
        loadingLayout.setOnErrorInflateListener(new LoadingLayout.OnInflateListener() {
            @Override
            public void onInflate(View inflated) {
                TextView text = inflated.findViewById(R.id.text_error);
                text.setText(finalMsg);
            }
        });
        onError(msg);
    }

    /**
     * 请求处理循环结束
     */
    @Override
    public void onComplete() {
        if (null != refreshLayout) {
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadMore();
        }
        if (null != loadingLayout) {
            loadingLayout.showContent();
        }
    }

    protected abstract void onSuccess(T t);

    protected abstract void onError(String message);
}
