package com.example.musicplayerdome.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


import androidx.annotation.NonNull;

import com.example.musicplayerdome.R;
import com.example.musicplayerdome.util.MyUtil;
import com.example.musicplayerdome.util.SP;
import com.example.musicplayerdome.util.SPManager;
import com.example.musicplayerdome.util.TimerEntity;
import com.example.musicplayerdome.util.TimerFlag;
import com.example.musicplayerdome.util.XToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingxingxing on 2019.4.4
 * 音频播放定时器弹窗
 */
public class AudioTimerDialog extends Dialog implements View.OnClickListener {
    private Activity mContext;
    private String TAG = "--ErrorCorrectionDialog--";
    private MyAdapter adapter;

    public AudioTimerDialog(Context context) {
        super(context);
        init(context);
    }

    public AudioTimerDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }

    /**
     * 初始化
     */
    private void init(Context context) {
        mContext = (Activity) context;
        //为弹窗绑定视图
        View view = mContext.getLayoutInflater().inflate(R.layout.audio_timer_dialog, null);
        setContentView(view);
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        //设置弹窗宽度
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //为弹窗绑定效果
        dialogWindow.setWindowAnimations(R.style.popupAnimation);
        dialogWindow.setAttributes(lp);
        //dissmiss
        view.findViewById(R.id.cancel_action).setOnClickListener(this);
        //绑定适配器
        ListView listView = findViewById(R.id.listviewId);
        adapter = new MyAdapter(getContext());
        adapter.setData(getData());
        listView.setAdapter(adapter);
    }

    private List<TimerEntity> getData() {
        List<TimerEntity> list = new ArrayList<>();
        list.add(new TimerEntity("不开启", TimerFlag.CLOSE));
        list.add(new TimerEntity("播完当前声音", TimerFlag.CURRENT));
        list.add(new TimerEntity("15分钟", TimerFlag.TIMER_15));
        list.add(new TimerEntity("30分钟", TimerFlag.TIMER_30));
        list.add(new TimerEntity("40分钟", TimerFlag.TIMER_40));
        list.add(new TimerEntity("60分钟", TimerFlag.TIMER_60));
        list.add(new TimerEntity("90分钟", TimerFlag.TIMER_90));
        return list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //取消
            case R.id.cancel_action:
                Log.d(TAG, " 取消");
                dismiss();
                break;
        }
    }

    private OnTimerListener onTimerListener;

    /**
     * 回调监听
     * @param onTimerListener
     */
    public void setOnChangeListener(OnTimerListener onTimerListener) {
        this.onTimerListener = onTimerListener;
    }

    /**
     * 更新定时方式
     */
    public void updateSelect() {
        if (adapter != null) {
            adapter.updateSelect();
        }
    }
    //抽象类来作为接口
    public interface OnTimerListener {
        void OnChange();
    }

    /**
     * 内部类适配器
     */
    private class MyAdapter extends BaseAdapter {
        List<TimerEntity> data;
        private Context context;

        public MyAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return data == null ? 0 : data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.audio_timer_item, null);
                holder.selectView = convertView.findViewById(R.id.select_img);
                holder.titleTv = convertView.findViewById(R.id.title);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (data == null || position > data.size() - 1) return convertView;
            final TimerEntity entity = data.get(position);
            //标题
            MyUtil.setText(holder.titleTv, entity.getTitle());
            //背景
            MyUtil.setBgColor(context, convertView, entity.isSelect() ? R.color.f5f5f5 : R.color.white);
            //选中按钮
            MyUtil.setVisible(holder.selectView, entity.isSelect() ? View.VISIBLE : View.GONE);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (entity.isSelect()) return;
                    clear();
                    entity.setSelect(true);
                    SPManager.write(context, SP.TIMER_STATE, entity.getTimeState());
                    updateTimer();
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }

        private void updateTimer() {
            if (onTimerListener != null) {
                onTimerListener.OnChange();
            }
        }

        private void clear() {
            if (data == null) return;
            for (TimerEntity entity : data) {
                if (entity.isSelect()) {
                    entity.setSelect(false);
                }
            }
        }

        public void setData(List<TimerEntity> data) {
            this.data = data;
            updateSelect();
        }

        /**
         * 更新定时方式
         */
        public void updateSelect() {
            if (data == null) return;
            int timerState = SPManager.getTimerState(mContext);
            for (TimerEntity entity : data) {
                if (entity.getTimeState() == timerState) {
                    entity.setSelect(true);
                } else {
                    entity.setSelect(false);
                }
            }
            notifyDataSetChanged();
        }

        private class ViewHolder {
            private TextView titleTv;
            private View selectView;
        }
    }
}
