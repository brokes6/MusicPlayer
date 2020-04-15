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
        musicurl.add("http://m10.music.126.net/20200415163834/17f8e709bc468df984f5289731a92dd4/ymusic/0e59/0353/0758/93f3068e7449459898f00201d354d979.mp3");
        musicurl.add("http://m10.music.126.net/20200415163851/a8aaf5dbfb70ba91c53688eb69802507/ymusic/684b/9b31/f8f6/1116e5187b3ebed4e25309d58327ee66.mp3");
        musicurl.add("http://m10.music.126.net/20200415163834/17f8e709bc468df984f5289731a92dd4/ymusic/0e59/0353/0758/93f3068e7449459898f00201d354d979.mp3");
        musicurl.add("http://m10.music.126.net/20200415163851/a8aaf5dbfb70ba91c53688eb69802507/ymusic/684b/9b31/f8f6/1116e5187b3ebed4e25309d58327ee66.mp3");
        musicurl.add("http://m10.music.126.net/20200415163834/17f8e709bc468df984f5289731a92dd4/ymusic/0e59/0353/0758/93f3068e7449459898f00201d354d979.mp3");
        musicurl.add("http://m10.music.126.net/20200415163851/a8aaf5dbfb70ba91c53688eb69802507/ymusic/684b/9b31/f8f6/1116e5187b3ebed4e25309d58327ee66.mp3");
        musicurl.add("http://m10.music.126.net/20200415163834/17f8e709bc468df984f5289731a92dd4/ymusic/0e59/0353/0758/93f3068e7449459898f00201d354d979.mp3");
        musicurl.add("http://m10.music.126.net/20200415163851/a8aaf5dbfb70ba91c53688eb69802507/ymusic/684b/9b31/f8f6/1116e5187b3ebed4e25309d58327ee66.mp3");
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
