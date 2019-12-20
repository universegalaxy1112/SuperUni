package com.uni.julio.supertv.helper;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


public class TVRecyclerView extends RecyclerView {
    private static final String TAG = "TVRecyclerView";

    public TVRecyclerView(Context context) {
        this(context, null);
    }
    public TVRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public TVRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
