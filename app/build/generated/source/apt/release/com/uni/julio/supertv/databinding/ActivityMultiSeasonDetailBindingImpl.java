package com.uni.julio.supertv.databinding;
import com.uni.julio.supertv.R;
import com.uni.julio.supertv.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityMultiSeasonDetailBindingImpl extends ActivityMultiSeasonDetailBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.fondoUrl, 20);
        sViewsWithIds.put(R.id.scrollview, 21);
        sViewsWithIds.put(R.id.description, 22);
        sViewsWithIds.put(R.id.actors, 23);
        sViewsWithIds.put(R.id.director, 24);
        sViewsWithIds.put(R.id.detail_tab, 25);
        sViewsWithIds.put(R.id.recycler_view, 26);
        sViewsWithIds.put(R.id.like, 27);
        sViewsWithIds.put(R.id.dislike, 28);
    }
    // views
    @NonNull
    private final androidx.constraintlayout.widget.ConstraintLayout mboundView0;
    @NonNull
    private final android.widget.TextView mboundView13;
    @NonNull
    private final android.widget.TextView mboundView15;
    @NonNull
    private final android.widget.TextView mboundView16;
    @NonNull
    private final android.widget.LinearLayout mboundView17;
    @NonNull
    private final android.widget.LinearLayout mboundView18;
    @NonNull
    private final android.widget.LinearLayout mboundView19;
    @NonNull
    private final androidx.cardview.widget.CardView mboundView3;
    @NonNull
    private final androidx.cardview.widget.CardView mboundView5;
    @NonNull
    private final android.widget.LinearLayout mboundView6;
    @NonNull
    private final androidx.cardview.widget.CardView mboundView7;
    @NonNull
    private final android.widget.LinearLayout mboundView8;
    // variables
    // values
    // listeners
    private OnClickListenerImpl mMovieDetailsVMPlayTrailerAndroidViewViewOnClickListener;
    private OnClickListenerImpl1 mMovieDetailsVMLikeAndroidViewViewOnClickListener;
    private OnClickListenerImpl2 mMovieDetailsVMOnClickFavoriteAndroidViewViewOnClickListener;
    private OnClickListenerImpl3 mMovieDetailsVMFinishActivityAndroidViewViewOnClickListener;
    private OnClickListenerImpl4 mMovieDetailsVMReportAndroidViewViewOnClickListener;
    private OnClickListenerImpl5 mMovieDetailsVMPlayHDAndroidViewViewOnClickListener;
    private OnClickListenerImpl6 mMovieDetailsVMPlaySDAndroidViewViewOnClickListener;
    private OnClickListenerImpl7 mMovieDetailsVMDislikeAndroidViewViewOnClickListener;
    // Inverse Binding Event Handlers

    public ActivityMultiSeasonDetailBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 29, sIncludes, sViewsWithIds));
    }
    private ActivityMultiSeasonDetailBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 6
            , (android.widget.TextView) bindings[23]
            , (android.widget.TextView) bindings[10]
            , (android.widget.TextView) bindings[22]
            , (android.widget.TextView) bindings[9]
            , (com.google.android.material.tabs.TabLayout) bindings[25]
            , (android.widget.TextView) bindings[24]
            , (android.widget.TextView) bindings[11]
            , (android.widget.TextView) bindings[28]
            , (android.widget.ImageView) bindings[20]
            , (android.widget.ImageView) bindings[2]
            , (android.widget.ImageView) bindings[1]
            , (android.widget.TextView) bindings[27]
            , (android.widget.LinearLayout) bindings[4]
            , (android.widget.RatingBar) bindings[14]
            , (com.uni.julio.supertv.helper.TVRecyclerView) bindings[26]
            , (android.widget.ScrollView) bindings[21]
            , (android.widget.TextView) bindings[12]
            );
        this.actorsDetail.setTag(null);
        this.descriptionDetail.setTag(null);
        this.directorDetail.setTag(null);
        this.imageView7.setTag(null);
        this.imageView8.setTag(null);
        this.mboundView0 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView13 = (android.widget.TextView) bindings[13];
        this.mboundView13.setTag(null);
        this.mboundView15 = (android.widget.TextView) bindings[15];
        this.mboundView15.setTag(null);
        this.mboundView16 = (android.widget.TextView) bindings[16];
        this.mboundView16.setTag(null);
        this.mboundView17 = (android.widget.LinearLayout) bindings[17];
        this.mboundView17.setTag(null);
        this.mboundView18 = (android.widget.LinearLayout) bindings[18];
        this.mboundView18.setTag(null);
        this.mboundView19 = (android.widget.LinearLayout) bindings[19];
        this.mboundView19.setTag(null);
        this.mboundView3 = (androidx.cardview.widget.CardView) bindings[3];
        this.mboundView3.setTag(null);
        this.mboundView5 = (androidx.cardview.widget.CardView) bindings[5];
        this.mboundView5.setTag(null);
        this.mboundView6 = (android.widget.LinearLayout) bindings[6];
        this.mboundView6.setTag(null);
        this.mboundView7 = (androidx.cardview.widget.CardView) bindings[7];
        this.mboundView7.setTag(null);
        this.mboundView8 = (android.widget.LinearLayout) bindings[8];
        this.mboundView8.setTag(null);
        this.play.setTag(null);
        this.ratingBar.setTag(null);
        this.textView5.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x100L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
        if (BR.movieDetailItem == variableId) {
            setMovieDetailItem((com.uni.julio.supertv.model.Movie) variable);
        }
        else if (BR.movieDetailsVM == variableId) {
            setMovieDetailsVM((com.uni.julio.supertv.viewmodel.EpisodeDetailsViewModel) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setMovieDetailItem(@Nullable com.uni.julio.supertv.model.Movie MovieDetailItem) {
        this.mMovieDetailItem = MovieDetailItem;
        synchronized(this) {
            mDirtyFlags |= 0x40L;
        }
        notifyPropertyChanged(BR.movieDetailItem);
        super.requestRebind();
    }
    public void setMovieDetailsVM(@Nullable com.uni.julio.supertv.viewmodel.EpisodeDetailsViewModel MovieDetailsVM) {
        this.mMovieDetailsVM = MovieDetailsVM;
        synchronized(this) {
            mDirtyFlags |= 0x80L;
        }
        notifyPropertyChanged(BR.movieDetailsVM);
        super.requestRebind();
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeMovieDetailsVMDisliked((androidx.databinding.ObservableBoolean) object, fieldId);
            case 1 :
                return onChangeMovieDetailsVMIsHD((androidx.databinding.ObservableBoolean) object, fieldId);
            case 2 :
                return onChangeMovieDetailsVMIsSD((androidx.databinding.ObservableBoolean) object, fieldId);
            case 3 :
                return onChangeMovieDetailsVMIsTrailer((androidx.databinding.ObservableBoolean) object, fieldId);
            case 4 :
                return onChangeMovieDetailsVMLiked((androidx.databinding.ObservableBoolean) object, fieldId);
            case 5 :
                return onChangeMovieDetailsVMIsFavorite((androidx.databinding.ObservableBoolean) object, fieldId);
        }
        return false;
    }
    private boolean onChangeMovieDetailsVMDisliked(androidx.databinding.ObservableBoolean MovieDetailsVMDisliked, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeMovieDetailsVMIsHD(androidx.databinding.ObservableBoolean MovieDetailsVMIsHD, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x2L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeMovieDetailsVMIsSD(androidx.databinding.ObservableBoolean MovieDetailsVMIsSD, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x4L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeMovieDetailsVMIsTrailer(androidx.databinding.ObservableBoolean MovieDetailsVMIsTrailer, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x8L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeMovieDetailsVMLiked(androidx.databinding.ObservableBoolean MovieDetailsVMLiked, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x10L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeMovieDetailsVMIsFavorite(androidx.databinding.ObservableBoolean MovieDetailsVMIsFavorite, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x20L;
            }
            return true;
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        java.lang.String movieDetailItemDescription = null;
        androidx.databinding.ObservableBoolean movieDetailsVMDisliked = null;
        android.view.View.OnClickListener movieDetailsVMPlayTrailerAndroidViewViewOnClickListener = null;
        android.view.View.OnClickListener movieDetailsVMLikeAndroidViewViewOnClickListener = null;
        androidx.databinding.ObservableBoolean movieDetailsVMIsHD = null;
        boolean movieDetailsVMIsHDGet = false;
        android.view.View.OnClickListener movieDetailsVMOnClickFavoriteAndroidViewViewOnClickListener = null;
        java.lang.String movieDetailItemReleaseDate = null;
        android.view.View.OnClickListener movieDetailsVMFinishActivityAndroidViewViewOnClickListener = null;
        androidx.databinding.ObservableBoolean movieDetailsVMIsSD = null;
        int movieDetailItemStarRating = 0;
        java.lang.String movieDetailItemDirector = null;
        androidx.databinding.ObservableBoolean movieDetailsVMIsTrailer = null;
        boolean movieDetailsVMDislikedGet = false;
        android.view.View.OnClickListener movieDetailsVMReportAndroidViewViewOnClickListener = null;
        android.view.View.OnClickListener movieDetailsVMPlayHDAndroidViewViewOnClickListener = null;
        java.lang.String movieDetailItemActors = null;
        android.view.View.OnClickListener movieDetailsVMPlaySDAndroidViewViewOnClickListener = null;
        com.uni.julio.supertv.model.Movie movieDetailItem = mMovieDetailItem;
        android.view.View.OnClickListener movieDetailsVMDislikeAndroidViewViewOnClickListener = null;
        boolean movieDetailsVMLikedGet = false;
        androidx.databinding.ObservableBoolean movieDetailsVMLiked = null;
        boolean movieDetailsVMIsTrailerGet = false;
        boolean movieDetailItemHDBranded = false;
        java.lang.String movieDetailItemTitle = null;
        int movieDetailItemGetLength = 0;
        boolean movieDetailsVMIsSDGet = false;
        androidx.databinding.ObservableBoolean movieDetailsVMIsFavorite = null;
        boolean movieDetailsVMIsFavoriteGet = false;
        com.uni.julio.supertv.viewmodel.EpisodeDetailsViewModel movieDetailsVM = mMovieDetailsVM;

        if ((dirtyFlags & 0x140L) != 0) {



                if (movieDetailItem != null) {
                    // read movieDetailItem.Description
                    movieDetailItemDescription = movieDetailItem.getDescription();
                    // read movieDetailItem.ReleaseDate
                    movieDetailItemReleaseDate = movieDetailItem.getReleaseDate();
                    // read movieDetailItem.StarRating
                    movieDetailItemStarRating = movieDetailItem.getStarRating();
                    // read movieDetailItem.Director
                    movieDetailItemDirector = movieDetailItem.getDirector();
                    // read movieDetailItem.Actors
                    movieDetailItemActors = movieDetailItem.getActors();
                    // read movieDetailItem.HDBranded
                    movieDetailItemHDBranded = movieDetailItem.isHDBranded();
                    // read movieDetailItem.Title
                    movieDetailItemTitle = movieDetailItem.getTitle();
                    // read movieDetailItem.getLength
                    movieDetailItemGetLength = movieDetailItem.getLength();
                }
        }
        if ((dirtyFlags & 0x1bfL) != 0) {


            if ((dirtyFlags & 0x181L) != 0) {

                    if (movieDetailsVM != null) {
                        // read movieDetailsVM.disliked
                        movieDetailsVMDisliked = movieDetailsVM.disliked;
                    }
                    updateRegistration(0, movieDetailsVMDisliked);


                    if (movieDetailsVMDisliked != null) {
                        // read movieDetailsVM.disliked.get()
                        movieDetailsVMDislikedGet = movieDetailsVMDisliked.get();
                    }
            }
            if ((dirtyFlags & 0x180L) != 0) {

                    if (movieDetailsVM != null) {
                        // read movieDetailsVM::playTrailer
                        movieDetailsVMPlayTrailerAndroidViewViewOnClickListener = (((mMovieDetailsVMPlayTrailerAndroidViewViewOnClickListener == null) ? (mMovieDetailsVMPlayTrailerAndroidViewViewOnClickListener = new OnClickListenerImpl()) : mMovieDetailsVMPlayTrailerAndroidViewViewOnClickListener).setValue(movieDetailsVM));
                        // read movieDetailsVM::like
                        movieDetailsVMLikeAndroidViewViewOnClickListener = (((mMovieDetailsVMLikeAndroidViewViewOnClickListener == null) ? (mMovieDetailsVMLikeAndroidViewViewOnClickListener = new OnClickListenerImpl1()) : mMovieDetailsVMLikeAndroidViewViewOnClickListener).setValue(movieDetailsVM));
                        // read movieDetailsVM::onClickFavorite
                        movieDetailsVMOnClickFavoriteAndroidViewViewOnClickListener = (((mMovieDetailsVMOnClickFavoriteAndroidViewViewOnClickListener == null) ? (mMovieDetailsVMOnClickFavoriteAndroidViewViewOnClickListener = new OnClickListenerImpl2()) : mMovieDetailsVMOnClickFavoriteAndroidViewViewOnClickListener).setValue(movieDetailsVM));
                        // read movieDetailsVM::finishActivity
                        movieDetailsVMFinishActivityAndroidViewViewOnClickListener = (((mMovieDetailsVMFinishActivityAndroidViewViewOnClickListener == null) ? (mMovieDetailsVMFinishActivityAndroidViewViewOnClickListener = new OnClickListenerImpl3()) : mMovieDetailsVMFinishActivityAndroidViewViewOnClickListener).setValue(movieDetailsVM));
                        // read movieDetailsVM::report
                        movieDetailsVMReportAndroidViewViewOnClickListener = (((mMovieDetailsVMReportAndroidViewViewOnClickListener == null) ? (mMovieDetailsVMReportAndroidViewViewOnClickListener = new OnClickListenerImpl4()) : mMovieDetailsVMReportAndroidViewViewOnClickListener).setValue(movieDetailsVM));
                        // read movieDetailsVM::playHD
                        movieDetailsVMPlayHDAndroidViewViewOnClickListener = (((mMovieDetailsVMPlayHDAndroidViewViewOnClickListener == null) ? (mMovieDetailsVMPlayHDAndroidViewViewOnClickListener = new OnClickListenerImpl5()) : mMovieDetailsVMPlayHDAndroidViewViewOnClickListener).setValue(movieDetailsVM));
                        // read movieDetailsVM::playSD
                        movieDetailsVMPlaySDAndroidViewViewOnClickListener = (((mMovieDetailsVMPlaySDAndroidViewViewOnClickListener == null) ? (mMovieDetailsVMPlaySDAndroidViewViewOnClickListener = new OnClickListenerImpl6()) : mMovieDetailsVMPlaySDAndroidViewViewOnClickListener).setValue(movieDetailsVM));
                        // read movieDetailsVM::dislike
                        movieDetailsVMDislikeAndroidViewViewOnClickListener = (((mMovieDetailsVMDislikeAndroidViewViewOnClickListener == null) ? (mMovieDetailsVMDislikeAndroidViewViewOnClickListener = new OnClickListenerImpl7()) : mMovieDetailsVMDislikeAndroidViewViewOnClickListener).setValue(movieDetailsVM));
                    }
            }
            if ((dirtyFlags & 0x182L) != 0) {

                    if (movieDetailsVM != null) {
                        // read movieDetailsVM.isHD
                        movieDetailsVMIsHD = movieDetailsVM.isHD;
                    }
                    updateRegistration(1, movieDetailsVMIsHD);


                    if (movieDetailsVMIsHD != null) {
                        // read movieDetailsVM.isHD.get()
                        movieDetailsVMIsHDGet = movieDetailsVMIsHD.get();
                    }
            }
            if ((dirtyFlags & 0x184L) != 0) {

                    if (movieDetailsVM != null) {
                        // read movieDetailsVM.isSD
                        movieDetailsVMIsSD = movieDetailsVM.isSD;
                    }
                    updateRegistration(2, movieDetailsVMIsSD);


                    if (movieDetailsVMIsSD != null) {
                        // read movieDetailsVM.isSD.get()
                        movieDetailsVMIsSDGet = movieDetailsVMIsSD.get();
                    }
            }
            if ((dirtyFlags & 0x188L) != 0) {

                    if (movieDetailsVM != null) {
                        // read movieDetailsVM.isTrailer
                        movieDetailsVMIsTrailer = movieDetailsVM.isTrailer;
                    }
                    updateRegistration(3, movieDetailsVMIsTrailer);


                    if (movieDetailsVMIsTrailer != null) {
                        // read movieDetailsVM.isTrailer.get()
                        movieDetailsVMIsTrailerGet = movieDetailsVMIsTrailer.get();
                    }
            }
            if ((dirtyFlags & 0x190L) != 0) {

                    if (movieDetailsVM != null) {
                        // read movieDetailsVM.liked
                        movieDetailsVMLiked = movieDetailsVM.liked;
                    }
                    updateRegistration(4, movieDetailsVMLiked);


                    if (movieDetailsVMLiked != null) {
                        // read movieDetailsVM.liked.get()
                        movieDetailsVMLikedGet = movieDetailsVMLiked.get();
                    }
            }
            if ((dirtyFlags & 0x1a0L) != 0) {

                    if (movieDetailsVM != null) {
                        // read movieDetailsVM.isFavorite
                        movieDetailsVMIsFavorite = movieDetailsVM.isFavorite;
                    }
                    updateRegistration(5, movieDetailsVMIsFavorite);


                    if (movieDetailsVMIsFavorite != null) {
                        // read movieDetailsVM.isFavorite.get()
                        movieDetailsVMIsFavoriteGet = movieDetailsVMIsFavorite.get();
                    }
            }
        }
        // batch finished
        if ((dirtyFlags & 0x140L) != 0) {
            // api target 1

            com.uni.julio.supertv.binding.BindingAdapters.setActors(this.actorsDetail, movieDetailItemActors);
            com.uni.julio.supertv.binding.BindingAdapters.setDescription(this.descriptionDetail, movieDetailItemDescription);
            com.uni.julio.supertv.binding.BindingAdapters.setDirector(this.directorDetail, movieDetailItemDirector);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView13, movieDetailItemReleaseDate);
            com.uni.julio.supertv.binding.BindingAdapters.visibleText(this.mboundView13, movieDetailItemReleaseDate);
            com.uni.julio.supertv.binding.BindingAdapters.setDuration(this.mboundView15, movieDetailItemGetLength);
            com.uni.julio.supertv.binding.BindingAdapters.bindShowHDIcon(this.mboundView16, movieDetailItemHDBranded);
            com.uni.julio.supertv.binding.BindingAdapters.setRating(this.ratingBar, movieDetailItemStarRating);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.textView5, movieDetailItemTitle);
        }
        if ((dirtyFlags & 0x180L) != 0) {
            // api target 1

            this.imageView7.setOnClickListener(movieDetailsVMFinishActivityAndroidViewViewOnClickListener);
            this.imageView8.setOnClickListener(movieDetailsVMOnClickFavoriteAndroidViewViewOnClickListener);
            this.mboundView17.setOnClickListener(movieDetailsVMLikeAndroidViewViewOnClickListener);
            this.mboundView18.setOnClickListener(movieDetailsVMDislikeAndroidViewViewOnClickListener);
            this.mboundView19.setOnClickListener(movieDetailsVMReportAndroidViewViewOnClickListener);
            this.mboundView6.setOnClickListener(movieDetailsVMPlaySDAndroidViewViewOnClickListener);
            this.mboundView8.setOnClickListener(movieDetailsVMPlayTrailerAndroidViewViewOnClickListener);
            this.play.setOnClickListener(movieDetailsVMPlayHDAndroidViewViewOnClickListener);
        }
        if ((dirtyFlags & 0x1a0L) != 0) {
            // api target 1

            com.uni.julio.supertv.binding.BindingAdapters.bindShowFavoriteIcon(this.imageView8, movieDetailsVMIsFavoriteGet);
        }
        if ((dirtyFlags & 0x190L) != 0) {
            // api target 1

            com.uni.julio.supertv.binding.BindingAdapters.bindLikeButton(this.mboundView17, movieDetailsVMLikedGet);
        }
        if ((dirtyFlags & 0x181L) != 0) {
            // api target 1

            com.uni.julio.supertv.binding.BindingAdapters.bindDislikeButton(this.mboundView18, movieDetailsVMDislikedGet);
        }
        if ((dirtyFlags & 0x182L) != 0) {
            // api target 1

            com.uni.julio.supertv.binding.BindingAdapters.bindInvisibleVisibility(this.mboundView3, movieDetailsVMIsHDGet);
        }
        if ((dirtyFlags & 0x184L) != 0) {
            // api target 1

            com.uni.julio.supertv.binding.BindingAdapters.bindInvisibleVisibility(this.mboundView5, movieDetailsVMIsSDGet);
        }
        if ((dirtyFlags & 0x188L) != 0) {
            // api target 1

            com.uni.julio.supertv.binding.BindingAdapters.bindInvisibleVisibility(this.mboundView7, movieDetailsVMIsTrailerGet);
        }
    }
    // Listener Stub Implementations
    public static class OnClickListenerImpl implements android.view.View.OnClickListener{
        private com.uni.julio.supertv.viewmodel.EpisodeDetailsViewModel value;
        public OnClickListenerImpl setValue(com.uni.julio.supertv.viewmodel.EpisodeDetailsViewModel value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.playTrailer(arg0); 
        }
    }
    public static class OnClickListenerImpl1 implements android.view.View.OnClickListener{
        private com.uni.julio.supertv.viewmodel.EpisodeDetailsViewModel value;
        public OnClickListenerImpl1 setValue(com.uni.julio.supertv.viewmodel.EpisodeDetailsViewModel value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.like(arg0); 
        }
    }
    public static class OnClickListenerImpl2 implements android.view.View.OnClickListener{
        private com.uni.julio.supertv.viewmodel.EpisodeDetailsViewModel value;
        public OnClickListenerImpl2 setValue(com.uni.julio.supertv.viewmodel.EpisodeDetailsViewModel value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.onClickFavorite(arg0); 
        }
    }
    public static class OnClickListenerImpl3 implements android.view.View.OnClickListener{
        private com.uni.julio.supertv.viewmodel.EpisodeDetailsViewModel value;
        public OnClickListenerImpl3 setValue(com.uni.julio.supertv.viewmodel.EpisodeDetailsViewModel value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.finishActivity(arg0); 
        }
    }
    public static class OnClickListenerImpl4 implements android.view.View.OnClickListener{
        private com.uni.julio.supertv.viewmodel.EpisodeDetailsViewModel value;
        public OnClickListenerImpl4 setValue(com.uni.julio.supertv.viewmodel.EpisodeDetailsViewModel value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.report(arg0); 
        }
    }
    public static class OnClickListenerImpl5 implements android.view.View.OnClickListener{
        private com.uni.julio.supertv.viewmodel.EpisodeDetailsViewModel value;
        public OnClickListenerImpl5 setValue(com.uni.julio.supertv.viewmodel.EpisodeDetailsViewModel value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.playHD(arg0); 
        }
    }
    public static class OnClickListenerImpl6 implements android.view.View.OnClickListener{
        private com.uni.julio.supertv.viewmodel.EpisodeDetailsViewModel value;
        public OnClickListenerImpl6 setValue(com.uni.julio.supertv.viewmodel.EpisodeDetailsViewModel value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.playSD(arg0); 
        }
    }
    public static class OnClickListenerImpl7 implements android.view.View.OnClickListener{
        private com.uni.julio.supertv.viewmodel.EpisodeDetailsViewModel value;
        public OnClickListenerImpl7 setValue(com.uni.julio.supertv.viewmodel.EpisodeDetailsViewModel value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.dislike(arg0); 
        }
    }
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): movieDetailsVM.disliked
        flag 1 (0x2L): movieDetailsVM.isHD
        flag 2 (0x3L): movieDetailsVM.isSD
        flag 3 (0x4L): movieDetailsVM.isTrailer
        flag 4 (0x5L): movieDetailsVM.liked
        flag 5 (0x6L): movieDetailsVM.isFavorite
        flag 6 (0x7L): movieDetailItem
        flag 7 (0x8L): movieDetailsVM
        flag 8 (0x9L): null
    flag mapping end*/
    //end
}