package com.uni.julio.supertv.databinding;
import com.uni.julio.supertv.R;
import com.uni.julio.supertv.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityOneseasonDetailBindingLandImpl extends ActivityOneseasonDetailBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.appBarLayout, 21);
        sViewsWithIds.put(R.id.scroll, 22);
        sViewsWithIds.put(R.id.description, 23);
        sViewsWithIds.put(R.id.actors, 24);
        sViewsWithIds.put(R.id.director, 25);
        sViewsWithIds.put(R.id.like, 26);
        sViewsWithIds.put(R.id.dislike, 27);
    }
    // views
    @NonNull
    private final android.widget.LinearLayout mboundView0;
    @NonNull
    private final android.widget.TextView mboundView14;
    @NonNull
    private final android.widget.TextView mboundView16;
    @NonNull
    private final android.widget.TextView mboundView17;
    @NonNull
    private final android.widget.LinearLayout mboundView18;
    @NonNull
    private final android.widget.LinearLayout mboundView19;
    @NonNull
    private final android.widget.LinearLayout mboundView20;
    @NonNull
    private final androidx.cardview.widget.CardView mboundView4;
    @NonNull
    private final androidx.cardview.widget.CardView mboundView6;
    @NonNull
    private final android.widget.LinearLayout mboundView7;
    @NonNull
    private final androidx.cardview.widget.CardView mboundView8;
    @NonNull
    private final android.widget.LinearLayout mboundView9;
    // variables
    // values
    // listeners
    private OnClickListenerImpl mMovieDetailsVMLikeAndroidViewViewOnClickListener;
    private OnClickListenerImpl1 mMovieDetailsVMOnClickFavoriteAndroidViewViewOnClickListener;
    private OnClickListenerImpl2 mMovieDetailsVMFinishActivityAndroidViewViewOnClickListener;
    private OnClickListenerImpl3 mMovieDetailsVMReportAndroidViewViewOnClickListener;
    private OnClickListenerImpl4 mMovieDetailsVMPlayHDAndroidViewViewOnClickListener;
    private OnClickListenerImpl5 mMovieDetailsVMPlaySDAndroidViewViewOnClickListener;
    private OnClickListenerImpl6 mMovieDetailsVMDislikeAndroidViewViewOnClickListener;
    private OnClickListenerImpl7 mMovieDetailsVMPlayTrailorAndroidViewViewOnClickListener;
    // Inverse Binding Event Handlers

    public ActivityOneseasonDetailBindingLandImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 28, sIncludes, sViewsWithIds));
    }
    private ActivityOneseasonDetailBindingLandImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 6
            , (android.widget.TextView) bindings[24]
            , (android.widget.TextView) bindings[11]
            , (com.google.android.material.appbar.AppBarLayout) bindings[21]
            , (android.widget.TextView) bindings[23]
            , (android.widget.TextView) bindings[10]
            , (android.widget.TextView) bindings[25]
            , (android.widget.TextView) bindings[12]
            , (android.widget.TextView) bindings[27]
            , (android.widget.ImageView) bindings[1]
            , (android.widget.ImageView) bindings[3]
            , (android.widget.ImageView) bindings[2]
            , (android.widget.TextView) bindings[26]
            , (android.widget.LinearLayout) bindings[5]
            , (android.widget.RatingBar) bindings[15]
            , (androidx.core.widget.NestedScrollView) bindings[22]
            , (android.widget.TextView) bindings[13]
            );
        this.actorsDetail.setTag(null);
        this.descriptionDetail.setTag(null);
        this.directorDetail.setTag(null);
        this.fondoUrl.setTag(null);
        this.imageView7.setTag(null);
        this.imageView8.setTag(null);
        this.mboundView0 = (android.widget.LinearLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView14 = (android.widget.TextView) bindings[14];
        this.mboundView14.setTag(null);
        this.mboundView16 = (android.widget.TextView) bindings[16];
        this.mboundView16.setTag(null);
        this.mboundView17 = (android.widget.TextView) bindings[17];
        this.mboundView17.setTag(null);
        this.mboundView18 = (android.widget.LinearLayout) bindings[18];
        this.mboundView18.setTag(null);
        this.mboundView19 = (android.widget.LinearLayout) bindings[19];
        this.mboundView19.setTag(null);
        this.mboundView20 = (android.widget.LinearLayout) bindings[20];
        this.mboundView20.setTag(null);
        this.mboundView4 = (androidx.cardview.widget.CardView) bindings[4];
        this.mboundView4.setTag(null);
        this.mboundView6 = (androidx.cardview.widget.CardView) bindings[6];
        this.mboundView6.setTag(null);
        this.mboundView7 = (android.widget.LinearLayout) bindings[7];
        this.mboundView7.setTag(null);
        this.mboundView8 = (androidx.cardview.widget.CardView) bindings[8];
        this.mboundView8.setTag(null);
        this.mboundView9 = (android.widget.LinearLayout) bindings[9];
        this.mboundView9.setTag(null);
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
            setMovieDetailsVM((com.uni.julio.supertv.viewmodel.MovieDetailsViewModel) variable);
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
    public void setMovieDetailsVM(@Nullable com.uni.julio.supertv.viewmodel.MovieDetailsViewModel MovieDetailsVM) {
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
        java.lang.String movieDetailItemGetReleaseDate = null;
        android.view.View.OnClickListener movieDetailsVMLikeAndroidViewViewOnClickListener = null;
        androidx.databinding.ObservableBoolean movieDetailsVMIsHD = null;
        boolean movieDetailsVMIsHDGet = false;
        java.lang.String movieDetailItemHDFondoUrl = null;
        android.view.View.OnClickListener movieDetailsVMOnClickFavoriteAndroidViewViewOnClickListener = null;
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
        boolean movieDetailItemIsHDBranded = false;
        boolean movieDetailsVMIsTrailerGet = false;
        java.lang.String movieDetailItemTitle = null;
        int movieDetailItemGetLength = 0;
        boolean movieDetailsVMIsSDGet = false;
        androidx.databinding.ObservableBoolean movieDetailsVMIsFavorite = null;
        boolean movieDetailsVMIsFavoriteGet = false;
        android.view.View.OnClickListener movieDetailsVMPlayTrailorAndroidViewViewOnClickListener = null;
        com.uni.julio.supertv.viewmodel.MovieDetailsViewModel movieDetailsVM = mMovieDetailsVM;

        if ((dirtyFlags & 0x140L) != 0) {



                if (movieDetailItem != null) {
                    // read movieDetailItem.Description
                    movieDetailItemDescription = movieDetailItem.getDescription();
                    // read movieDetailItem.getReleaseDate
                    movieDetailItemGetReleaseDate = movieDetailItem.getReleaseDate();
                    // read movieDetailItem.hDFondoUrl
                    movieDetailItemHDFondoUrl = movieDetailItem.getHDFondoUrl();
                    // read movieDetailItem.StarRating
                    movieDetailItemStarRating = movieDetailItem.getStarRating();
                    // read movieDetailItem.Director
                    movieDetailItemDirector = movieDetailItem.getDirector();
                    // read movieDetailItem.Actors
                    movieDetailItemActors = movieDetailItem.getActors();
                    // read movieDetailItem.isHDBranded
                    movieDetailItemIsHDBranded = movieDetailItem.isHDBranded();
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
                        // read movieDetailsVM::like
                        movieDetailsVMLikeAndroidViewViewOnClickListener = (((mMovieDetailsVMLikeAndroidViewViewOnClickListener == null) ? (mMovieDetailsVMLikeAndroidViewViewOnClickListener = new OnClickListenerImpl()) : mMovieDetailsVMLikeAndroidViewViewOnClickListener).setValue(movieDetailsVM));
                        // read movieDetailsVM::onClickFavorite
                        movieDetailsVMOnClickFavoriteAndroidViewViewOnClickListener = (((mMovieDetailsVMOnClickFavoriteAndroidViewViewOnClickListener == null) ? (mMovieDetailsVMOnClickFavoriteAndroidViewViewOnClickListener = new OnClickListenerImpl1()) : mMovieDetailsVMOnClickFavoriteAndroidViewViewOnClickListener).setValue(movieDetailsVM));
                        // read movieDetailsVM::finishActivity
                        movieDetailsVMFinishActivityAndroidViewViewOnClickListener = (((mMovieDetailsVMFinishActivityAndroidViewViewOnClickListener == null) ? (mMovieDetailsVMFinishActivityAndroidViewViewOnClickListener = new OnClickListenerImpl2()) : mMovieDetailsVMFinishActivityAndroidViewViewOnClickListener).setValue(movieDetailsVM));
                        // read movieDetailsVM::report
                        movieDetailsVMReportAndroidViewViewOnClickListener = (((mMovieDetailsVMReportAndroidViewViewOnClickListener == null) ? (mMovieDetailsVMReportAndroidViewViewOnClickListener = new OnClickListenerImpl3()) : mMovieDetailsVMReportAndroidViewViewOnClickListener).setValue(movieDetailsVM));
                        // read movieDetailsVM::playHD
                        movieDetailsVMPlayHDAndroidViewViewOnClickListener = (((mMovieDetailsVMPlayHDAndroidViewViewOnClickListener == null) ? (mMovieDetailsVMPlayHDAndroidViewViewOnClickListener = new OnClickListenerImpl4()) : mMovieDetailsVMPlayHDAndroidViewViewOnClickListener).setValue(movieDetailsVM));
                        // read movieDetailsVM::playSD
                        movieDetailsVMPlaySDAndroidViewViewOnClickListener = (((mMovieDetailsVMPlaySDAndroidViewViewOnClickListener == null) ? (mMovieDetailsVMPlaySDAndroidViewViewOnClickListener = new OnClickListenerImpl5()) : mMovieDetailsVMPlaySDAndroidViewViewOnClickListener).setValue(movieDetailsVM));
                        // read movieDetailsVM::dislike
                        movieDetailsVMDislikeAndroidViewViewOnClickListener = (((mMovieDetailsVMDislikeAndroidViewViewOnClickListener == null) ? (mMovieDetailsVMDislikeAndroidViewViewOnClickListener = new OnClickListenerImpl6()) : mMovieDetailsVMDislikeAndroidViewViewOnClickListener).setValue(movieDetailsVM));
                        // read movieDetailsVM::playTrailor
                        movieDetailsVMPlayTrailorAndroidViewViewOnClickListener = (((mMovieDetailsVMPlayTrailorAndroidViewViewOnClickListener == null) ? (mMovieDetailsVMPlayTrailorAndroidViewViewOnClickListener = new OnClickListenerImpl7()) : mMovieDetailsVMPlayTrailorAndroidViewViewOnClickListener).setValue(movieDetailsVM));
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
            com.uni.julio.supertv.binding.BindingAdapters.loadImage(this.fondoUrl, movieDetailItemHDFondoUrl);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView14, movieDetailItemGetReleaseDate);
            com.uni.julio.supertv.binding.BindingAdapters.setDuration(this.mboundView16, movieDetailItemGetLength);
            com.uni.julio.supertv.binding.BindingAdapters.bindShowHDIcon(this.mboundView17, movieDetailItemIsHDBranded);
            com.uni.julio.supertv.binding.BindingAdapters.setRating(this.ratingBar, movieDetailItemStarRating);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.textView5, movieDetailItemTitle);
        }
        if ((dirtyFlags & 0x180L) != 0) {
            // api target 1

            this.imageView7.setOnClickListener(movieDetailsVMFinishActivityAndroidViewViewOnClickListener);
            this.imageView8.setOnClickListener(movieDetailsVMOnClickFavoriteAndroidViewViewOnClickListener);
            this.mboundView18.setOnClickListener(movieDetailsVMLikeAndroidViewViewOnClickListener);
            this.mboundView19.setOnClickListener(movieDetailsVMDislikeAndroidViewViewOnClickListener);
            this.mboundView20.setOnClickListener(movieDetailsVMReportAndroidViewViewOnClickListener);
            this.mboundView7.setOnClickListener(movieDetailsVMPlaySDAndroidViewViewOnClickListener);
            this.mboundView9.setOnClickListener(movieDetailsVMPlayTrailorAndroidViewViewOnClickListener);
            this.play.setOnClickListener(movieDetailsVMPlayHDAndroidViewViewOnClickListener);
        }
        if ((dirtyFlags & 0x1a0L) != 0) {
            // api target 1

            com.uni.julio.supertv.binding.BindingAdapters.bindShowFavoriteIcon(this.imageView8, movieDetailsVMIsFavoriteGet);
        }
        if ((dirtyFlags & 0x190L) != 0) {
            // api target 1

            com.uni.julio.supertv.binding.BindingAdapters.bindLikeButton(this.mboundView18, movieDetailsVMLikedGet);
        }
        if ((dirtyFlags & 0x181L) != 0) {
            // api target 1

            com.uni.julio.supertv.binding.BindingAdapters.bindDislikeButton(this.mboundView19, movieDetailsVMDislikedGet);
        }
        if ((dirtyFlags & 0x182L) != 0) {
            // api target 1

            com.uni.julio.supertv.binding.BindingAdapters.bindInvisibleVisibility(this.mboundView4, movieDetailsVMIsHDGet);
        }
        if ((dirtyFlags & 0x184L) != 0) {
            // api target 1

            com.uni.julio.supertv.binding.BindingAdapters.bindInvisibleVisibility(this.mboundView6, movieDetailsVMIsSDGet);
        }
        if ((dirtyFlags & 0x188L) != 0) {
            // api target 1

            com.uni.julio.supertv.binding.BindingAdapters.bindInvisibleVisibility(this.mboundView8, movieDetailsVMIsTrailerGet);
        }
    }
    // Listener Stub Implementations
    public static class OnClickListenerImpl implements android.view.View.OnClickListener{
        private com.uni.julio.supertv.viewmodel.MovieDetailsViewModel value;
        public OnClickListenerImpl setValue(com.uni.julio.supertv.viewmodel.MovieDetailsViewModel value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.like(arg0); 
        }
    }
    public static class OnClickListenerImpl1 implements android.view.View.OnClickListener{
        private com.uni.julio.supertv.viewmodel.MovieDetailsViewModel value;
        public OnClickListenerImpl1 setValue(com.uni.julio.supertv.viewmodel.MovieDetailsViewModel value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.onClickFavorite(arg0); 
        }
    }
    public static class OnClickListenerImpl2 implements android.view.View.OnClickListener{
        private com.uni.julio.supertv.viewmodel.MovieDetailsViewModel value;
        public OnClickListenerImpl2 setValue(com.uni.julio.supertv.viewmodel.MovieDetailsViewModel value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.finishActivity(arg0); 
        }
    }
    public static class OnClickListenerImpl3 implements android.view.View.OnClickListener{
        private com.uni.julio.supertv.viewmodel.MovieDetailsViewModel value;
        public OnClickListenerImpl3 setValue(com.uni.julio.supertv.viewmodel.MovieDetailsViewModel value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.report(arg0); 
        }
    }
    public static class OnClickListenerImpl4 implements android.view.View.OnClickListener{
        private com.uni.julio.supertv.viewmodel.MovieDetailsViewModel value;
        public OnClickListenerImpl4 setValue(com.uni.julio.supertv.viewmodel.MovieDetailsViewModel value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.playHD(arg0); 
        }
    }
    public static class OnClickListenerImpl5 implements android.view.View.OnClickListener{
        private com.uni.julio.supertv.viewmodel.MovieDetailsViewModel value;
        public OnClickListenerImpl5 setValue(com.uni.julio.supertv.viewmodel.MovieDetailsViewModel value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.playSD(arg0); 
        }
    }
    public static class OnClickListenerImpl6 implements android.view.View.OnClickListener{
        private com.uni.julio.supertv.viewmodel.MovieDetailsViewModel value;
        public OnClickListenerImpl6 setValue(com.uni.julio.supertv.viewmodel.MovieDetailsViewModel value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.dislike(arg0); 
        }
    }
    public static class OnClickListenerImpl7 implements android.view.View.OnClickListener{
        private com.uni.julio.supertv.viewmodel.MovieDetailsViewModel value;
        public OnClickListenerImpl7 setValue(com.uni.julio.supertv.viewmodel.MovieDetailsViewModel value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.playTrailor(arg0); 
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