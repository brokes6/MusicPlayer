package com.example.musicplayerdome.search.bean;

import java.util.List;

public class VideoUrlBean {
    private List<urlsData> urls;
    private int code;

    public List<urlsData> getUrls() {
        return urls;
    }

    public void setUrls(List<urlsData> urls) {
        this.urls = urls;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public class urlsData{
        private String id;
        private String url;
        private int size;
        private int validityTime;
        private boolean needPay;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getValidityTime() {
            return validityTime;
        }

        public void setValidityTime(int validityTime) {
            this.validityTime = validityTime;
        }

        public boolean isNeedPay() {
            return needPay;
        }

        public void setNeedPay(boolean needPay) {
            this.needPay = needPay;
        }
    }
}
