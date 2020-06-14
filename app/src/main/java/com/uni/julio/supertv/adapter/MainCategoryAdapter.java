package com.uni.julio.supertv.adapter;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.uni.julio.supertv.R;
import com.uni.julio.supertv.helper.TVRecyclerView;
import com.uni.julio.supertv.helper.TVRecyclerViewAdapter;
import com.uni.julio.supertv.model.MainCategory;
import com.uni.julio.supertv.viewmodel.MainCategoriesMenuViewModelContract;

import java.util.List;

public class MainCategoryAdapter extends TVRecyclerViewAdapter<TVRecyclerViewAdapter.ViewHolder> {
    private List<MainCategory> mainCategoryDataList;
    private MainCategoriesMenuViewModelContract.View viewCallback;
    private Context mContext;
    private TVRecyclerView tvRecyclerView;
    public MainCategoryAdapter(Context context, TVRecyclerView recyclerView, final List<MainCategory> mainCategoryDatalist, final MainCategoriesMenuViewModelContract.View viewCallback){
        this.mainCategoryDataList=mainCategoryDatalist;
        this.viewCallback=viewCallback;
        this.tvRecyclerView = recyclerView;
        mContext=context;
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, final int pos) {
                 viewCallback.onMainCategorySelected(mainCategoryDatalist.get(pos));
                       }
                });
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
        return new MyViewHolder(mContext,itemView);
    }

    @Override
    protected void focusOut(View v, int position) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(v, "scaleX", 1.05f, 1.0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(v, "scaleY", 1.05f, 1.0f);
        AnimatorSet set = new AnimatorSet();
        set.play(scaleX).with(scaleY);
        set.start();
    }
    @Override
    protected void focusIn(View v, int position) {
        this.tvRecyclerView.scrollToPosition(position);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(v, "scaleX", 1.05f, 1.1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(v, "scaleY", 1.05f, 1.1f);
        AnimatorSet set = new AnimatorSet();
        set.play(scaleX).with(scaleY);
        set.start();
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDataBinding(TVRecyclerViewAdapter.ViewHolder holder, final int position) {
        final MyViewHolder viewHolder=(MyViewHolder)holder;
        final MainCategory data=mainCategoryDataList.get(position);
        viewHolder.img.setImageResource(data.getCatImageId());
        viewHolder.img.setTag(position);
    }


    @Override
    public int getItemCount() {
        return mainCategoryDataList.size();
    }

    class MyViewHolder extends TVRecyclerViewAdapter.ViewHolder{
        LinearLayout parent;
        ImageView img;
         public MyViewHolder(Context context, View itemView ){
            super(context,itemView);
            parent=itemView.findViewById(R.id.parent);
            img=itemView.findViewById(R.id.img);
            itemView.setBackground(mContext.getResources().getDrawable(R.drawable.maincategory_bg));
        }
    }
}
