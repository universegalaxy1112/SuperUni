package com.uni.julio.supertvplus.adapter;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.uni.julio.supertvplus.R;
import com.uni.julio.supertvplus.binding.BindingAdapters;
import com.uni.julio.supertvplus.helper.TVRecyclerView;
import com.uni.julio.supertvplus.helper.TVRecyclerViewAdapter;
import com.uni.julio.supertvplus.listeners.MovieSelectedListener;
import com.uni.julio.supertvplus.model.Movie;
import com.uni.julio.supertvplus.model.VideoStream;
import com.uni.julio.supertvplus.utils.Device;

import java.util.List;

public class GridViewAdapter extends TVRecyclerViewAdapter<GridViewAdapter.MyViewHolder> {
    private List<?extends VideoStream> mMovies;
    private Context mContext;
    private MovieSelectedListener movieSelectedListener;

    public GridViewAdapter(Context context, TVRecyclerView recyclerView, List<?extends VideoStream> videoDataList,  MovieSelectedListener movieSelectedListener) {
        this.mMovies=videoDataList;
        this.mContext=context;
        this.movieSelectedListener=movieSelectedListener;
        if(!Device.canTreatAsBox()) recyclerView.setIsAutoProcessFocus(false);
        else
        recyclerView.setOnItemStateListener(new TVRecyclerView.OnItemStateListener() {
            @Override
            public void onItemViewClick(View view, int position) {
                GridViewAdapter.this.movieSelectedListener.onMovieSelected(mMovies.get((int)view.getTag()));
            }

            @Override
            public void onItemViewFocusChanged(boolean gainFocus, View view, int position) {
            }
        });
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View itemView= LayoutInflater.from(mContext).inflate(R.layout.gridview_row,viewGroup,false);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager)(mContext.getSystemService(Context.WINDOW_SERVICE))).getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int px=(int)mContext.getResources().getDisplayMetrics().density*(int) mContext.getResources().getDimension(R.dimen.dip_4);
        int width=(screenWidth-16*Integer.parseInt(mContext.getString(R.string.more_video))-px)/Integer.parseInt(mContext.getString(R.string.more_video));
        ViewGroup.LayoutParams params= new ViewGroup.LayoutParams(width, (int) (1.5*width));
        itemView.setLayoutParams(params);
        return new MyViewHolder(mContext,itemView);
    }

    @Override
    protected void focusOut(View v, int position) {

    }
    @Override
    protected void focusIn(View v, int position) {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDataBinding(MyViewHolder holder,  int position) {
        Movie movie = (Movie) mMovies.get(position);

        holder.getViewDataBinding().setVariable(com.uni.julio.supertvplus.BR.moviesMenuItem, movie);
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
