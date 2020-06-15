package com.uni.julio.supertvplus.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.uni.julio.supertvplus.R;
import com.uni.julio.supertvplus.helper.RecyclerViewItemDecoration;
import com.uni.julio.supertvplus.helper.TVRecyclerView;
import com.uni.julio.supertvplus.helper.TVRecyclerViewAdapter;
import com.uni.julio.supertvplus.helper.VideoStreamManager;
import com.uni.julio.supertvplus.listeners.LoadMoviesForCategoryResponseListener;
import com.uni.julio.supertvplus.listeners.MovieSelectedListener;
import com.uni.julio.supertvplus.listeners.ShowAsGridListener;
import com.uni.julio.supertvplus.model.Movie;
import com.uni.julio.supertvplus.model.MovieCategory;
import com.uni.julio.supertvplus.utils.Device;
import com.uni.julio.supertvplus.utils.Dialogs;
import com.uni.julio.supertvplus.utils.networing.NetManager;

import java.util.List;

public class MoviesCategoryAdapter extends TVRecyclerViewAdapter<MoviesCategoryAdapter.MyViewHolder> implements  LoadMoviesForCategoryResponseListener {
    private List<MovieCategory> mMoviesList;
    private Context mContext;
    private final MovieSelectedListener mMovieSelectedListener;
    private final ShowAsGridListener mShowAsGridListener;
    private final int mMainCategoryPosition;
    private int maxTimeout = 60;
    private boolean treatAsBox = false;
    private final int[] timeOutPerRow;

