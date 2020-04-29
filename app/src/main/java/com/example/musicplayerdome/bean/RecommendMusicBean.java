package com.example.musicplayerdome.bean;

public class RecommendMusicBean {
    private String Rimg;
    private String Rtext;
    private String Rauthor;
    private String Rintroduce;
    private int Rid;

    public String getRimg() {
        return Rimg;
    }

    public void setRimg(String rimg) {
        Rimg = rimg;
    }

    public String getRtext() {
        return Rtext;
    }

    public void setRtext(String rtext) {
        Rtext = rtext;
    }

    public String getRauthor() {
        return Rauthor;
    }

    public void setRauthor(String rauthor) {
        Rauthor = rauthor;
    }

    public String getRintroduce() {
        return Rintroduce;
    }

    public void setRintroduce(String rintroduce) {
        Rintroduce = rintroduce;
    }

    public int getRid() {
        return Rid;
    }

    public void setRid(int rid) {
        Rid = rid;
    }
    public RecommendMusicBean setUrl(String url){
        Rimg = url;
        return this;
    }
    public RecommendMusicBean setText(String text){
        Rtext = text;
        return this;
    }
    public RecommendMusicBean setauthor(String author){
        Rauthor = author;
        return this;
    }
    public RecommendMusicBean setintroduce(String introduce){
        Rintroduce = introduce;
        return this;
    }
    public RecommendMusicBean setid(int id){
        Rid = id;
        return this;
    }
}
