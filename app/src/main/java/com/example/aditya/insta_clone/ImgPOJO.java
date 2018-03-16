package com.example.aditya.insta_clone;

import android.graphics.Bitmap;

import com.parse.ParseFile;

/**
 * Created by Aditya on 2017-12-30.
 */

public class ImgPOJO {
    private Bitmap Image;
    public ImgPOJO(Bitmap Image){
        this.Image=Image;
    }
    public Bitmap getImage(){return Image;}
    public void setImage(Bitmap Image){this.Image=Image;};
}
