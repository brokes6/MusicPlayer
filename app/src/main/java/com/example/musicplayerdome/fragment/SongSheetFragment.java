package com.example.musicplayerdome.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.ActivityUtils;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.activity.SongSheetActivity;
import com.example.musicplayerdome.adapter.MainMusicAdapter;
import com.example.musicplayerdome.adapter.MySongAdapter;
import com.example.musicplayerdome.adapter.SongListAdapter;
import com.example.musicplayerdome.bean.Audio;
import com.example.musicplayerdome.databinding.SongsheetfragmentBinding;
import com.example.musicplayerdome.resources.DomeData;

import java.util.ArrayList;
import java.util.List;

public class SongSheetFragment extends Fragment implements View.OnClickListener{
    SongsheetfragmentBinding binding;
    private static final String TAG = "SongSheetFragment";
    MySongAdapter mySongAdapter;

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
        mySongAdapter = new MySongAdapter();
        LinearLayoutManager i = new LinearLayoutManager(getContext());
        i.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.mySong.setLayoutManager(i);
        binding.mySong.setAdapter(mySongAdapter);
        mySongAdapter.loadMore(DomeData.getMySong());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }
}
