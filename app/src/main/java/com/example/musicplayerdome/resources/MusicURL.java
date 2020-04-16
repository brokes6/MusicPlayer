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
        musicurl.add("http://m10.music.126.net/20200416101410/34fc4f4637ec42338d62e5d1d45c276c/ymusic/148c/9061/a198/f79f2f535b7472f0ee439bcc829c2b71.mp3");
        musicurl.add("http://m10.music.126.net/20200416101317/e12d7af2b278400fd88aa3d69a636b77/ymusic/0309/0f09/535a/4cd23701ac8c896252b2d02be64a0401.mp3");
        musicurl.add("http://m10.music.126.net/20200416101410/34fc4f4637ec42338d62e5d1d45c276c/ymusic/148c/9061/a198/f79f2f535b7472f0ee439bcc829c2b71.mp3");
        musicurl.add("http://m10.music.126.net/20200416101317/e12d7af2b278400fd88aa3d69a636b77/ymusic/0309/0f09/535a/4cd23701ac8c896252b2d02be64a0401.mp3");
        musicurl.add("http://m10.music.126.net/20200416101410/34fc4f4637ec42338d62e5d1d45c276c/ymusic/148c/9061/a198/f79f2f535b7472f0ee439bcc829c2b71.mp3");
        musicurl.add("http://m10.music.126.net/20200416101317/e12d7af2b278400fd88aa3d69a636b77/ymusic/0309/0f09/535a/4cd23701ac8c896252b2d02be64a0401.mp3");
        musicurl.add("http://m10.music.126.net/20200416101410/34fc4f4637ec42338d62e5d1d45c276c/ymusic/148c/9061/a198/f79f2f535b7472f0ee439bcc829c2b71.mp3");
        musicurl.add("http://m10.music.126.net/20200416101317/e12d7af2b278400fd88aa3d69a636b77/ymusic/0309/0f09/535a/4cd23701ac8c896252b2d02be64a0401.mp3");
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
