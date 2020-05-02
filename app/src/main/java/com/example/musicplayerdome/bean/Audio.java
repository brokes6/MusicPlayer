package com.example.musicplayerdome.bean;


import java.io.Serializable;


/**
 * Created by ding on 2016/10/5.
 * 音频实体类
 */
public class Audio implements Serializable {
    //id
    private long id;
    //类型
    private int type;
    //大小
    private int length;
    //时长
    private int time;
    //状态
    private int status;
    //名称
    private String name;
    private String bookName;
    //封面
    private String faceUrl;
    //文稿url
    private String textUrl;
    //标签一
    private String tag1;
    //标签二
    private String tag2;
    //标签三
    private String tag3;
    //作者
    private String author;
    //读者
    private String reader;
    //来源
    private String source;
    //简介
    private String abstractInfo;
    //副标题
    private String Subtitle;
    //文稿内容
    private String wengao;
    //播放地址
    private String fileUrl;
    //是否可播放
    private boolean lock;

    private String lrcurl;

    public String getSubtitle() {
        return Subtitle;
    }

    public void setSubtitle(String subtitle) {
        Subtitle = subtitle;
    }

    public String getLrcurl() {
        return lrcurl;
    }

    public void setLrcurl(String lrcurl) {
        this.lrcurl = lrcurl;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getWengao() {
        return wengao;
    }

    public void setWengao(String wengao) {
        this.wengao = wengao;
    }

    public String getAbstractInfo() {
        return abstractInfo;
    }

    public void setAbstractInfo(String abstractInfo) {
        this.abstractInfo = abstractInfo;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getReader() {
        return reader;
    }

    public void setReader(String reader) {
        this.reader = reader;
    }

    public String getSource() {
        return (source);
    }

    public void setSource(String source) {
        this.source = source;
    }


    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getName() {
        return (name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFaceUrl() {
        return faceUrl;
    }

    public void setFaceUrl(String faceUrl) {
        this.faceUrl = faceUrl;
    }

    public String getTextUrl() {
        return textUrl;
    }

    public void setTextUrl(String textUrl) {
        this.textUrl = textUrl;
    }

    public String getTag1() {
        return tag1;
    }

    public void setTag1(String tag1) {
        this.tag1 = tag1;
    }

    public String getTag2() {
        return tag2;
    }

    public void setTag2(String tag2) {
        this.tag2 = tag2;
    }

    public String getTag3() {
        return tag3;
    }

    public void setTag3(String tag3) {
        this.tag3 = tag3;
    }

    @Override
    public String toString() {
        return "Audio{" +
                "type=" + type +
                ", id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public boolean isLock() {
        return lock;
    }

    public String getArtist() {
        return "";
    }

    public String getAlbum() {
        return "";
    }
    public Audio setUrl(String url){
        fileUrl = url;
        return this;
    }
    public Audio setimgUrl(String url){
        faceUrl = url;
        return this;
    }
    public Audio setText(String text){
        name = text;
        return this;
    }
    public Audio setauthor(String author){
        this.author = author;
        return this;
    }
    public Audio setintroduce(String introduce){
        abstractInfo = introduce;
        return this;
    }
    public Audio setid(int id){
        this.id = id;
        return this;
    }
}
