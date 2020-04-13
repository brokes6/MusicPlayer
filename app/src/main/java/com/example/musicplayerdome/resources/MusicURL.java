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
        musicurl.add("http://m10.music.126.net/20200413180135/ee0a91720c55be606d4f6b6571891130/ymusic/0309/0f09/535a/4cd23701ac8c896252b2d02be64a0401.mp3");
        musicurl.add("http://m10.music.126.net/20200413180246/a29afbd418b130d2e58dea8613929aec/ymusic/4875/f55e/3b68/e1ee835f97b279438fbcd62f20932b4c.mp3");
        musicurl.add("http://m10.music.126.net/20200413180135/ee0a91720c55be606d4f6b6571891130/ymusic/0309/0f09/535a/4cd23701ac8c896252b2d02be64a0401.mp3");
        musicurl.add("http://m10.music.126.net/20200413180246/a29afbd418b130d2e58dea8613929aec/ymusic/4875/f55e/3b68/e1ee835f97b279438fbcd62f20932b4c.mp3");
        musicurl.add("http://m10.music.126.net/20200413180135/ee0a91720c55be606d4f6b6571891130/ymusic/0309/0f09/535a/4cd23701ac8c896252b2d02be64a0401.mp3");
        musicurl.add("http://m10.music.126.net/20200413180246/a29afbd418b130d2e58dea8613929aec/ymusic/4875/f55e/3b68/e1ee835f97b279438fbcd62f20932b4c.mp3");
        musicurl.add("http://m10.music.126.net/20200413180135/ee0a91720c55be606d4f6b6571891130/ymusic/0309/0f09/535a/4cd23701ac8c896252b2d02be64a0401.mp3");
        musicurl.add("http://m10.music.126.net/20200413180246/a29afbd418b130d2e58dea8613929aec/ymusic/4875/f55e/3b68/e1ee835f97b279438fbcd62f20932b4c.mp3");
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
