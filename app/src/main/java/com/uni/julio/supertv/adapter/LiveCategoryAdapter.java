package com.uni.julio.supertv.adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.uni.julio.supertv.R;
import com.uni.julio.supertv.helper.TVRecyclerView;
import com.uni.julio.supertv.helper.TVRecyclerViewAdapter;
import com.uni.julio.supertv.listeners.LiveTVCategorySelectedListener;
import com.uni.julio.supertv.model.LiveTVCategory;

import java.util.List;

public class LiveCategoryAdapter extends TVRecyclerViewAdapter<LiveCategoryAdapter.MyViewHolder> {

    private Context mContext;
    private List<LiveTVCategory> categories;
    private TVRecyclerView recyclerView;
    private LiveTVCategorySelectedListener liveTVCategorySelectedListener;
    private View currentView = null;
    public LiveCategoryAdapter(Context context, List<LiveTVCategory> categories, TVRecyclerView recyclerView, LiveTVCategorySelectedListener liveTVCategorySelectedListener) {
        mContext=context;
        this.categories=categories;
        this.liveTVCategorySelectedListener=liveTVCategorySelectedListener;
        this.recyclerView = recyclerView;
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
            v.findViewById(R.id.channel_title).setBackground(mContext.getResources().getDrawable(R.drawable.background_program_item));
            v.findViewById(R.id.total_channel).setBackground(mContext.getResources().getDrawable(R.drawable.background_program_item));
            ((TextView)v.findViewById(R.id.total_channel_text)).setTextColor(mContext.getResources().getColor(R.color.live_category_text));
            ((TextView)v.findViewById(R.id.channel_title_text)).setTextColor(mContext.getResources().getColor(R.color.live_category_text));
    }
    @Override
    protected void focusIn(View v, int position) {
        v.findViewById(R.id.channel_title).setBackground(mContext.getResources().getDrawable(R.drawable.background_program_item_focused));
        v.findViewById(R.id.total_channel).setBackground(mContext.getResources().getDrawable(R.drawable.background_program_item_focused));
        ((TextView)v.findViewById(R.id.total_channel_text)).setTextColor(mContext.getResources().getColor(R.color.white));
        ((TextView)v.findViewById(R.id.channel_title_text)).setTextColor(mContext.getResources().getColor(R.color.white));
    }

    @Override
    protected void onDataBinding(MyViewHolder holder, int position) {
        LiveTVCategory category=this.categories.get(position);
        holder.getViewDataBinding().setVariable(com.uni.julio.supertv.BR.liveCategory, category);
        holder.getViewDataBinding().getRoot().setTag(position);
        holder.getViewDataBinding().getRoot().findViewById(R.id.fl_main_layout).setTag(position);
        holder.getViewDataBinding().setVariable(com.uni.julio.supertv.BR.liveCategoryAdapter, this);
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
        public MyViewHolder(Context context,View itemView){
            super(context,itemView);
            viewDataBinding= DataBindingUtil.bind(itemView);
        }
        ViewDataBinding getViewDataBinding(){
            return viewDataBinding;
        }
    }

}
