package com.example.musicplayerdome.resources;

import java.util.ArrayList;
import java.util.List;

public class MusicImg {
    private List<String> musicurl = new ArrayList<>();
    public MusicImg(){
        musicurl.add("https://p3fx.kgimg.com/stdmusic/20150718/20150718073742794801.jpg");
        musicurl.add("https://p3fx.kgimg.com/stdmusic/20170803/20170803105723296968.jpg");
    }
    public List<String> getMusicIMG(){
        return musicurl;
    }
}
