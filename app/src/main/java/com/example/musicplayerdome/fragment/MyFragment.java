package com.example.musicplayerdome.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.musicplayerdome.R;
import com.example.musicplayerdome.databinding.MyfragmentBinding;
import com.example.musicplayerdome.databinding.SongsheetfragmentBinding;

public class MyFragment extends Fragment {
    MyfragmentBinding binding;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.myfragment,container,false);
        return binding.getRoot();
    }
}
