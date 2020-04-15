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
        musicurl.add("http://m10.music.126.net/20200415144838/629d90f7a5990df52f0ba39a3836f95d/ymusic/530f/0153/520b/de4caae28069f8b035bfea6d75012fad.mp3");
        musicurl.add("http://m10.music.126.net/20200415144853/5b99706c17b14f97b094d36581dc6ac5/ymusic/0e59/0353/0758/93f3068e7449459898f00201d354d979.mp3");
        musicurl.add("http://m10.music.126.net/20200415144838/629d90f7a5990df52f0ba39a3836f95d/ymusic/530f/0153/520b/de4caae28069f8b035bfea6d75012fad.mp3");
        musicurl.add("http://m10.music.126.net/20200415144853/5b99706c17b14f97b094d36581dc6ac5/ymusic/0e59/0353/0758/93f3068e7449459898f00201d354d979.mp3");
        musicurl.add("http://m10.music.126.net/20200415144838/629d90f7a5990df52f0ba39a3836f95d/ymusic/530f/0153/520b/de4caae28069f8b035bfea6d75012fad.mp3");
        musicurl.add("http://m10.music.126.net/20200415144853/5b99706c17b14f97b094d36581dc6ac5/ymusic/0e59/0353/0758/93f3068e7449459898f00201d354d979.mp3");
        musicurl.add("http://m10.music.126.net/20200415144838/629d90f7a5990df52f0ba39a3836f95d/ymusic/530f/0153/520b/de4caae28069f8b035bfea6d75012fad.mp3");
        musicurl.add("http://m10.music.126.net/20200415144853/5b99706c17b14f97b094d36581dc6ac5/ymusic/0e59/0353/0758/93f3068e7449459898f00201d354d979.mp3");
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
