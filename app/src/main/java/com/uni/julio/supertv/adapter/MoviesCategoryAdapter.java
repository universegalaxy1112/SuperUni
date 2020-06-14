package com.uni.julio.supertv.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uni.julio.supertv.R;
import com.uni.julio.supertv.helper.RecyclerViewItemDecoration;
import com.uni.julio.supertv.helper.TVRecyclerView;
import com.uni.julio.supertv.helper.TVRecyclerViewAdapter;
import com.uni.julio.supertv.helper.VideoStreamManager;
import com.uni.julio.supertv.listeners.LoadMoviesForCategoryResponseListener;
import com.uni.julio.supertv.listeners.MovieAcceptedListener;
import com.uni.julio.supertv.listeners.MovieSelectedListener;
import com.uni.julio.supertv.listeners.SearchSelectedListener;
import com.uni.julio.supertv.listeners.ShowAsGridListener;
import com.uni.julio.supertv.model.ModelTypes;
import com.uni.julio.supertv.model.Movie;
import com.uni.julio.supertv.model.MovieCategory;
import com.uni.julio.supertv.utils.Device;
import com.uni.julio.supertv.utils.Dialogs;
import com.uni.julio.supertv.utils.networing.NetManager;

import java.util.List;

public class MoviesCategoryAdapter extends TVRecyclerViewAdapter<MoviesCategoryAdapter.MyViewHolder> implements MovieSelectedListener, LoadMoviesForCategoryResponseListener {
    private List<MovieCategory> mMoviesList;
    private Context mContext;
    private final MovieSelectedListener mMovieSelectedListener;
    private final ShowAsGridListener mShowAsGridListener;
    private final int mMainCategoryPosition;
    private int maxTimeout = 60;
    private boolean adapt_flag=true;
    private int[] lastSelectedItemByRow;
    private boolean treatAsBox = false;
    private final int[] timeOutPerRow;
    private TVRecyclerView recyclerView;
    ImageView imageView;
    private List<MovieCategory> mCategoriesList;

    public MoviesCategoryAdapter(Context context ,TVRecyclerView recyclerView, List<MovieCategory> videoDataList, int mainCategoryPosition, MovieAcceptedListener movieAcceptedListener, MovieSelectedListener movieSelectedListener, ShowAsGridListener showAsGridListener, SearchSelectedListener searchSelectedListener) {
        this.mMoviesList=videoDataList;
        this.mContext=context;
        this.recyclerView=recyclerView;
        mMovieSelectedListener = movieSelectedListener;
        mShowAsGridListener = showAsGridListener;
        mMainCategoryPosition = mainCategoryPosition;
        lastSelectedItemByRow = new int[mMoviesList.size()];
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
        recyclerView.scrollToPosition(position);

    }
    public void onResume() {
        try{
            if(VideoStreamManager.getInstance().getMainCategoriesList().size() > mMainCategoryPosition){
                mCategoriesList = VideoStreamManager.getInstance().getMainCategory(mMainCategoryPosition).getMovieCategories();
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
        MovieCategory movieCategory=mMoviesList.get(position);
        List<Movie> movieList = (List<Movie>) movieCategory.getMovieList();
            holder.getViewDataBinding().getRoot().setVisibility(View.VISIBLE);
            holder.getViewDataBinding().setVariable(com.uni.julio.supertv.BR.movieCategory, movieCategory);
            holder.viewDataBinding.getRoot().findViewById(R.id.all_pane_btn).setTag(position);
            boolean needsRedraw = true;
            holder.getViewDataBinding().getRoot().findViewById(R.id.all_pane_btn).setTag(position);
            holder.getViewDataBinding().setVariable(com.uni.julio.supertv.BR.categoryAdapter, this);
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
                        timeOutPerRow[(Integer) v.getTag()] = timeOutPerRow[(Integer) v.getTag()] + 15;
                        if (timeOutPerRow[(Integer) v.getTag()] >= maxTimeout) {
                            timeOutPerRow[(Integer) v.getTag()] = maxTimeout;
                        }
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
                        NetManager.getInstance().retrieveMoviesForSubCategory(VideoStreamManager.getInstance().getMainCategory(mMainCategoryPosition), movieCategory, this, timeOutPerRow[position]);
                    }
                }

                if (movieCategory.getCatName().contains("ecientes") && (movieList == null || movieList.size() == 0)) {
                    holder.getViewDataBinding().getRoot().findViewById(R.id.loadingBar).setVisibility(View.GONE);
                    holder.getViewDataBinding().getRoot().findViewById(R.id.reload).setVisibility(View.GONE);
                    holder.getViewDataBinding().getRoot().findViewById(R.id.recycler_view).setVisibility(View.GONE);
                    ((TextView) holder.getViewDataBinding().getRoot().findViewById(R.id.error_txt)).setText("No Content");
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
                    moviesRecyclerAdapter.setTreatAsBox(treatAsBox);
                    rowsRecycler.setLayoutManager(rowslayoutmanger);
                    rowsRecycler.setAdapter(moviesRecyclerAdapter);
                    rowsRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);
                        }
                    });
                    if (rowsRecycler.getItemDecorationCount() == 0) {
                        rowsRecycler.addItemDecoration(new RecyclerViewItemDecoration(20, 16, 48, 16));
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
            ;//Log.d("liveTV", "Adding MovieCategory " + movieCategory.getCatName() + " in position " + movieCategory.getId());
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

    @Override
    public void onMovieSelected(int rowPosition, int itemPosition) {

            lastSelectedItemByRow[rowPosition] = itemPosition;
            mMovieSelectedListener.onMovieSelected(rowPosition,itemPosition);

    }

   public  class MyViewHolder extends TVRecyclerViewAdapter.ViewHolder{
        private ViewDataBinding viewDataBinding;
        public MyViewHolder(Context context,View itemView){
            super(context,itemView);
            viewDataBinding= DataBindingUtil.bind(itemView);

         }
         public TVRecyclerView getRecycler(){return getViewDataBinding().getRoot().findViewById(R.id.moviecategoryrecycler);}
         public ViewDataBinding getViewDataBinding(){
            return viewDataBinding;
         }
    }

}
