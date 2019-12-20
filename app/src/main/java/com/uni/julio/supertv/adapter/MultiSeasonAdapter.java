package com.uni.julio.supertv.adapter;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.uni.julio.supertv.R;
import com.uni.julio.supertv.binding.BindingAdapters;
import com.uni.julio.supertv.helper.TVRecyclerView;
import com.uni.julio.supertv.helper.TVRecyclerViewAdapter;
import com.uni.julio.supertv.listeners.ImageLoadedListener;
import com.uni.julio.supertv.listeners.MovieAcceptedListener;
import com.uni.julio.supertv.listeners.MovieSelectedListener;
import com.uni.julio.supertv.model.ImageResponse;
import com.uni.julio.supertv.model.Movie;
import com.uni.julio.supertv.model.VideoStream;
import com.uni.julio.supertv.utils.Files;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiSeasonAdapter extends TVRecyclerViewAdapter<MultiSeasonAdapter.MyViewHolder> implements ImageLoadedListener {
    List<?extends VideoStream> mMovies;
    Context mContext;
    private int mRowPosition;
    private MovieSelectedListener movieSelectedListener;
    private MovieAcceptedListener movieAcceptedListener;
    private boolean mShowTitle=false;
    private boolean mTreatAsBox=false;
    private Map<Integer, Bitmap> loadedImages=new HashMap<>();
    private Handler handler=new Handler();
    private TVRecyclerView recyclerView;
    private File directory;
    public MultiSeasonAdapter(Context context, TVRecyclerView recyclerView, List<?extends VideoStream> videoDataList, int rowPosition, MovieSelectedListener movieSelectedListener) {
        this.mMovies=videoDataList;
        this.mContext=context;
        this.mRowPosition=rowPosition;
        this.movieSelectedListener=movieSelectedListener;
        this.recyclerView=recyclerView;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View itemView= LayoutInflater.from(mContext).inflate(R.layout.multiseason_row,viewGroup,false);
        directory = Files.GetFile(Files.GetCacheDir());
        if(directory != null && !directory.exists()) {
            directory.mkdirs();
        }
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
    protected void focusIn(View v, int position) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(v, "scaleX", 1.0f, 1.1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(v, "scaleY", 1.0f, 1.1f);
        AnimatorSet set = new AnimatorSet();
        set.play(scaleX).with(scaleY);
        set.start();
    }



    public void setTreatAsBox(boolean treatAsBox) {
        mTreatAsBox = treatAsBox;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDataBinding(MyViewHolder holder,  int position) {
        Movie movie = (Movie) mMovies.get(position);

        holder.getViewDataBinding().setVariable(com.uni.julio.supertv.BR.moviesMenuItem, movie);
        holder.getViewDataBinding().getRoot().setTag(new int[]{mRowPosition,position});
        holder.getViewDataBinding().setVariable(com.uni.julio.supertv.BR.moviesAdapter,this);
        if(movie.getHDPosterUrl().equals("lupita")) {
            ((ImageView) holder.getViewDataBinding().getRoot().findViewById(R.id.fl_main_layout).findViewById(R.id.img)).setImageResource(R.drawable.ic_search_black_24dp);
        }
        else{
            if (true) {
                BindingAdapters.loadImage((ImageView)holder.getViewDataBinding().getRoot().findViewById(R.id.fl_main_layout).findViewById(R.id.img),movie.getHDPosterUrl());
                return;
            }
            holder.getViewDataBinding().executePendingBindings();

        }
    }
    public void updateMovies(List<? extends VideoStream> objects) {
        mMovies = objects;
    }
    public void onClickItem(View view) {
        int rowPosition = ((int[]) view.getTag())[0];
        int itemPosition = ((int[]) view.getTag())[1];
        movieSelectedListener.onMovieSelected(rowPosition, itemPosition);
    }
    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    @Override
    public void onLoaded(ImageResponse response) {


    }



    class MyViewHolder extends TVRecyclerViewAdapter.ViewHolder{
        private ViewDataBinding viewDataBinding;
        public MyViewHolder(Context context,View itemView){
            super(context,itemView);
            viewDataBinding= DataBindingUtil.bind(itemView);
            itemView.setBackground(mContext.getResources().getDrawable(R.drawable.movies_bg));

        }
        public ViewDataBinding getViewDataBinding(){
            return viewDataBinding;
        }
    }
    protected void postAndNotifyAdapter(final Handler handler, final TVRecyclerView.Adapter adapter, final ImageResponse response) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (!recyclerView.isComputingLayout()) {
                    notifyItemChanged(response.getPosition());
                } else {
                    postAndNotifyAdapter(handler, adapter, response);
                }
            }
        });
    }
}
