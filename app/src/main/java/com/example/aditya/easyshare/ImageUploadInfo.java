package com.example.aditya.easyshare;

/**
 * Created by aditya on 1/1/18.
 */

public class ImageUploadInfo {

    public String imageName;
    public String imageURL;

    public ImageUploadInfo() {

    }

    public ImageUploadInfo(String name, String url) {
        this.imageName = name;
        this.imageURL = url;
    }

    public String getImageName() {
        return imageName;
    }

    public String getImageURL() {
        return imageURL;
    }
}
