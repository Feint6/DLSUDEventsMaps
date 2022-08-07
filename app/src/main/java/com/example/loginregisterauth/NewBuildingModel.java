package com.example.loginregisterauth;

import com.google.firebase.database.Exclude;

public class NewBuildingModel {

    private String bName;
    private String cDesc;
    private String aImageUrl;
    private String mKey;
    private int position;

    public NewBuildingModel() {
        //empty constructor needed
    }

    public NewBuildingModel(String name, String desc, String imageUrl) {
        if (name.trim().equals("")) {
            name = "No Name";
        }

        bName = name;
        cDesc = desc;
        aImageUrl = imageUrl;
    }

    public String getbName() {
        return bName;
    }

    public void setbName(String bName) {
        this.bName = bName;
    }

    public String getcDesc() {
        return cDesc;
    }

    public void setcDesc(String cDesc) {
        this.cDesc = cDesc;
    }

    public String getaImageUrl() {
        return aImageUrl;
    }

    public void setaImageUrl(String aImageUrl) {
        this.aImageUrl = aImageUrl;
    }

    @Exclude
    public String getKey() {
        return mKey;
    }

    @Exclude
    public void setKey(String key) {
        mKey = key;
    }
}
