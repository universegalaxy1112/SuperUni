package com.uni.julio.supertv.databinding;
import com.uni.julio.supertv.R;
import com.uni.julio.supertv.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class VideoListRowBindingImpl extends VideoListRowBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = null;
    }
    // views
    // variables
    // values
    // listeners
    private OnClickListenerImpl mMoviesAdapterOnClickItemAndroidViewViewOnClickListener;
    // Inverse Binding Event Handlers

    public VideoListRowBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 3, sIncludes, sViewsWithIds));
    }
    private VideoListRowBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.LinearLayout) bindings[0]
            , (android.widget.ImageView) bindings[1]
            , (android.widget.TextView) bindings[2]
            );
        this.flMainLayout.setTag(null);
        this.img.setTag(null);
        this.title.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x4L;
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
        if (BR.moviesAdapter == variableId) {
            setMoviesAdapter((com.uni.julio.supertv.adapter.MoviesRecyclerAdapter) variable);
        }
        else if (BR.moviesMenuItem == variableId) {
            setMoviesMenuItem((com.uni.julio.supertv.model.Movie) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setMoviesAdapter(@Nullable com.uni.julio.supertv.adapter.MoviesRecyclerAdapter MoviesAdapter) {
        this.mMoviesAdapter = MoviesAdapter;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.moviesAdapter);
        super.requestRebind();
    }
    public void setMoviesMenuItem(@Nullable com.uni.julio.supertv.model.Movie MoviesMenuItem) {
        this.mMoviesMenuItem = MoviesMenuItem;
        synchronized(this) {
            mDirtyFlags |= 0x2L;
        }
        notifyPropertyChanged(BR.moviesMenuItem);
        super.requestRebind();
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
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
        android.view.View.OnClickListener moviesAdapterOnClickItemAndroidViewViewOnClickListener = null;
        java.lang.String moviesMenuItemGetTitle = null;
        com.uni.julio.supertv.adapter.MoviesRecyclerAdapter moviesAdapter = mMoviesAdapter;
        java.lang.String moviesMenuItemHDPosterUrl = null;
        com.uni.julio.supertv.model.Movie moviesMenuItem = mMoviesMenuItem;

        if ((dirtyFlags & 0x5L) != 0) {



                if (moviesAdapter != null) {
                    // read moviesAdapter::onClickItem
                    moviesAdapterOnClickItemAndroidViewViewOnClickListener = (((mMoviesAdapterOnClickItemAndroidViewViewOnClickListener == null) ? (mMoviesAdapterOnClickItemAndroidViewViewOnClickListener = new OnClickListenerImpl()) : mMoviesAdapterOnClickItemAndroidViewViewOnClickListener).setValue(moviesAdapter));
                }
        }
        if ((dirtyFlags & 0x6L) != 0) {



                if (moviesMenuItem != null) {
                    // read moviesMenuItem.getTitle
                    moviesMenuItemGetTitle = moviesMenuItem.getTitle();
                    // read moviesMenuItem.HDPosterUrl
                    moviesMenuItemHDPosterUrl = moviesMenuItem.getHDPosterUrl();
                }
        }
        // batch finished
        if ((dirtyFlags & 0x5L) != 0) {
            // api target 1

            this.flMainLayout.setOnClickListener(moviesAdapterOnClickItemAndroidViewViewOnClickListener);
        }
        if ((dirtyFlags & 0x6L) != 0) {
            // api target 1

            com.uni.julio.supertv.binding.BindingAdapters.loadImage(this.img, moviesMenuItemHDPosterUrl);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.title, moviesMenuItemGetTitle);
        }
    }
    // Listener Stub Implementations
    public static class OnClickListenerImpl implements android.view.View.OnClickListener{
        private com.uni.julio.supertv.adapter.MoviesRecyclerAdapter value;
        public OnClickListenerImpl setValue(com.uni.julio.supertv.adapter.MoviesRecyclerAdapter value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.onClickItem(arg0); 
        }
    }
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): moviesAdapter
        flag 1 (0x2L): moviesMenuItem
        flag 2 (0x3L): null
    flag mapping end*/
    //end
}