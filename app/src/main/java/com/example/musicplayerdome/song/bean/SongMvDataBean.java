package com.example.musicplayerdome.song.bean;

import java.util.List;

public class SongMvDataBean {
    private int code;
    private MvData data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public MvData getData() {
        return data;
    }

    public void setData(MvData data) {
        this.data = data;
    }

    public class MvData{
        private long id;
        private String url;
        private long size;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }
    }
}
