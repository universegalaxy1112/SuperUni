package com.uni.julio.supertvplus.model;

import android.graphics.Bitmap;

public class ImageResponse {

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    private int position;
    private Bitmap bitmap;


}
