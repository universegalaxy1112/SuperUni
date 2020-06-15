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

import com.uni.julio.supertvplus.BR;
import com.uni.julio.supertvplus.R;
import com.uni.julio.supertvplus.helper.TVRecyclerView;
import com.uni.julio.supertvplus.helper.TVRecyclerViewAdapter;
import com.uni.julio.supertvplus.listeners.LiveProgramSelectedListener;
import com.uni.julio.supertvplus.model.LiveProgram;
import com.uni.julio.supertvplus.utils.Device;

import java.util.List;

public class LivetvAdapterNew extends TVRecyclerViewAdapter<LivetvAdapterNew.MyViewHolder> {

    private Context mContext;
    private List<LiveProgram> livePrograms;
    private LiveProgramSelectedListener liveProgramSelectedListener;
    public LivetvAdapterNew(Context context,  List<LiveProgram> livePrograms,  TVRecyclerView recyclerView, LiveProgramSelectedListener liveProgramSelectedListener) {
        mContext=context;
        this.livePrograms=livePrograms;
        this.liveProgramSelectedListener=liveProgramSelectedListener;
        recyclerView.setSelectedScale(1.0f);
        if(!Device.canTreatAsBox()) recyclerView.setIsAutoProcessFocus(false);
        recyclerView.setLive(true);
        recyclerView.setOnItemStateListener(new TVRecyclerView.OnItemStateListener() {
            @Override
            public void onItemViewClick(View view, int position) {
                LivetvAdapterNew.this.liveProgramSelectedListener.onLiveProgramSelected(LivetvAdapterNew.this.livePrograms.get((Integer) view.getTag()), (Integer) view.getTag());
            }

            @Override
            public void onItemViewFocusChanged(boolean gainFocus, final View v, int position) {
                if(v == null || !Device.treatAsBox) return;
                if(gainFocus){
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            v.findViewById(R.id.channel_title).setBackground(mContext.getResources().getDrawable(R.drawable.background_program_item_focused));
                            v.findViewById(R.id.now_playing).setBackground(mContext.getResources().getDrawable(R.drawable.background_program_item_focused));
                            ((TextView)v.findViewById(R.id.now_playing_text)).setTextColor(mContext.getResources().getColor(R.color.white));
                            ((TextView)v.findViewById(R.id.channel_title_text)).setTextColor(mContext.getResources().getColor(R.color.white));
                        }
                    });

                }else{
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            v.findViewById(R.id.channel_title).setBackground(mContext.getResources().getDrawable(R.drawable.background_program_item));
                            v.findViewById(R.id.now_playing).setBackground(mContext.getResources().getDrawable(R.drawable.background_program_item));
                            ((TextView)v.findViewById(R.id.now_playing_text)).setTextColor(mContext.getResources().getColor(R.color.live_category_text));
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
               // recyclerView.setItemSelected(livePrograms.size()-1);
            }
        });
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.livetvnew_list, viewGroup, false);
        return new MyViewHolder(mContext, convertView);
    }


    public void updateChannels(List<LiveProgram> programs) {
        livePrograms = programs;
        postAndNotifyAdapter();
    }

    public void onClick(View view) {
        liveProgramSelectedListener.onLiveProgramSelected(livePrograms.get((Integer) view.getTag()), (Integer) view.getTag());
    }
    @Override
    public int getItemCount() {
        return livePrograms.size();
    }

    @Override
    protected void focusOut(final View v, int position) {

    }

    @Override
    protected void focusIn(final View v, int position) {

    }

    @Override
    protected void onDataBinding(MyViewHolder holder, int position) {
        LiveProgram liveProgram=livePrograms.get(position);
        holder.getViewDataBinding().setVariable(com.uni.julio.supertvplus.BR.liveProgramItem,liveProgram);
        holder.getViewDataBinding().setVariable(BR.livetvAdapter,this);
        holder.getViewDataBinding().getRoot().setTag(position);
        holder.getViewDataBinding().executePendingBindings();
    }

    class MyViewHolder extends TVRecyclerViewAdapter.ViewHolder{
        private ViewDataBinding viewDataBinding;
        public MyViewHolder(Context context, View itemView ){
            super(context, itemView);
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
