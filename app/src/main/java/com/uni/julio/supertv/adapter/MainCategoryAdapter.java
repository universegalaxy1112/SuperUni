package com.uni.julio.supertv.adapter;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.uni.julio.supertv.R;
import com.uni.julio.supertv.helper.TVRecyclerViewAdapter;
import com.uni.julio.supertv.model.MainCategory;
import com.uni.julio.supertv.viewmodel.MainCategoriesMenuViewModelContract;

import java.util.List;

public class MainCategoryAdapter extends TVRecyclerViewAdapter<TVRecyclerViewAdapter.ViewHolder> {
    List<MainCategory> mainCategoryDataList;
    MainCategoriesMenuViewModelContract.View viewCallback;
    Context mContext;
    public MainCategoryAdapter(Context context, final List<MainCategory> mainCategoryDatalist, final MainCategoriesMenuViewModelContract.View viewCallback){
        this.mainCategoryDataList=mainCategoryDatalist;
        this.viewCallback=viewCallback;
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
