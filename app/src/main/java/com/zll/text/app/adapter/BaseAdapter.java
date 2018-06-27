package com.zll.text.app.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by superMoon on 2017/8/17.
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public OnClickListener onClickListener;
    public OnLongClickListener onLongClickListener;

    @Override
    public int getItemCount() {
        return getItem();
    }

    /**
     * 根据这个方法填充不同的布局
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return getItemType(position);
    }

    /**
     * 填充页面
     *
     * @param parent
     * @param layoutRes
     * @return
     */
    public View inflate(ViewGroup parent, int layoutRes) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
    }

    /**
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return onCreateView(parent,viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        onBindView(holder,position);
    }

    /**
     * 通过异步请求将列表的数据填充到Adapter
     * @param list 数据
     * @param pageIndex 1刷新，2加载
     */
    public abstract void addRecyclerData(List list, int pageIndex);

    /**
     * 返回item的数量
     * @return
     */
    protected abstract int getItem();

    /**
     * 返回item的类型
     * @return
     */
    protected abstract int getItemType(int position);

    /**
     * 填充页面
     *
     * @param parent
     * @param viewType
     * @return
     */
    protected abstract RecyclerView.ViewHolder onCreateView(ViewGroup parent, int viewType);

    /**
     * 绑定数据
     *
     * @param holder
     * @param position
     */
    protected abstract void onBindView(RecyclerView.ViewHolder holder, int position);
    /**
     * 定义单击事件接口
     */
    public interface OnClickListener {
        void onItemClick(View view, int position, List t);
    }

    /**
     * 所有单击事件的处理方法
     */
    public void setOnClickListener(final OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    /**
     * 定义长按单击事件接口
     */
    public interface OnLongClickListener {
        void onItemLongClick(View view, int position, List t);
    }

    /**
     * 所有长按事件的处理方法
     */
    public void setOnLongClickListener(final OnLongClickListener OnLongClickListener) {
        this.onLongClickListener = OnLongClickListener;
    }
}
