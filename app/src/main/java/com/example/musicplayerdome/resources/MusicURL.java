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
        musicurl.add("http://m10.music.126.net/20200413104126/9f101cbb706980f65c4e98ae5f31a893/ymusic/6420/2b57/372a/db02ed80ddef529d716f0e031dbb091a.mp3");
        musicurl.add("http://m10.music.126.net/20200413101513/fef1272b3ea2830c4ad3d074e54ea315/ymusic/0e59/050e/555e/93746e25762d61279d4ab49abcdd9d76.mp3");
        musicurl.add("http://m10.music.126.net/20200413101538/18445914a2877794fdf3fa3793e6506f/ymusic/6420/2b57/372a/db02ed80ddef529d716f0e031dbb091a.mp3");
        musicurl.add("http://m10.music.126.net/20200413101513/fef1272b3ea2830c4ad3d074e54ea315/ymusic/0e59/050e/555e/93746e25762d61279d4ab49abcdd9d76.mp3");
        musicurl.add("http://m10.music.126.net/20200413101538/18445914a2877794fdf3fa3793e6506f/ymusic/6420/2b57/372a/db02ed80ddef529d716f0e031dbb091a.mp3");
        musicurl.add("http://m10.music.126.net/20200413101513/fef1272b3ea2830c4ad3d074e54ea315/ymusic/0e59/050e/555e/93746e25762d61279d4ab49abcdd9d76.mp3");
        musicurl.add("http://m10.music.126.net/20200413101538/18445914a2877794fdf3fa3793e6506f/ymusic/6420/2b57/372a/db02ed80ddef529d716f0e031dbb091a.mp3");
        musicurl.add("http://m10.music.126.net/20200413101513/fef1272b3ea2830c4ad3d074e54ea315/ymusic/0e59/050e/555e/93746e25762d61279d4ab49abcdd9d76.mp3");
        musicurl.add("http://m10.music.126.net/20200413101538/18445914a2877794fdf3fa3793e6506f/ymusic/6420/2b57/372a/db02ed80ddef529d716f0e031dbb091a.mp3");
        musicurl.add("http://m10.music.126.net/20200413101513/fef1272b3ea2830c4ad3d074e54ea315/ymusic/0e59/050e/555e/93746e25762d61279d4ab49abcdd9d76.mp3");
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