    public MoviesCategoryAdapter(Context context, List<MovieCategory> videoDataList, int mainCategoryPosition, MovieSelectedListener movieSelectedListener, ShowAsGridListener showAsGridListener) {
        this.mMoviesList=videoDataList;
        this.mContext=context;
        mMovieSelectedListener = movieSelectedListener;
        mShowAsGridListener = showAsGridListener;
        mMainCategoryPosition = mainCategoryPosition;
        timeOutPerRow = new int[mMoviesList.size()];
        for(int i = 0; i < timeOutPerRow.length; i++) {
            int minTimeout = 45;
            timeOutPerRow[i] = minTimeout;
        }
        if(Device.canTreatAsBox()) {
            treatAsBox = true;
        }
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.video_list_section, viewGroup, false);
        return new MyViewHolder(mContext,convertView);
    }

    @Override
    protected void focusOut(View v, int position) {

    }
    @Override
    protected void focusIn(View v, int position) {

    }
    public void onResume() {
        try{
            if(VideoStreamManager.getInstance().getMainCategory(mMainCategoryPosition) != null){
                List<MovieCategory> mCategoriesList = VideoStreamManager.getInstance().getMainCategory(mMainCategoryPosition).getMovieCategories();
                if(mCategoriesList.size() > 0 && mCategoriesList.get(0).getCatName().equals("Favorite"))
                    NetManager.getInstance().retrieveMoviesForSubCategory(VideoStreamManager.getInstance().getMainCategory(mMainCategoryPosition), mCategoriesList.get(0), this, 30);
                if(mCategoriesList.size() > 1 && mCategoriesList.get(1).getCatName().equals("Vistas Recientes"))
                    NetManager.getInstance().retrieveMoviesForSubCategory(VideoStreamManager.getInstance().getMainCategory(mMainCategoryPosition), mCategoriesList.get(1), this, 30);
                mMoviesList = VideoStreamManager.getInstance().getMainCategory(mMainCategoryPosition).getMovieCategories();
                this.notifyItemChanged(0);
                this.notifyItemChanged(1);
            }
        }catch (Exception e){
            e.printStackTrace();
            Dialogs.showOneButtonDialog(mContext, R.string.exception_title, R.string.exception_content, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    ((AppCompatActivity)mContext).finish();
                }
            });
        }

    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position){
        final MovieCategory movieCategory=mMoviesList.get(position);
        List<Movie> movieList = (List<Movie>) movieCategory.getMovieList();
            holder.getViewDataBinding().getRoot().setVisibility(View.VISIBLE);
            holder.getViewDataBinding().setVariable(com.uni.julio.supertvplus.BR.movieCategory, movieCategory);
            holder.viewDataBinding.getRoot().findViewById(R.id.all_pane_btn).setTag(position);
            boolean needsRedraw = true;
            holder.getViewDataBinding().getRoot().findViewById(R.id.all_pane_btn).setTag(position);
            holder.getViewDataBinding().setVariable(com.uni.julio.supertvplus.BR.categoryAdapter, this);
            if (movieCategory.hasErrorLoading()) {
                holder.getViewDataBinding().getRoot().findViewById(R.id.reload).setVisibility(View.VISIBLE);
                holder.getViewDataBinding().getRoot().findViewById(R.id.error_txt).setVisibility(View.VISIBLE);
                ((TextView) holder.getViewDataBinding().getRoot().findViewById(R.id.error_txt)).setText(mContext.getString(R.string.generic_loading_message));
                holder.getViewDataBinding().getRoot().findViewById(R.id.ic_more).setVisibility(View.GONE);
                holder.getViewDataBinding().getRoot().findViewById(R.id.reload).setTag(position);//clicks
                holder.getViewDataBinding().getRoot().findViewById(R.id.recycler_view).setVisibility(View.GONE);
                holder.getViewDataBinding().getRoot().findViewById(R.id.loadingBar).setVisibility(View.GONE);
                holder.getViewDataBinding().getRoot().findViewById(R.id.reload).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.setVisibility(View.GONE);
                        mMoviesList.get((Integer) v.getTag()).setLoaded(false);
                        mMoviesList.get((Integer) v.getTag()).setLoading(false);
                        mMoviesList.get((Integer) v.getTag()).setErrorLoading(false);
                        notifyItemChanged((Integer) v.getTag());
                    }
                });
            } else {
                if (!movieCategory.isLoaded() && (movieList == null || movieList.size() == 0)) {
                    if (!movieCategory.isLoading()) {
                        movieCategory.setLoading(true);
                        needsRedraw = false;
                        holder.getViewDataBinding().getRoot().findViewById(R.id.loadingBar).setVisibility(View.VISIBLE);
                        holder.getViewDataBinding().getRoot().findViewById(R.id.reload).setVisibility(View.GONE);
                        holder.getViewDataBinding().getRoot().findViewById(R.id.ic_more).setVisibility(View.GONE);
                        holder.getViewDataBinding().getRoot().findViewById(R.id.recycler_view).setVisibility(View.GONE);
                        holder.getViewDataBinding().getRoot().findViewById(R.id.error_txt).setVisibility(View.GONE);
                        NetManager.getInstance().retrieveMoviesForSubCategory(VideoStreamManager.getInstance().getMainCategory(mMainCategoryPosition), movieCategory, this, 30);
                    }
                }

                if (movieCategory.getCatName().contains("ecientes") && (movieList == null || movieList.size() == 0)) {
                    holder.getViewDataBinding().getRoot().findViewById(R.id.loadingBar).setVisibility(View.GONE);
                    holder.getViewDataBinding().getRoot().findViewById(R.id.reload).setVisibility(View.GONE);
                    holder.getViewDataBinding().getRoot().findViewById(R.id.recycler_view).setVisibility(View.GONE);
                    ((TextView) holder.getViewDataBinding().getRoot().findViewById(R.id.error_txt)).setText(R.string.no_content);
                    holder.getViewDataBinding().getRoot().findViewById(R.id.error_txt).setVisibility(View.VISIBLE);
                    needsRedraw = false;
                }
                if (needsRedraw) {
                    holder.getViewDataBinding().getRoot().findViewById(R.id.recycler_view).setVisibility(View.VISIBLE);
                    holder.getViewDataBinding().getRoot().findViewById(R.id.loadingBar).setVisibility(View.GONE);
                    holder.getViewDataBinding().getRoot().findViewById(R.id.reload).setVisibility(View.GONE);
                    holder.getViewDataBinding().getRoot().findViewById(R.id.ic_more).setVisibility(View.VISIBLE);
                    holder.getViewDataBinding().getRoot().findViewById(R.id.error_txt).setVisibility(View.GONE);

                    TVRecyclerView rowsRecycler = holder.getViewDataBinding().getRoot().findViewById(R.id.recycler_view);
                    GridLayoutManager rowslayoutmanger = new GridLayoutManager(mContext, 1);
                    rowslayoutmanger.setOrientation(LinearLayoutManager.HORIZONTAL);

                    MoviesRecyclerAdapter moviesRecyclerAdapter = new MoviesRecyclerAdapter(mContext, rowsRecycler, movieList, position, mMovieSelectedListener);
                    rowsRecycler.setLayoutManager(rowslayoutmanger);
                    rowsRecycler.setAdapter(moviesRecyclerAdapter);
                    rowsRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);
                        }
                    });
                    if (rowsRecycler.getItemDecorationCount() == 0) {
                        rowsRecycler.addItemDecoration(new RecyclerViewItemDecoration(16, mContext.getResources().getInteger(R.integer.recycler_decoration_padding), 32, mContext.getResources().getInteger(R.integer.recycler_decoration_padding)));
                    }

                }
            }
            holder.getViewDataBinding().executePendingBindings();

    }
    @Override
    protected void onDataBinding(MyViewHolder holder, int position) {
    }
    public void onClickItem(View view) {
         mShowAsGridListener.onShowAsGridSelected((Integer)view.getTag());
    }


    @Override
    public int getItemCount() {
        return mMoviesList.size();
    }

    public void onMoviesForCategoryCompleted(MovieCategory movieCategory) {
        movieCategory.setLoaded(true);
        if(!movieCategory.hasErrorLoading()) {
            movieCategory.setLoading(false);
            movieCategory.setErrorLoading(false);
            if(treatAsBox && movieCategory.getCatName().contains("ettings")) {
                movieCategory.setCatName("");//solo mostrar LUPA
                if(movieCategory.getMovieList().size() > 1) {
                    movieCategory.getMovieList().remove(1);
                }
                movieCategory.getMovieList().get(0).setTitle("Buscar");
                ((Movie)movieCategory.getMovieList().get(0)).setHDPosterUrl("lupita");
            }
            VideoStreamManager.getInstance().getMainCategory(mMainCategoryPosition).addMovieCategory(movieCategory.getId(), movieCategory);
        }
        this.notifyItemChanged(movieCategory.getId());
    }

    @Override
    public void onMoviesForCategoryCompletedError(MovieCategory movieCategory) {
        movieCategory.setLoaded(true);
        movieCategory.setLoading(false);
        movieCategory.setErrorLoading(true);
        this.notifyItemChanged(movieCategory.getId());
    }

    @Override
    public void onError() {

    }


    class MyViewHolder extends TVRecyclerViewAdapter.ViewHolder{
        ViewDataBinding viewDataBinding;
        private MyViewHolder(Context context,View itemView){
            super(context,itemView);
            viewDataBinding= DataBindingUtil.bind(itemView);
         }
         private ViewDataBinding getViewDataBinding(){
            return viewDataBinding;
         }
    }

}
