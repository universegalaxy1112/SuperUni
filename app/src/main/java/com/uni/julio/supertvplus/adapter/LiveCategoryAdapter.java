package com.uni.julio.supertvplus.adapter;


import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.uni.julio.supertvplus.R;
import com.uni.julio.supertvplus.helper.TVRecyclerView;
import com.uni.julio.supertvplus.helper.TVRecyclerViewAdapter;
import com.uni.julio.supertvplus.listeners.LiveTVCategorySelectedListener;
import com.uni.julio.supertvplus.model.LiveTVCategory;
import com.uni.julio.supertvplus.utils.Device;

import java.util.List;


public class LiveCategoryAdapter extends TVRecyclerViewAdapter<LiveCategoryAdapter.MyViewHolder> {

    private Context mContext;
    private List<LiveTVCategory> categories;
    private LiveTVCategorySelectedListener liveTVCategorySelectedListener;
    public LiveCategoryAdapter(Context context, List<LiveTVCategory> categories, TVRecyclerView recyclerView, LiveTVCategorySelectedListener liveTVCategorySelectedListener) {
        mContext=context;
        this.categories=categories;
        this.liveTVCategorySelectedListener=liveTVCategorySelectedListener;
        if(!Device.canTreatAsBox()) recyclerView.setIsAutoProcessFocus(false);
        recyclerView.setSelectedScale(1.0f);
        recyclerView.setLive(true);
        recyclerView.setOnItemStateListener(new TVRecyclerView.OnItemStateListener() {
            @Override
            public void onItemViewClick(View view, int position) {
                LiveCategoryAdapter.this.liveTVCategorySelectedListener.onLiveTVCategorySelected(LiveCategoryAdapter.this.categories.get((Integer)view.getTag()));
            }


            @Override
            public void onItemViewFocusChanged(boolean gainFocus, final View v, int position) {
                if(v == null || !Device.treatAsBox) return;
                if(gainFocus){
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            v.findViewById(R.id.channel_title).setBackground(mContext.getResources().getDrawable(R.drawable.background_program_item_focused));
                            v.findViewById(R.id.total_channel).setBackground(mContext.getResources().getDrawable(R.drawable.background_program_item_focused));
                            ((TextView)v.findViewById(R.id.total_channel_text)).setTextColor(mContext.getResources().getColor(R.color.white));
                            ((TextView)v.findViewById(R.id.channel_title_text)).setTextColor(mContext.getResources().getColor(R.color.white));
                        }
                    });
                }else{
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            v.findViewById(R.id.channel_title).setBackground(mContext.getResources().getDrawable(R.drawable.background_program_item));
                            v.findViewById(R.id.total_channel).setBackground(mContext.getResources().getDrawable(R.drawable.background_program_item));
                            ((TextView)v.findViewById(R.id.total_channel_text)).setTextColor(mContext.getResources().getColor(R.color.live_category_text));
                            ((TextView)v.findViewById(R.id.channel_title_text)).setTextColor(mContext.getResources().getColor(R.color.live_category_text));
                        }
                    });
                }
            }
        });
        recyclerView.setOnScrollStateListener(new TVRecyclerView.onScrollStateListener() {
            @Override
            public void onScrollEnd(View view) {
                //recyclerView.setItemSelected(0);
            }

            @Override
            public void onScrollStart(View view) {
                //recyclerView.scrollToPosition(categories.size()-1);
            }
        });
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.live_category_list, viewGroup, false);
        return new MyViewHolder(mContext,convertView);
    }


    @Override
    protected void focusOut(View v, int position) {

    }
    @Override
    protected void focusIn(View v, int position) {

    }

    @Override
    protected void onDataBinding(MyViewHolder holder, int position) {
        LiveTVCategory category=this.categories.get(position);
        holder.getViewDataBinding().setVariable(com.uni.julio.supertvplus.BR.liveCategory, category);
        holder.getViewDataBinding().getRoot().setTag(position);
        holder.getViewDataBinding().getRoot().findViewById(R.id.fl_main_layout).setTag(position);
        holder.getViewDataBinding().setVariable(com.uni.julio.supertvplus.BR.liveCategoryAdapter, this);
        holder.getViewDataBinding().executePendingBindings();
    }

    public void onClickItem(View view) {
        liveTVCategorySelectedListener.onLiveTVCategorySelected(categories.get((Integer)view.getTag()));
    }

    @Override
    public int getItemCount() {
        return categories.size();
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

}
