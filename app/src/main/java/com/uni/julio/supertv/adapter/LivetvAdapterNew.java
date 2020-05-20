package com.uni.julio.supertv.adapter;


import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
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
import com.uni.julio.supertv.listeners.LiveProgramSelectedListener;
import com.uni.julio.supertv.model.LiveProgram;

import java.util.List;

public class LivetvAdapterNew extends TVRecyclerViewAdapter<LivetvAdapterNew.MyViewHolder> {

    private Context mContext;
    private List<LiveProgram> livePrograms;
    private TVRecyclerView recyclerView;
    private LiveProgramSelectedListener liveProgramSelectedListener;
    private View currentView = null;
    public LivetvAdapterNew(Context context, List<LiveProgram> livePrograms, TVRecyclerView recyclerView, LiveProgramSelectedListener liveProgramSelectedListener) {
        mContext=context;
        this.livePrograms=livePrograms;
        this.liveProgramSelectedListener=liveProgramSelectedListener;
        this.recyclerView = recyclerView;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.livetvnew_list, viewGroup, false);
        return new MyViewHolder(mContext,convertView);
    }


    @Override
    protected void focusOut(View v, int position) {
        if(currentView == null || ((Integer) currentView.getTag() != position && recyclerView.getFocusedChild() != null)){
            v.findViewById(R.id.channel_title).setBackground(mContext.getResources().getDrawable(R.drawable.background_program_item));
            v.findViewById(R.id.now_playing).setBackground(mContext.getResources().getDrawable(R.drawable.background_program_item));
            ((TextView)v.findViewById(R.id.now_playing_text)).setTextColor(mContext.getResources().getColor(R.color.live_category_text));
            ((TextView)v.findViewById(R.id.channel_title_text)).setTextColor(mContext.getResources().getColor(R.color.live_category_text));
        }

    }
    @Override
    protected void focusIn(View v, int position) {
        v.findViewById(R.id.channel_title).setBackground(mContext.getResources().getDrawable(R.drawable.background_program_item_focused));
        v.findViewById(R.id.now_playing).setBackground(mContext.getResources().getDrawable(R.drawable.background_program_item_focused));
        ((TextView)v.findViewById(R.id.now_playing_text)).setTextColor(mContext.getResources().getColor(R.color.white));
        ((TextView)v.findViewById(R.id.channel_title_text)).setTextColor(mContext.getResources().getColor(R.color.white));
    }

    public void updateChannels(List<LiveProgram> programs) {
        livePrograms = programs;
        notifyDataSetChanged();
        recyclerView.requestFocus();
    }

    @Override
    protected void onDataBinding(MyViewHolder holder, int position) {
        LiveProgram liveProgram=livePrograms.get(position);
        holder.getViewDataBinding().setVariable(com.uni.julio.supertv.BR.liveProgramItem,liveProgram);
        holder.getViewDataBinding().getRoot().setTag(position);
        holder.getViewDataBinding().getRoot().findViewById(R.id.fl_main_layout).setTag(holder.getAdapterPosition());
        holder.getViewDataBinding().setVariable(com.uni.julio.supertv.BR.liveAdapternew, this);
        holder.getViewDataBinding().executePendingBindings();
    }

    public void onClickItem(View view) {
        /*view.findViewById(R.id.channel_title).setBackground(mContext.getResources().getDrawable(R.drawable.background_program_item_focused));
        view.findViewById(R.id.now_playing).setBackground(mContext.getResources().getDrawable(R.drawable.background_program_item_focused));
        ((TextView)view.findViewById(R.id.now_playing_text)).setTextColor(mContext.getResources().getColor(R.color.white));
        ((TextView)view.findViewById(R.id.channel_title_text)).setTextColor(mContext.getResources().getColor(R.color.white));*/
        liveProgramSelectedListener.onLiveProgramSelected(livePrograms.get((Integer) view.getTag()), (Integer) view.getTag());
    }

    @Override
    public int getItemCount() {
        return livePrograms.size();
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
