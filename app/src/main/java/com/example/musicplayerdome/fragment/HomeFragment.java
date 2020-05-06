package com.example.musicplayerdome.fragment;

import android.graphics.Outline;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;

import com.example.musicplayerdome.rewrite.GlideImageLoader;
import com.example.musicplayerdome.R;
import com.example.musicplayerdome.adapter.RecommendMusicAdapter;
import com.example.musicplayerdome.adapter.SongListAdapter;
import com.example.musicplayerdome.databinding.FragmentHomeBinding;
import com.example.musicplayerdome.resources.DomeData;
import com.youth.banner.BannerConfig;


public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    SongListAdapter songListAdapter;
    RecommendMusicAdapter recommendMusicAdapter;
    public static final String PLAYLIST_NAME = "playlistName";
    public static final String PLAYLIST_PICURL = "playlistPicUrl";
    public static final String PLAYLIST_CREATOR_NICKNAME = "playlistCreatorNickname";
    public static final String PLAYLIST_CREATOR_AVATARURL = "playlistCreatorAvatarUrl";
    public static final String PLAYLIST_ID = "playlistId";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false);
        initView();
        initBanner();
        return binding.getRoot();
    }
    private void initView(){
        songListAdapter = new SongListAdapter();
        LinearLayoutManager i = new LinearLayoutManager(getContext());
        i.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.songList.setLayoutManager(i);
        binding.songList.setAdapter(songListAdapter);
        songListAdapter.loadMore(DomeData.getSongRecommendation());

        recommendMusicAdapter = new RecommendMusicAdapter(getContext());
        LinearLayoutManager i1 = new LinearLayoutManager(getContext());
        i1.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recommendMusic.setLayoutManager(i1);
        binding.recommendMusic.setAdapter(recommendMusicAdapter);
        recommendMusicAdapter.loadMore(DomeData.getRecommendMusic());
    }
    private void initBanner(){
        binding.banner.setImageLoader(new GlideImageLoader());
        binding.banner.setImages(DomeData.getBanner());
        binding.banner.setIndicatorGravity(BannerConfig.CENTER);
        binding.banner.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), 30);
            }
        });
        binding.banner.setClipToOutline(true);
        binding.banner.start();
    }
}
