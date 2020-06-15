package com.uni.julio.supertvplus.adapter;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.uni.julio.supertvplus.R;
import com.uni.julio.supertvplus.helper.TVRecyclerView;
import com.uni.julio.supertvplus.helper.TVRecyclerViewAdapter;
import com.uni.julio.supertvplus.listeners.MovieSelectedListener;
import com.uni.julio.supertvplus.model.Movie;
import com.uni.julio.supertvplus.model.VideoStream;
import com.uni.julio.supertvplus.utils.Device;

import java.util.List;

public class MoviesRecyclerAdapter extends TVRecyclerViewAdapter<MoviesRecyclerAdapter.MyViewHolder> {
    private List<?extends VideoStream> mMovies;
    private Context mContext;
    private int mRowPosition;
    private MovieSelectedListener movieSelectedListener;
    private TVRecyclerView recyclerView;
    MoviesRecyclerAdapter(Context context, TVRecyclerView recyclerView, List<? extends VideoStream> videoDataList, int rowPosition, MovieSelectedListener movieSelectedListener) {
        this.mMovies=videoDataList;
        this.mContext=context;
        this.mRowPosition=rowPosition;
        this.movieSelectedListener=movieSelectedListener;
        this.recyclerView=recyclerView;
        if(!Device.canTreatAsBox()) recyclerView.setIsAutoProcessFocus(false);

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View itemView= LayoutInflater.from(mContext).inflate(R.layout.video_list_row,viewGroup,false);
        return new MyViewHolder(mContext,itemView);
    }

    @Override
    protected void focusOut(View v, int position) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(v, "scaleX", 1.1f, 1.0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(v, "scaleY", 1.1f, 1.0f);
        AnimatorSet set = new AnimatorSet();
        set.play(scaleX).with(scaleY);

        set.start();
    }

    @Override
    protected void focusIn(View v, final int position) {
        recyclerView.scrollToPosition(position);

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(v, "scaleX", 1.0f, 1.1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(v, "scaleY", 1.0f, 1.1f);
        AnimatorSet set = new AnimatorSet();
        set.play(scaleX).with(scaleY);
        set.start();


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDataBinding(MyViewHolder holder,  int position) {
        Movie movie = (Movie) mMovies.get(position);
        holder.getViewDataBinding().setVariable(com.uni.julio.supertvplus.BR.moviesMenuItem, movie);
        holder.getViewDataBinding().getRoot().setTag(position);
        holder.getViewDataBinding().setVariable(com.uni.julio.supertvplus.BR.moviesAdapter,this);

    }

    public void onClickItem(View view) {
         movieSelectedListener.onMovieSelected(mMovies.get((int)view.getTag()));
    }
    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    class MyViewHolder extends TVRecyclerViewAdapter.ViewHolder{
        private ViewDataBinding viewDataBinding;
        MyViewHolder(Context context, View itemView){
            super(context,itemView);
           viewDataBinding= DataBindingUtil.bind(itemView);
            itemView.setBackground(mContext.getResources().getDrawable(R.drawable.movies_bg));

        }
        ViewDataBinding getViewDataBinding(){
            return viewDataBinding;
        }
    }
}
