package yuncun.bean;


import java.util.List;

public class YuncunReviewBean {

    private int code;
    private String message;
    public List<UserData> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<UserData> getData() {
        return data;
    }

    public void setData(List<UserData> data) {
        this.data = data;
    }

    public class UserData{
        private long id;
        private String threadId;
        private String content;
        private long time;
        private simpleUserInfoBean simpleUserInfo;
        private simpleResourceInfoBean simpleResourceInfo;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getThreadId() {
            return threadId;
        }

        public void setThreadId(String threadId) {
            this.threadId = threadId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public simpleUserInfoBean getSimpleUserInfo() {
            return simpleUserInfo;
        }

        public void setSimpleUserInfo(simpleUserInfoBean simpleUserInfo) {
            this.simpleUserInfo = simpleUserInfo;
        }

        public simpleResourceInfoBean getSimpleResourceInfo() {
            return simpleResourceInfo;
        }

        public void setSimpleResourceInfo(simpleResourceInfoBean simpleResourceInfo) {
            this.simpleResourceInfo = simpleResourceInfo;
        }

        public class simpleUserInfoBean{
            private long userId;
            private String nickname;
            private String avatar;
            private boolean followed;
            private int userType;

            public long getUserId() {
                return userId;
            }

            public void setUserId(long userId) {
                this.userId = userId;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public boolean isFollowed() {
                return followed;
            }

            public void setFollowed(boolean followed) {
                this.followed = followed;
            }

            public int getUserType() {
                return userType;
            }

            public void setUserType(int userType) {
                this.userType = userType;
            }
        }
        public class simpleResourceInfoBean{
            private long songId;
            private String threadId;
            private String name;
            private List<artistsBean> artists;
            private String songCoverUrl;
            private songBean song;

            public long getSongId() {
                return songId;
            }

            public void setSongId(long songId) {
                this.songId = songId;
            }

            public String getThreadId() {
                return threadId;
            }

            public void setThreadId(String threadId) {
                this.threadId = threadId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getSongCoverUrl() {
                return songCoverUrl;
            }

            public void setSongCoverUrl(String songCoverUrl) {
                this.songCoverUrl = songCoverUrl;
            }

            public List<artistsBean> getArtists() {
                return artists;
            }

            public void setArtists(List<artistsBean> artists) {
                this.artists = artists;
            }

            public songBean getSong() {
                return song;
            }

            public void setSong(songBean song) {
                this.song = song;
            }

            public class artistsBean{
                private long id;
                private String name;

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
            }

            public class songBean{
                private String name;
                private long id;
                private long dt;
                private List<ar> ar;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public long getId() {
                    return id;
                }

                public void setId(long id) {
                    this.id = id;
                }

                public long getDt() {
                    return dt;
                }

                public void setDt(long dt) {
                    this.dt = dt;
                }

                public List<songBean.ar> getAr() {
                    return ar;
                }

                public void setAr(List<songBean.ar> ar) {
                    this.ar = ar;
                }

                private class ar{
                    private long id;
                    private String name;

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
                }
            }
        }
    }
}
