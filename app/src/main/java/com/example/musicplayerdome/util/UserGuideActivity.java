package com.example.musicplayerdome.util;

import android.app.Activity;

import com.example.musicplayerdome.R;
import com.example.musicplayerdome.activity.LoginActivity;
import com.xuexiang.xui.widget.activity.BaseGuideActivity;

import java.util.ArrayList;
import java.util.List;

public class UserGuideActivity extends BaseGuideActivity {

    @Override
    protected List<Object> getGuideResourceList() {
        List<Object> list = new ArrayList<>();
        list.add(R.mipmap.guide_img_1);
        list.add(R.mipmap.guide_img_2);
        list.add(R.mipmap.guide_img_3);
        list.add(R.mipmap.guide_img_4);
        return list;
    }

    @Override
    protected Class<? extends Activity> getSkipClass() {
        return LoginActivity.class;
    }
}
