package com.example.musicplayerdome.main.bean;

import java.util.List;

public class RecommendsongBean {
    private int code;
    private int category;
    private List<resultData> result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public List<resultData> getResult() {
        return result;
    }

    public void setResult(List<resultData> result) {
        this.result = result;
    }

    public class resultData{
        private long id;
        private int type;
        private String name;
        private String picUrl;
        private boolean canDislike;
        private String alg;
        private songData song;

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public boolean isCanDislike() {
            return canDislike;
        }

        public songData getSong() {
            return song;
        }

        public void setSong(songData song) {
            this.song = song;
        }

        public void setCanDislike(boolean canDislike) {
            this.canDislike = canDislike;
        }

        public String getAlg() {
            return alg;
        }

        public void setAlg(String alg) {
            this.alg = alg;
        }

        public class songData{
            private String name;
            private long id;
            private int position;
            private List<artistsData>artists;
            private long duration;

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

            public int getPosition() {
                return position;
            }

            public void setPosition(int position) {
                this.position = position;
            }

            public List<artistsData> getArtists() {
                return artists;
            }

            public void setArtists(List<artistsData> artists) {
                this.artists = artists;
            }

            public long getDuration() {
                return duration;
            }

            public void setDuration(long duration) {
                this.duration = duration;
            }

            public class artistsData{
                private String name;
                private long id;
                private String picUrl;

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

                public String getPicUrl() {
                    return picUrl;
                }

                public void setPicUrl(String picUrl) {
                    this.picUrl = picUrl;
                }
            }
        }
    }
}
