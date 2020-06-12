package com.example.musicplayerdome.search.bean;

import java.util.List;

public class LikeVideoBean {
    private int code;
    private List<dataData> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<dataData> getData() {
        return data;
    }

    public void setData(List<dataData> data) {
        this.data = data;
    }

    private class dataData{
        private String villageBirthElement;

        public String getVillageBirthElement() {
            return villageBirthElement;
        }

        public void setVillageBirthElement(String villageBirthElement) {
            this.villageBirthElement = villageBirthElement;
        }
    }
}
