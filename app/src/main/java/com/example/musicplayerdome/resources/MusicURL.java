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
        musicurl.add("http://m10.music.126.net/20200411123008/c6826acd650b4771e660aa1b0e6131fd/ymusic/74ca/73cc/f70a/653cca1c8bc3559a3b92a4fccab56682.mp3");
        musicurl.add("http://m10.music.126.net/20200411123034/03d32951ebd1d49c0c73550793cd0dcf/ymusic/4875/f55e/3b68/e1ee835f97b279438fbcd62f20932b4c.mp3");
        musicurl.add("http://m10.music.126.net/20200411123008/c6826acd650b4771e660aa1b0e6131fd/ymusic/74ca/73cc/f70a/653cca1c8bc3559a3b92a4fccab56682.mp3");
        musicurl.add("http://m10.music.126.net/20200411123034/03d32951ebd1d49c0c73550793cd0dcf/ymusic/4875/f55e/3b68/e1ee835f97b279438fbcd62f20932b4c.mp3");
        musicurl.add("http://m10.music.126.net/20200411123008/c6826acd650b4771e660aa1b0e6131fd/ymusic/74ca/73cc/f70a/653cca1c8bc3559a3b92a4fccab56682.mp3");
        musicurl.add("http://m10.music.126.net/20200411123034/03d32951ebd1d49c0c73550793cd0dcf/ymusic/4875/f55e/3b68/e1ee835f97b279438fbcd62f20932b4c.mp3");
        musicurl.add("http://m10.music.126.net/20200411123008/c6826acd650b4771e660aa1b0e6131fd/ymusic/74ca/73cc/f70a/653cca1c8bc3559a3b92a4fccab56682.mp3");
        musicurl.add("http://m10.music.126.net/20200411123034/03d32951ebd1d49c0c73550793cd0dcf/ymusic/4875/f55e/3b68/e1ee835f97b279438fbcd62f20932b4c.mp3");
        musicurl.add("http://m10.music.126.net/20200411123008/c6826acd650b4771e660aa1b0e6131fd/ymusic/74ca/73cc/f70a/653cca1c8bc3559a3b92a4fccab56682.mp3");
        musicurl.add("http://m10.music.126.net/20200411123034/03d32951ebd1d49c0c73550793cd0dcf/ymusic/4875/f55e/3b68/e1ee835f97b279438fbcd62f20932b4c.mp3");
        musicurl.add("http://m10.music.126.net/20200411123008/c6826acd650b4771e660aa1b0e6131fd/ymusic/74ca/73cc/f70a/653cca1c8bc3559a3b92a4fccab56682.mp3");
        musicurl.add("http://m10.music.126.net/20200411123034/03d32951ebd1d49c0c73550793cd0dcf/ymusic/4875/f55e/3b68/e1ee835f97b279438fbcd62f20932b4c.mp3");
        musicurl.add("http://m10.music.126.net/20200411123008/c6826acd650b4771e660aa1b0e6131fd/ymusic/74ca/73cc/f70a/653cca1c8bc3559a3b92a4fccab56682.mp3");
        musicurl.add("http://m10.music.126.net/20200411123034/03d32951ebd1d49c0c73550793cd0dcf/ymusic/4875/f55e/3b68/e1ee835f97b279438fbcd62f20932b4c.mp3");
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
