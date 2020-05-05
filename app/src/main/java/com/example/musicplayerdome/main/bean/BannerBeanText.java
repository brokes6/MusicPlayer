package com.example.musicplayerdome.main.bean;

public class BannerBeanText {
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

    public BannerBeanText setUrl(String url){
        BannerUrl = url;
        return this;
    }
    public BannerBeanText setText(String text){
        BannerName = text;
        return this;
    }
}
