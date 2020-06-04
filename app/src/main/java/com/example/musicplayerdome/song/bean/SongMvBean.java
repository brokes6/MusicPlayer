package com.example.musicplayerdome.song.bean;

import java.io.Serializable;
import java.util.List;

public class SongMvBean implements Serializable {
    private int code;
    private long time;
    private boolean hasMore;
    private List<MvData> mvs;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public List<MvData> getMvs() {
        return mvs;
    }

    public void setMvs(List<MvData> mvs) {
        this.mvs = mvs;
    }

    public void addMvs(List<MvData> mvs){
        this.mvs.addAll(mvs);
    }

    public class MvData implements Serializable{
        private long id;
        private String name;
        private String artistName;
        private String imgurl;
        private ArtistData artist;
        private long duration;
        private int playCount;
        private String publishTime;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getArtistName() {
            return artistName;
        }

        public void setArtistName(String artistName) {
            this.artistName = artistName;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public ArtistData getArtist() {
            return artist;
        }

        public void setArtist(ArtistData artist) {
            this.artist = artist;
        }

        public long getDuration() {
            return duration;
        }

        public void setDuration(long duration) {
            this.duration = duration;
        }

        public int getPlayCount() {
            return playCount;
        }

        public void setPlayCount(int playCount) {
            this.playCount = playCount;
        }

        public String getPublishTime() {
            return publishTime;
        }

        public void setPublishTime(String publishTime) {
            this.publishTime = publishTime;
        }

        public class ArtistData implements Serializable{
            private long img1v1Id;
            private String img1v1Url;
            private String name;
            private int id;

            public long getImg1v1Id() {
                return img1v1Id;
            }

            public void setImg1v1Id(long img1v1Id) {
                this.img1v1Id = img1v1Id;
            }

            public String getImg1v1Url() {
                return img1v1Url;
            }

            public void setImg1v1Url(String img1v1Url) {
                this.img1v1Url = img1v1Url;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }
        }
    }
}
