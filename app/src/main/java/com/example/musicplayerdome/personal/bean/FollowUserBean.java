package com.example.musicplayerdome.personal.bean;

public class FollowUserBean {
    private String py;
    private String followTimeContent;
    private String followContent;
    private int code;
    private userData user;

    public String getPy() {
        return py;
    }

    public void setPy(String py) {
        this.py = py;
    }

    public String getFollowTimeContent() {
        return followTimeContent;
    }

    public void setFollowTimeContent(String followTimeContent) {
        this.followTimeContent = followTimeContent;
    }

    public String getFollowContent() {
        return followContent;
    }

    public void setFollowContent(String followContent) {
        this.followContent = followContent;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public userData getUser() {
        return user;
    }

    public void setUser(userData user) {
        this.user = user;
    }

    public class userData{
        private long avatarImgIdStr;
        private long backgroundImgIdStr;
        private long userId;
        private int gender;
        private String backgroundUrl;
        private String signature;
        private int vipType;
        private Boolean followed;

        public long getAvatarImgIdStr() {
            return avatarImgIdStr;
        }

        public void setAvatarImgIdStr(long avatarImgIdStr) {
            this.avatarImgIdStr = avatarImgIdStr;
        }

        public long getBackgroundImgIdStr() {
            return backgroundImgIdStr;
        }

        public void setBackgroundImgIdStr(long backgroundImgIdStr) {
            this.backgroundImgIdStr = backgroundImgIdStr;
        }

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public String getBackgroundUrl() {
            return backgroundUrl;
        }

        public void setBackgroundUrl(String backgroundUrl) {
            this.backgroundUrl = backgroundUrl;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public int getVipType() {
            return vipType;
        }

        public void setVipType(int vipType) {
            this.vipType = vipType;
        }

        public Boolean getFollowed() {
            return followed;
        }

        public void setFollowed(Boolean followed) {
            this.followed = followed;
        }
    }
}
