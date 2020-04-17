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
        musicurl.add("http://m10.music.126.net/20200417095925/57a8675e892b5c8d9602fe00777391f0/ymusic/5308/565e/0f5a/dbf5f42a96a41226ee828035aa896eab.mp3");
        musicurl.add("http://m701.music.126.net/20200417095957/a1d2e38ed02b32d67e707aa9b64f0dcd/jdymusic/obj/w5zDlMODwrDDiGjCn8Ky/1669400994/cc9c/0bde/52c2/daf709e38cfbcc3c55dc90e42a731174.mp3");
        musicurl.add("http://m10.music.126.net/20200417095925/57a8675e892b5c8d9602fe00777391f0/ymusic/5308/565e/0f5a/dbf5f42a96a41226ee828035aa896eab.mp3");
        musicurl.add("http://m701.music.126.net/20200417095957/a1d2e38ed02b32d67e707aa9b64f0dcd/jdymusic/obj/w5zDlMODwrDDiGjCn8Ky/1669400994/cc9c/0bde/52c2/daf709e38cfbcc3c55dc90e42a731174.mp3");
        musicurl.add("http://m10.music.126.net/20200417095925/57a8675e892b5c8d9602fe00777391f0/ymusic/5308/565e/0f5a/dbf5f42a96a41226ee828035aa896eab.mp3");
        musicurl.add("http://m701.music.126.net/20200417095957/a1d2e38ed02b32d67e707aa9b64f0dcd/jdymusic/obj/w5zDlMODwrDDiGjCn8Ky/1669400994/cc9c/0bde/52c2/daf709e38cfbcc3c55dc90e42a731174.mp3");
        musicurl.add("http://m10.music.126.net/20200417095925/57a8675e892b5c8d9602fe00777391f0/ymusic/5308/565e/0f5a/dbf5f42a96a41226ee828035aa896eab.mp3");
        musicurl.add("http://m701.music.126.net/20200417095957/a1d2e38ed02b32d67e707aa9b64f0dcd/jdymusic/obj/w5zDlMODwrDDiGjCn8Ky/1669400994/cc9c/0bde/52c2/daf709e38cfbcc3c55dc90e42a731174.mp3");
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
