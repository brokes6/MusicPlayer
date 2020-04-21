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
        musicurl.add("http://m10.music.126.net/20200421112559/8709d7c2eca9c3906b755d044673a990/ymusic/2ef7/207c/4ea8/639616b07d8da803b47297959a1d5803.mp3");
        musicurl.add("http://m10.music.126.net/20200421112618/892c57861f4db192ae1669dc79011545/ymusic/54ac/288b/1b92/fd4b004b06ff121ce0cd7aef51087b1c.mp3");
        musicurl.add("http://m10.music.126.net/20200421112559/8709d7c2eca9c3906b755d044673a990/ymusic/2ef7/207c/4ea8/639616b07d8da803b47297959a1d5803.mp3");
        musicurl.add("http://m10.music.126.net/20200421112618/892c57861f4db192ae1669dc79011545/ymusic/54ac/288b/1b92/fd4b004b06ff121ce0cd7aef51087b1c.mp3");
        musicurl.add("http://m10.music.126.net/20200421112559/8709d7c2eca9c3906b755d044673a990/ymusic/2ef7/207c/4ea8/639616b07d8da803b47297959a1d5803.mp3");
        musicurl.add("http://m10.music.126.net/20200421112618/892c57861f4db192ae1669dc79011545/ymusic/54ac/288b/1b92/fd4b004b06ff121ce0cd7aef51087b1c.mp3");
        musicurl.add("http://m10.music.126.net/20200421112559/8709d7c2eca9c3906b755d044673a990/ymusic/2ef7/207c/4ea8/639616b07d8da803b47297959a1d5803.mp3");
        musicurl.add("http://m10.music.126.net/20200421112618/892c57861f4db192ae1669dc79011545/ymusic/54ac/288b/1b92/fd4b004b06ff121ce0cd7aef51087b1c.mp3");
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
