package com.example.qrcreatorscanner;

public class imageupload {

    public String imageName;
    public String imageURL;

    imageupload(String name,String URL)
    {
        this.imageName = name;
        this.imageURL = URL;
    }

    public String getImageName() {
        return imageName;
    }
    public String getImageURL() {
        return imageURL;
    }

}
