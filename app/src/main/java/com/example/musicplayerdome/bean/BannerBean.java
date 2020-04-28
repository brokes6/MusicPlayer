package com.example.musicplayerdome.bean;

public class BannerBean {
    private String BannerUrl;
    private String BannerName;

    public String getBannerUrl() {
        return BannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        BannerUrl = bannerUrl;
    }

    public String getBannerName() {
        return BannerName;
    }

    public void setBannerName(String bannerName) {
        BannerName = bannerName;
    }

    public BannerBean setUrl(String url){
        BannerUrl = url;
        return this;
    }
    public BannerBean setText(String text){
        BannerName = text;
        return this;
    }
}
