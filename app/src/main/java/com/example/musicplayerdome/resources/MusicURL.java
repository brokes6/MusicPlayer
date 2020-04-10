package com.example.musicplayerdome.resources;

import java.util.ArrayList;
import java.util.List;

/**
 * 资源的初始化和get，set
 * @author fuxinbo
 * @since 2020/04/09 10:07
 */

public class MusicURL {
    private static final String TAG = "MusicURL";
    private List<String> musicurl = new ArrayList<>();
    public MusicURL(){
        //这里的资源得定时更换
        musicurl.add("http://m10.music.126.net/20200410113557/5870f986c0878a2b686b1c461b792220/ymusic/ca33/9655/fae1/030f10cb577fb0c8419a3034427f2199.mp3");
        musicurl.add("http://m10.music.126.net/20200410113615/4e9fa18977f66631b2b43325d1f74997/ymusic/0a61/11fc/ccb1/66ec9f821f1c5fb34c0df7f54fdd5dbd.mp3");
        musicurl.add("http://m10.music.126.net/20200410113635/42348cf5790575ab93cf4e1e4a045c50/ymusic/025b/050c/0f0f/5123a217232f21aec6a68ea58f5403bb.mp3");
        musicurl.add("http://m10.music.126.net/20200408112125/f82ad956e91c4d2264afbbc71be6e86c/ymusic/25b9/3a30/8c18/a293afa71168120a616434a2a21d97ef.mp3");
    }
    //算是增删改查
    public List<String> getMusicURL(){
        return musicurl;
    }

    public void addMusicURL(List<String> url){
        musicurl.addAll(url);
    }

    public void replaceMusicURL(List<String> url){
        musicurl.clear();
        musicurl.addAll(url);
    }

    public boolean clearMusicURL(){
        musicurl.clear();
        return true;
    }
}
