package com.example.musicplayerdome.history.bean;

import com.example.musicplayerdome.personal.bean.UserPlaylistBean;

import java.util.List;

public class SonghistoryBean {
    private int code;

    private List<PlaylistBean> weekData;

    private List<PlaylistBean> allData;

    public static class PlaylistBean {

        private int playCount;

        private int score;

        private List<Song> song;

        public static class Song{

            private String name;
            private long id;
            private int pst;
            private int t;
            private List<author>ar;
            private List<tile>alia;
            private List<al>al;
            public static class author{
                private String name;
            }
            public static class tile{
                private String name;
            }
            public static class al{
                private long id;
                private String name;
                private String picUrl;
            }

        }

    }
}
