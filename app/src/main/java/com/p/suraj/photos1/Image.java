package com.p.suraj.photos1;

import android.graphics.Bitmap;

/**
 * Created by Suraj on 9/6/2017.
 */

public class Image {

    private Bitmap image;
    private String keyj;

    public String getKeyj() {
        return keyj;
    }

    public void setKeyj(String keyj) {
        this.keyj = keyj;
    }

    public Image(Bitmap image, String keyj) {
        this.image = image;
        this.keyj = keyj;
    }

    public Image(Bitmap image) {
        this.image = image;
    }

    public Image() {
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
