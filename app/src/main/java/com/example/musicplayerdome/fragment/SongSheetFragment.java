package com.example.musicplayerdome.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.musicplayerdome.R;
import com.example.musicplayerdome.abstractclass.OnItemListenter;
import com.example.musicplayerdome.activity.MainActivity;
import com.example.musicplayerdome.activity.MusicActivity;
import com.example.musicplayerdome.adapter.MainMusicAdapter;
import com.example.musicplayerdome.bean.Audio;
import com.example.musicplayerdome.databinding.SongsheetfragmentBinding;
import com.example.musicplayerdome.resources.MusicURL;

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
        setMusicList();
        initView();
        return binding.getRoot();
    }

    private void setMusicList() {
        MusicURL musicURL = new MusicURL();
        fileArr = musicURL.getMusicURL();
        for (int i = 0; i < fileArr.size(); i++) {
            Audio audio = new Audio();
            audio.setFileUrl(fileArr.get(i));
            audio.setId(i + 1);
            audio.setType(1);
            audio.setName("音乐" + (i + 1));
            audioList.add(audio);
        }
    }
    private void initView(){
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recyclerView.setLayoutManager(lm);
        binding.recyclerView.setAdapter(mainMusicAdapter = new MainMusicAdapter(getContext()));
        /**
         * 回调监听也行，但没必要，只需要在加个参数用来判断就行了
         * 通过抽象类来回调监听
         * 这边才是真正的方法
         */
        mainMusicAdapter.setOnItemClickListener(new OnItemListenter() {
            @Override
            public void onItemClick(View view, int postionid) {
                Intent intent = new Intent(getContext(), MusicActivity.class);
                intent.putExtra ("sid",postionid);
                intent.putExtra ("skey",true);
                startActivity(intent);
            }
        });
        mainMusicAdapter.loadMore(audioList);
    }

    @Override
    public void onClick(View v) {

    }
}
