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
        musicurl.add("http://m10.music.126.net/20200423111549/7f79c74cd43db58b8412b065ab038173/ymusic/f998/bb37/c1e0/83be80389ba1192e3536b23fe5e27c03.mp3");
        musicurl.add("http://m10.music.126.net/20200423111534/c6c901a0a058f6b660fc8218fbcd705a/ymusic/074d/441c/231f/01e429488489e14edbaa78f8888e896b.mp3");
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
