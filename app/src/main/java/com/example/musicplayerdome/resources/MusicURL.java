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
        musicurl.add("http://m10.music.126.net/20200408112125/9141758a58045209127f9e311a2af940/ymusic/530f/0e0f/520f/deb70b3dd39e906d945ceeee68a1c8d0.mp3");
        musicurl.add("http://m10.music.126.net/20200408112125/a80c3530cfad54c49ef2af76e80b15f6/ymusic/5464/6d21/dc04/245e4d6a4e5e35ea74340736a103efe3.mp3");
        musicurl.add("http://m10.music.126.net/20200408112125/689100acca14a662d103b8f6227bfbe3/ymusic/050c/045f/075a/2f3a0a37a635f374d45d00d098a156b9.mp3");
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
