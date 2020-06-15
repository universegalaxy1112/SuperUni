package com.uni.julio.supertvplus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.leanback.widget.Presenter;

import com.uni.julio.supertvplus.R;
import com.uni.julio.supertvplus.model.Movie;


public class MoviesPresenter extends Presenter {
    private Context mContext;

    public MoviesPresenter(Context context) {
        this.mContext = context;
    }
     @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new MoviesPresenterViewHolder(((LayoutInflater) this.mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.video_list_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, Object item) {
        Movie movie = (Movie) item;
        ((MoviesPresenterViewHolder)holder).getViewBinding().setVariable(com.uni.julio.supertvplus.BR.moviesMenuItem,movie);
        ((MoviesPresenterViewHolder) holder).getViewBinding().executePendingBindings();

    }
    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
    }


}
