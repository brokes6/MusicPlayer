package com.example.musicplayerdome.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.example.musicplayerdome.R;
import com.example.musicplayerdome.databinding.ActivityMusicBinding;

public class MusicActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityMusicBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_music);
        initView();
    }
    private void initView(){

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }
}
