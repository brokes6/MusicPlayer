package com.example.musicplayerdome.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.ActivityUtils;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.activity.SongSheetActivity;
import com.example.musicplayerdome.adapter.MainMusicAdapter;
import com.example.musicplayerdome.bean.Audio;
import com.example.musicplayerdome.databinding.SongsheetfragmentBinding;

import java.util.ArrayList;
import java.util.List;

public class SongSheetFragment extends Fragment implements View.OnClickListener{
    SongsheetfragmentBinding binding;
    private MainMusicAdapter mainMusicAdapter;
    private long firstTime = 0;
    private List<Audio> audioList = new ArrayList<>();
    private List<String> fileArr = new ArrayList<>();
    private static final String TAG = "SongSheetFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.songsheetfragment,container,false);
        initView();
        return binding.getRoot();
    }


    private void initView(){
        binding.songSheet1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.song_sheet_1:
                ActivityUtils.startActivity(SongSheetActivity.class);
                break;
        }
    }
}
