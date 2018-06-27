
package com.zx.zll.rxjava.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zx.zll.rxjava.R;

import java.util.HashMap;
import java.util.Map;

/**
 * 布局显示页，loading、empty、error、content
 * Created by superMoon on 2018/6/22.
 */

public class LoadingLayout extends FrameLayout {
    private LayoutInflater mInflater;
    private AnimationDrawable animationDrawable;

    private int mEmptyResId = NO_ID, mLoadingResId = NO_ID, mErrorResId = NO_ID;
    private int mContentId = NO_ID;

    private OnInflateListener mOnEmptyInflateListener;
    private OnInflateListener mOnErrorInflateListener;

    private Map<Integer, View> mLayouts = new HashMap<>();

    public LoadingLayout(Context context) {
        this(context, null, R.attr.styleLoadingLayout);
    }

    public LoadingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.styleLoadingLayout);
    }

    public LoadingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInflater = LayoutInflater.from(context);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoadingLayout, defStyleAttr, R.style.LoadingLayout_Style);
        mEmptyResId = a.getResourceId(R.styleable.LoadingLayout_llEmptyResId, R.layout.view_empty);
        mLoadingResId = a.getResourceId(R.styleable.LoadingLayout_llLoadingResId, R.layout.view_loading);
        mErrorResId = a.getResourceId(R.styleable.LoadingLayout_llErrorResId, R.layout.view_error);
        a.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() == 0) {
            return;
        }
        if (getChildCount() > 1) {
            removeViews(1, getChildCount() - 1);
        }
        View view = getChildAt(0);
        setContentView(view);
        showLoading();
    }

    private void setContentView(View view) {
        mContentId = view.getId();
        mLayouts.put(mContentId, view);
    }

    public LoadingLayout setOnEmptyInflateListener(OnInflateListener listener) {
        mOnEmptyInflateListener = listener;
        if (mOnEmptyInflateListener != null && mLayouts.containsKey(mEmptyResId)) {
            listener.onInflate(mLayouts.get(mEmptyResId));
        }
        return this;
    }

    public LoadingLayout setOnErrorInflateListener(OnInflateListener listener) {
        mOnErrorInflateListener = listener;
        if (mOnErrorInflateListener != null && mLayouts.containsKey(mErrorResId)) {
            listener.onInflate(mLayouts.get(mErrorResId));
        }
        return this;
    }

    public void showLoading() {
        show(mLoadingResId);
    }

    public void showEmpty() {
        show(mEmptyResId);
    }

    public void showError() {
        show(mErrorResId);
    }

    public void showContent() {
        show(mContentId);
    }

    private void show(int layoutId) {
        for (View view : mLayouts.values()) {
            view.setVisibility(GONE);
            if (layoutId == mLoadingResId) {
                if (null != animationDrawable && animationDrawable.isRunning()) {
                    animationDrawable.stop();
                    animationDrawable.selectDrawable(0);//回到动画的初始位置
                }
            }
        }
        layout(layoutId).setVisibility(VISIBLE);
    }

    private View layout(int layoutId) {
        if (mLayouts.containsKey(layoutId)) {
            if (layoutId == mLoadingResId && null != animationDrawable && !animationDrawable.isRunning()) {
                animationDrawable.start();
            }
            return mLayouts.get(layoutId);
        }
        View layout = mInflater.inflate(layoutId, this, false);
        layout.setVisibility(GONE);
        addView(layout);
        mLayouts.put(layoutId, layout);

        if (layoutId == mEmptyResId) {
            if (mOnEmptyInflateListener != null) {
                mOnEmptyInflateListener.onInflate(layout);
            }
        } else if (layoutId == mErrorResId) {
            if (mOnErrorInflateListener != null) {
                mOnErrorInflateListener.onInflate(layout);
            }
        } else if (layoutId == mLoadingResId) {
            TextView text = layout.findViewById(R.id.text_load);
            Drawable[] drawables = text.getCompoundDrawables();
            animationDrawable = (AnimationDrawable) drawables[1];//0,1,2,3对应左，上，右，下的图片
            if (!animationDrawable.isRunning()) {
                animationDrawable.start();
            }
        }
        return layout;
    }

    public interface OnInflateListener {
        void onInflate(View inflated);
    }
}