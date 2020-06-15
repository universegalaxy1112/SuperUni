package com.uni.julio.supertvplus.adapter;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.uni.julio.supertvplus.BR;
import com.uni.julio.supertvplus.R;
import com.uni.julio.supertvplus.binding.BindingAdapters;
import com.uni.julio.supertvplus.helper.TVRecyclerView;
import com.uni.julio.supertvplus.listeners.MovieSelectedListener;
import com.uni.julio.supertvplus.model.Movie;
import com.uni.julio.supertvplus.model.VideoStream;
import com.uni.julio.supertvplus.utils.Device;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MultiSeasonAdapter extends RecyclerView.Adapter<MultiSeasonAdapter.MyViewHolder>{
    private List<?extends VideoStream> mMovies;
    private Context mContext;
    private MovieSelectedListener movieSelectedListener;
    private Map<Integer, Bitmap> loadedImages=new HashMap<>();
    private Handler handler=new Handler();
    private TVRecyclerView recyclerView;
    public MultiSeasonAdapter(Context context, TVRecyclerView recyclerView, List<?extends VideoStream> videoDataList, MovieSelectedListener movieSelectedListener) {
        this.mMovies=videoDataList;
        this.mContext=context;
        this.movieSelectedListener=movieSelectedListener;
        this.recyclerView=recyclerView;
        if(!Device.canTreatAsBox()) recyclerView.setIsAutoProcessFocus(false);
        this.recyclerView.setOnItemStateListener(new TVRecyclerView.OnItemStateListener() {
            @Override
            public void onItemViewClick(View view, int position) {
                MultiSeasonAdapter.this.movieSelectedListener.onMovieSelected(mMovies.get((int)view.getTag()));
            }

            @Override
            public void onItemViewFocusChanged(boolean gainFocus, View view, int position) {
                /*if(view == null) return;
                if(gainFocus) {
                    view.findViewById(R.id.fl_main_layout).setBackground(mContext.getResources().getDrawable(R.drawable.moviesborder));
                }else{
                    view.findViewById(R.id.fl_main_layout).setBackground(mContext.getResources().getDrawable(R.drawable.md_transparent));
                }*/
            }
        });
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View itemView= LayoutInflater.from(mContext).inflate(R.layout.multiseason_row,viewGroup,false);
        int orientation=mContext.getResources().getConfiguration().orientation;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager)(mContext.getSystemService(Context.WINDOW_SERVICE))).getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int px=(int)mContext.getResources().getDisplayMetrics().density*24;

        if(orientation == Configuration.ORIENTATION_PORTRAIT){
            int width=(screenWidth-32-px);
            ViewGroup.LayoutParams params= new ViewGroup.LayoutParams(width, (int) (0.9*width/3));
            itemView.setLayoutParams(params);
        }else{
            int width=(3*screenWidth/8-32-px);
            ViewGroup.LayoutParams params= new ViewGroup.LayoutParams(width, (int) (width/3));
            itemView.setLayoutParams(params);
        }

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Movie movie = (Movie) mMovies.get(position);
        holder.getViewDataBinding().setVariable(BR.moviesMenuItem, movie);
        holder.getViewDataBinding().getRoot().setTag(position);
        holder.getViewDataBinding().setVariable(com.uni.julio.supertvplus.BR.moviesAdapter,this);
        BindingAdapters.loadImage((ImageView)holder.getViewDataBinding().getRoot().findViewById(R.id.fl_main_layout).findViewById(R.id.img),movie.getHDPosterUrl());
        holder.getViewDataBinding().executePendingBindings();
    }

    public void updateMovies(List<? extends VideoStream> objects) {
        mMovies = objects;
        postAndNotifyAdapter();
    }

    public void onClickItem(View view) {
        MultiSeasonAdapter.this.movieSelectedListener.onMovieSelected(mMovies.get((int)view.getTag()));
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{
        private ViewDataBinding viewDataBinding;
        MyViewHolder(View itemView){
            super(itemView);
            viewDataBinding= DataBindingUtil.bind(itemView);
        }
        ViewDataBinding getViewDataBinding(){
            return viewDataBinding;
        }
    }
    private void postAndNotifyAdapter() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }
}
