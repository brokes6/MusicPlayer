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
        musicurl.add("http://m10.music.126.net/20200416094630/f253ca50accd802c4bc79b5e376704f6/ymusic/545d/015b/015c/c766079b1861b8740a1766e6bfa59442.mp3");
        musicurl.add("http://m10.music.126.net/20200416094702/89553b97c9021756ef73b8b4f308c974/ymusic/0309/0f09/535a/4cd23701ac8c896252b2d02be64a0401.mp3");
        musicurl.add("http://m10.music.126.net/20200416094630/f253ca50accd802c4bc79b5e376704f6/ymusic/545d/015b/015c/c766079b1861b8740a1766e6bfa59442.mp3");
        musicurl.add("http://m10.music.126.net/20200416094702/89553b97c9021756ef73b8b4f308c974/ymusic/0309/0f09/535a/4cd23701ac8c896252b2d02be64a0401.mp3");
        musicurl.add("http://m10.music.126.net/20200416094630/f253ca50accd802c4bc79b5e376704f6/ymusic/545d/015b/015c/c766079b1861b8740a1766e6bfa59442.mp3");
        musicurl.add("http://m10.music.126.net/20200416094702/89553b97c9021756ef73b8b4f308c974/ymusic/0309/0f09/535a/4cd23701ac8c896252b2d02be64a0401.mp3");
        musicurl.add("http://m10.music.126.net/20200416094630/f253ca50accd802c4bc79b5e376704f6/ymusic/545d/015b/015c/c766079b1861b8740a1766e6bfa59442.mp3");
        musicurl.add("http://m10.music.126.net/20200416094702/89553b97c9021756ef73b8b4f308c974/ymusic/0309/0f09/535a/4cd23701ac8c896252b2d02be64a0401.mp3");
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
