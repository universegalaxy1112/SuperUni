package com.uni.julio.supertvplus.helper;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewItemDecoration extends RecyclerView.ItemDecoration {
    public int top;
    public int right;
    public int bottom;
    public int left;
    public RecyclerViewItemDecoration(int top,int right,int bottom,int left) {
        this.top = top;
        this.right=right;
        this.bottom=bottom;
        this.left=left;
    }
    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = left;
        outRect.right =right;
        outRect.bottom = bottom;
        outRect.top = top;
    }
}