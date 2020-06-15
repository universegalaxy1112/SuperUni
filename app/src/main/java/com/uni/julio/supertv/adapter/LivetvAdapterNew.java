package com.uni.julio.supertv.adapter;

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
import androidx.recyclerview.widget.RecyclerView;

import com.uni.julio.supertv.R;
import com.uni.julio.supertv.helper.TVRecyclerView;
import com.uni.julio.supertv.listeners.LiveProgramSelectedListener;
import com.uni.julio.supertv.model.LiveProgram;
import com.uni.julio.supertv.utils.Device;

import java.util.List;

public class LivetvAdapterNew extends RecyclerView.Adapter<LivetvAdapterNew.MyViewHolder> {

    private Context mContext;
    private List<LiveProgram> livePrograms;
    private LiveProgramSelectedListener liveProgramSelectedListener;
    public LivetvAdapterNew(Context context,  List<LiveProgram> livePrograms,  TVRecyclerView recyclerView, LiveProgramSelectedListener liveProgramSelectedListener) {
        mContext=context;
        this.livePrograms=livePrograms;
        this.liveProgramSelectedListener=liveProgramSelectedListener;
        recyclerView.setSelectedScale(1.0f);
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
        return new MyViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        LiveProgram liveProgram=livePrograms.get(position);
        holder.getViewDataBinding().setVariable(com.uni.julio.supertv.BR.liveProgramItem,liveProgram);
        holder.getViewDataBinding().getRoot().setTag(position);
        holder.getViewDataBinding().executePendingBindings();
    }

    public void updateChannels(List<LiveProgram> programs) {
        livePrograms = programs;
        postAndNotifyAdapter();
    }

    @Override
    public int getItemCount() {
        return livePrograms.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder{
        private ViewDataBinding viewDataBinding;
        MyViewHolder(View itemView){
            super(itemView);
            viewDataBinding= DataBindingUtil.bind(itemView);
            itemView.findViewById(R.id.fl_main_layout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    liveProgramSelectedListener.onLiveProgramSelected(livePrograms.get((Integer) view.getTag()), (Integer) view.getTag());
                }
            });

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
