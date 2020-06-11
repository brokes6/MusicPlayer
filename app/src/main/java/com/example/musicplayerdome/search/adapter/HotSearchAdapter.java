package com.example.musicplayerdome.search.adapter;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.musicplayerdome.R;
import com.example.musicplayerdome.search.bean.HotSearchDetailBean;
import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;

/**
 * 热门搜索适配器（展示热门搜索）
 * HotSearchDetailBean.DataBean
 */
public class HotSearchAdapter extends BaseRecyclerAdapter<HotSearchDetailBean.DataBean> {
    private Context context;
    private HotSearchDetailBean list;
    private OnHotSearchAdapterClickListener listener;
    private TextView tvName,tvNumber,tvCount,tvDescription;
    private RelativeLayout rlHotSearch;
    public HotSearchAdapter(Context context) {
        this.context = context;
    }

    public void setListener(OnHotSearchAdapterClickListener listener) {
        this.listener = listener;
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.item_hot_search;
    }

    @Override
    protected void bindData(@NonNull RecyclerViewHolder holder, int position, HotSearchDetailBean.DataBean item) {
        tvName = holder.findViewById(R.id.tv_name);
        tvNumber = holder.findViewById(R.id.tv_number);
        tvCount = holder.findViewById(R.id.tv_count);
        tvDescription = holder.findViewById(R.id.tv_description);
        rlHotSearch = holder.findViewById(R.id.rl_hot_search);
        if (item!=null){
            setBean(item,position);
            setListener(listener, position);
        }
    }

    public void setBean(HotSearchDetailBean.DataBean list, int position) {
        tvName.setText(list.getSearchWord());
        tvNumber.setText(position + 1 + "");
        tvCount.setText(list.getScore() + "");
        tvDescription.setText(list.getContent());
    }

    public void setListener(OnHotSearchAdapterClickListener listener, int i) {
        rlHotSearch.setOnClickListener(v -> {
            if (listener != null) {
                listener.onHotSearchClick(i);
            }
        });
    }

    public interface OnHotSearchAdapterClickListener {
        void onHotSearchClick(int position);
    }

}
