package com.example.musicplayerdome.bean;

public class SongRecommendation {
    private String SongUrl;
    private String SongText;

    public String getSongUrl() {
        return SongUrl;
    }

    public void setSongUrl(String songUrl) {
        SongUrl = songUrl;
    }

    public String getSongText() {
        return SongText;
    }

    public void setSongText(String songText) {
        SongText = songText;
    }

    public SongRecommendation setImageUrl(String url){
        SongUrl = url;
        return this;
    }
    public SongRecommendation setImageText(String text){
        SongText = text;
        return this;
    }
}
