package com.uni.julio.supertvplus.helper;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import androidx.leanback.app.BackgroundManager;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

 public class PicassoBackgroundManagerTarget implements Target {
    BackgroundManager mBackgroundManager;

    public PicassoBackgroundManagerTarget(BackgroundManager backgroundManager) {
        this.mBackgroundManager = backgroundManager;
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
        this.mBackgroundManager.setBitmap(bitmap);
    }

     @Override
     public void onBitmapFailed(Exception e, Drawable errorDrawable) {
         this.mBackgroundManager.setDrawable(errorDrawable);

     }



    @Override
    public void onPrepareLoad(Drawable drawable) {
        // Do nothing, default_background manager has its own transitions
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PicassoBackgroundManagerTarget that = (PicassoBackgroundManagerTarget) o;

        return mBackgroundManager.equals(that.mBackgroundManager);
    }

    @Override
    public int hashCode() {
        return mBackgroundManager.hashCode();
    }
}
