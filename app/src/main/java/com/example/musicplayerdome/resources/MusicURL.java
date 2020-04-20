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
        musicurl.add("http://m10.music.126.net/20200420112403/3edc73cab41195ffe3e661c5cd92a341/ymusic/87db/0a3f/8176/314d2d050f7e05564bb7eaa9aeb69ef1.mp3");
        musicurl.add("http://m10.music.126.net/20200420112319/2c7e3531fa82fe66b29d3580b7f07555/ymusic/a0bb/0881/037f/85945d812405ab5677195f518a1607de.mp3");
        musicurl.add("http://m10.music.126.net/20200420112403/3edc73cab41195ffe3e661c5cd92a341/ymusic/87db/0a3f/8176/314d2d050f7e05564bb7eaa9aeb69ef1.mp3");
        musicurl.add("http://m10.music.126.net/20200420112319/2c7e3531fa82fe66b29d3580b7f07555/ymusic/a0bb/0881/037f/85945d812405ab5677195f518a1607de.mp3");
        musicurl.add("http://m10.music.126.net/20200420112403/3edc73cab41195ffe3e661c5cd92a341/ymusic/87db/0a3f/8176/314d2d050f7e05564bb7eaa9aeb69ef1.mp3");
        musicurl.add("http://m10.music.126.net/20200420112319/2c7e3531fa82fe66b29d3580b7f07555/ymusic/a0bb/0881/037f/85945d812405ab5677195f518a1607de.mp3");
        musicurl.add("http://m10.music.126.net/20200420112403/3edc73cab41195ffe3e661c5cd92a341/ymusic/87db/0a3f/8176/314d2d050f7e05564bb7eaa9aeb69ef1.mp3");
        musicurl.add("http://m10.music.126.net/20200420112319/2c7e3531fa82fe66b29d3580b7f07555/ymusic/a0bb/0881/037f/85945d812405ab5677195f518a1607de.mp3");
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
