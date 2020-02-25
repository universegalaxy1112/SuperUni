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

import com.uni.julio.supertv.BR;
import com.uni.julio.supertv.R;
import com.uni.julio.supertv.helper.TVRecyclerView;
import com.uni.julio.supertv.helper.TVRecyclerViewAdapter;
import com.uni.julio.supertv.listeners.LiveProgramSelectedListener;
import com.uni.julio.supertv.model.CastDevice;
import com.uni.julio.supertv.model.LiveProgram;
import com.uni.julio.supertv.viewmodel.MovieDetailsViewModelContract;

import java.util.List;

public class CastAdapter extends TVRecyclerViewAdapter<CastAdapter.MyViewHolder> {

    private Context mContext;
    private List<CastDevice> castDevices;
    private TVRecyclerView recyclerView;
    private MovieDetailsViewModelContract.View viewCallback;
    public CastAdapter(Context context, List<CastDevice> castDeviceList, TVRecyclerView recyclerView, MovieDetailsViewModelContract.View listener) {
        mContext=context;
        this.castDevices=castDeviceList;
        this.viewCallback=listener;
        this.recyclerView=recyclerView;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.castlistitem, viewGroup, false);

        return new MyViewHolder(mContext,convertView);
    }

    @Override
    protected void focusOut(View v, int position) {
    }
    @Override
    protected void focusIn(View v, int position) {
        this.recyclerView.scrollToPosition(position);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder,int position){
        CastDevice castDevice=castDevices.get(position);
        holder.getViewDataBinding().setVariable(com.uni.julio.supertv.BR.castItem,castDevice);
        holder.getViewDataBinding().getRoot().setTag(position);
        holder.getViewDataBinding().setVariable(com.uni.julio.supertv.BR.castAdapter, this);
        holder.getViewDataBinding().executePendingBindings();
    }

    @Override
    protected void onDataBinding(MyViewHolder holder, int position) {

    }
    public void onClickItem(View view) {

    }
    @Override
    public int getItemCount() {
        return castDevices.size();
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
