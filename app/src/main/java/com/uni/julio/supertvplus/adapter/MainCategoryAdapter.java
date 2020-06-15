package com.uni.julio.supertvplus.adapter;


import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uni.julio.supertvplus.R;
import com.uni.julio.supertvplus.helper.TVRecyclerView;
import com.uni.julio.supertvplus.helper.VideoStreamManager;
import com.uni.julio.supertvplus.model.MainCategory;
import com.uni.julio.supertvplus.utils.Device;
import com.uni.julio.supertvplus.viewmodel.MainCategoriesMenuViewModelContract;

import java.util.List;

public class MainCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private MainCategoriesMenuViewModelContract.View viewCallback;
    private Context mContext;

    public MainCategoryAdapter(Context context, TVRecyclerView recyclerView,   MainCategoriesMenuViewModelContract.View viewCallback){
        this.viewCallback=viewCallback;
        if(!Device.canTreatAsBox()) recyclerView.setIsAutoProcessFocus(false);
        recyclerView.setOnItemStateListener(new TVRecyclerView.OnItemStateListener() {
            @Override
            public void onItemViewClick(View view, int position) {
                MainCategoryAdapter.this.viewCallback.onMainCategorySelected(VideoStreamManager.getInstance().getMainCategory((int)view.getTag()));
            }

            @Override
            public void onItemViewFocusChanged(boolean gainFocus, View view, int position) {
                /*if(view == null || !Device.treatAsBox) return;

                if(gainFocus)
                    view.findViewById(R.id.parent).setBackground(mContext.getResources().getDrawable(R.drawable.maincategory_border));
                else
                    view.findViewById(R.id.parent).setBackground(mContext.getResources().getDrawable(R.drawable.md_transparent));*/
            }
        });
        mContext=context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View itemView= LayoutInflater.from(mContext).inflate(R.layout.maincategory_list,viewGroup,false);
        int orientation=mContext.getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_PORTRAIT){
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((WindowManager)(mContext.getSystemService(Context.WINDOW_SERVICE))).getDefaultDisplay().getMetrics(displayMetrics);
            int screenWidth = displayMetrics.widthPixels;
            int px=(int)mContext.getResources().getDisplayMetrics().density*50;
            int width=(screenWidth-2*mContext.getResources().getInteger(R.integer.main_padding)*Integer.parseInt(mContext.getString(R.string.maincategory_column_num))-px)/Integer.parseInt(mContext.getString(R.string.maincategory_column_num));
            ViewGroup.LayoutParams params= new ViewGroup.LayoutParams(width, (width));
            itemView.setLayoutParams(params);
        }
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder viewHolder=(MyViewHolder)holder;
        MainCategory data=VideoStreamManager.getInstance().getMainCategoriesList().get(position);
        viewHolder.img.setImageResource(data.getCatImageId());
        viewHolder.parent.setTag(data.getId());
    }

    public void update(List<MainCategory> mainCategories) {
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return VideoStreamManager.getInstance().getMainCategoriesList().size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        LinearLayout parent;
        ImageView img;
         MyViewHolder(View itemView){
            super(itemView);
            parent=itemView.findViewById(R.id.parent);
            parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainCategoryAdapter.this.viewCallback.onMainCategorySelected(VideoStreamManager.getInstance().getMainCategory((int)v.getTag()));
                }
            });
            img=itemView.findViewById(R.id.img);
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
