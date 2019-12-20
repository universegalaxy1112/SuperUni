package com.uni.julio.supertv.adapter;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.uni.julio.supertv.R;
import com.uni.julio.supertv.helper.TVRecyclerViewAdapter;
import com.uni.julio.supertv.listeners.LiveProgramSelectedListener;
import com.uni.julio.supertv.model.LiveProgram;

import java.util.List;

public class LivetvAdapter extends TVRecyclerViewAdapter<LivetvAdapter.MyViewHolder> {

private Context mContext;
private List<LiveProgram> livePrograms;
private LiveProgramSelectedListener liveProgramSelectedListener;
    public LivetvAdapter(Context context, List<LiveProgram> livePrograms, LiveProgramSelectedListener liveProgramSelectedListener) {
        mContext=context;
        this.livePrograms=livePrograms;
        this.liveProgramSelectedListener=liveProgramSelectedListener;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.livetv_list, viewGroup, false);

        return new MyViewHolder(mContext,convertView);
    }

    @Override
    protected void focusOut(View v, int position) {

    }
    @Override
    protected void focusIn(View v, int position) {


    }
    @Override
    public void onBindViewHolder(MyViewHolder holder,int position){
        LiveProgram liveProgram=livePrograms.get(position);
        holder.getViewDataBinding().setVariable(com.uni.julio.supertv.BR.liveProgramItem,liveProgram);
        holder.getViewDataBinding().getRoot().setTag(position);
        holder.getViewDataBinding().getRoot().findViewById(R.id.fl_main_layout).setTag(position);
        holder.getViewDataBinding().setVariable(com.uni.julio.supertv.BR.liveTVAdapter, this);
        holder.getViewDataBinding().executePendingBindings();

    }
    public void updateChannels(List<LiveProgram> programs) {
        livePrograms = programs;
        notifyDataSetChanged();
    }
    @Override
    protected void onDataBinding(MyViewHolder holder, int position) {

    }
    public void onClickItem(View view) {
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
            itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        ObjectAnimator scaleX = ObjectAnimator.ofFloat(v, "scaleX", 1.0f, 1.2f);
                        ObjectAnimator scaleY = ObjectAnimator.ofFloat(v, "scaleY", 1.0f, 1.2f);
                        AnimatorSet set = new AnimatorSet();
                        set.play(scaleX).with(scaleY);
                        set.start();

                     } else {
                        ObjectAnimator scaleX = ObjectAnimator.ofFloat(v, "scaleX", 1.2f, 1.0f);
                        ObjectAnimator scaleY = ObjectAnimator.ofFloat(v, "scaleY", 1.2f, 1.0f);

                        AnimatorSet set = new AnimatorSet();
                        set.play(scaleX).with(scaleY);
                        set.start();
                    }
                }
            });
        }
        public ViewDataBinding getViewDataBinding(){
            return viewDataBinding;
        }
    }

}
