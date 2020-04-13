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
        musicurl.add("http://m10.music.126.net/20200413111516/21fe129cdeafbe1aa92da4663e392de1/ymusic/6420/2b57/372a/db02ed80ddef529d716f0e031dbb091a.mp3");
        musicurl.add("http://m10.music.126.net/20200413111539/afc14c39f9ea18d88a936f294bf1f7a3/ymusic/0e59/050e/555e/93746e25762d61279d4ab49abcdd9d76.mp3");
        musicurl.add("http://m10.music.126.net/20200413111516/21fe129cdeafbe1aa92da4663e392de1/ymusic/6420/2b57/372a/db02ed80ddef529d716f0e031dbb091a.mp3");
        musicurl.add("http://m10.music.126.net/20200413111539/afc14c39f9ea18d88a936f294bf1f7a3/ymusic/0e59/050e/555e/93746e25762d61279d4ab49abcdd9d76.mp3");
        musicurl.add("http://m10.music.126.net/20200413111516/21fe129cdeafbe1aa92da4663e392de1/ymusic/6420/2b57/372a/db02ed80ddef529d716f0e031dbb091a.mp3");
        musicurl.add("http://m10.music.126.net/20200413111539/afc14c39f9ea18d88a936f294bf1f7a3/ymusic/0e59/050e/555e/93746e25762d61279d4ab49abcdd9d76.mp3");
        musicurl.add("http://m10.music.126.net/20200413111516/21fe129cdeafbe1aa92da4663e392de1/ymusic/6420/2b57/372a/db02ed80ddef529d716f0e031dbb091a.mp3");
        musicurl.add("http://m10.music.126.net/20200413111539/afc14c39f9ea18d88a936f294bf1f7a3/ymusic/0e59/050e/555e/93746e25762d61279d4ab49abcdd9d76.mp3");
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
