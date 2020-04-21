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
        musicurl.add("http://m10.music.126.net/20200421155346/7e785119836651fde8c4c8b821e40a00/ymusic/f998/bb37/c1e0/83be80389ba1192e3536b23fe5e27c03.mp3");
        musicurl.add("http://m10.music.126.net/20200421155402/1c29d1728785e65289f5d1e96eea7e83/ymusic/4f6f/a946/6b6e/c84f65560562aab698c824eaf5a1ff76.mp3");
        musicurl.add("http://m10.music.126.net/20200421155346/7e785119836651fde8c4c8b821e40a00/ymusic/f998/bb37/c1e0/83be80389ba1192e3536b23fe5e27c03.mp3");
        musicurl.add("http://m10.music.126.net/20200421155402/1c29d1728785e65289f5d1e96eea7e83/ymusic/4f6f/a946/6b6e/c84f65560562aab698c824eaf5a1ff76.mp3");
        musicurl.add("http://m10.music.126.net/20200421155346/7e785119836651fde8c4c8b821e40a00/ymusic/f998/bb37/c1e0/83be80389ba1192e3536b23fe5e27c03.mp3");
        musicurl.add("http://m10.music.126.net/20200421155402/1c29d1728785e65289f5d1e96eea7e83/ymusic/4f6f/a946/6b6e/c84f65560562aab698c824eaf5a1ff76.mp3");
        musicurl.add("http://m10.music.126.net/20200421155346/7e785119836651fde8c4c8b821e40a00/ymusic/f998/bb37/c1e0/83be80389ba1192e3536b23fe5e27c03.mp3");
        musicurl.add("http://m10.music.126.net/20200421155402/1c29d1728785e65289f5d1e96eea7e83/ymusic/4f6f/a946/6b6e/c84f65560562aab698c824eaf5a1ff76.mp3");
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
