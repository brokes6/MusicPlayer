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
        musicurl.add("http://m10.music.126.net/20200425165321/18a714a8d124fa57bb07a0e607d6e795/ymusic/f998/bb37/c1e0/83be80389ba1192e3536b23fe5e27c03.mp3");
        musicurl.add("http://m10.music.126.net/20200425165336/1cbb626d9fa5d2721de7029b7e21dbb1/ymusic/074d/441c/231f/01e429488489e14edbaa78f8888e896b.mp3");
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
