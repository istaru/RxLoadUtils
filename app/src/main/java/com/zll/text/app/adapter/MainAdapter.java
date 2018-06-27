package com.zll.text.app.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zll.text.app.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Moon on 2018/6/26.
 */

public class MainAdapter extends BaseAdapter {

    private List<Map<String, Object>> listMap;

    public MainAdapter() {
        listMap = new ArrayList<>();
    }


    @Override
    public void addRecyclerData(List list, int pageIndex) {
        if (pageIndex == 1) {
            listMap.clear();
        }
        listMap.addAll(list);
    }

    @Override
    protected int getItem() {
        return listMap.size();
    }

    @Override
    protected int getItemType(int position) {
        return 0;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateView(ViewGroup parent, int viewType) {
        return new EventsHolder(inflate(parent, R.layout.item_events));
    }

    @Override
    protected void onBindView(RecyclerView.ViewHolder holder, int position) {
        EventsHolder viewHolder = (EventsHolder) holder;
        Map<String, Object> map = listMap.get(position);
        Glide.with(holder.itemView.getContext())
                .load(map.get("activityImg"))
//                .transform(new GlideRoundTransform(viewHolder.itemView.getContext(), 10))//设置圆角
//                .placeholder(R.mipmap.banner_placeholder)//加载中的图片
//                .error(R.mipmap.banner_placeholder)//加载出错的图片
                .priority(Priority.HIGH)//优先加载
                .diskCacheStrategy(DiskCacheStrategy.ALL)//设置缓存策略
                .into(viewHolder.eventsImg);
        viewHolder.eventsTxt.setText(map.get("activityTitle") + "");
        viewHolder.eventsType.setText(map.get("activityTypeStr") + "");
        int activityType = (int) map.get("activityType");
        if (activityType == 1) {
            viewHolder.eventsTxt.setTextColor(ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.gray_q));
            viewHolder.eventsType.setBackgroundResource(R.mipmap.huodong_over_bg);
        } else if (activityType == 2) {
            viewHolder.eventsTxt.setTextColor(ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.black));
            viewHolder.eventsType.setBackgroundResource(R.mipmap.huodong_xianshi_or_bg);
        } else {
            viewHolder.eventsTxt.setTextColor(ContextCompat.getColor(viewHolder.itemView.getContext(), R.color.black));
            viewHolder.eventsType.setBackgroundResource(R.mipmap.huodong_blue_bg);
        }
    }

    class EventsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView eventsImg;
        private TextView eventsTxt, eventsType;

        public EventsHolder(View itemView) {
            super(itemView);
            eventsImg = (ImageView) itemView.findViewById(R.id.events_img);
            eventsTxt = (TextView) itemView.findViewById(R.id.events_txt);
            eventsType = (TextView) itemView.findViewById(R.id.events_type);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickListener.onItemClick(v, getAdapterPosition(), listMap);
        }
    }
}
