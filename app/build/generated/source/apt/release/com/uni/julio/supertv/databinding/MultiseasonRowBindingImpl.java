package com.uni.julio.supertv.databinding;
import com.uni.julio.supertv.R;
import com.uni.julio.supertv.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class MultiseasonRowBindingImpl extends MultiseasonRowBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.img, 3);
        sViewsWithIds.put(R.id.textArea, 4);
    }
    // views
    // variables
    // values
    // listeners
    private OnClickListenerImpl mMoviesAdapterOnClickItemAndroidViewViewOnClickListener;
    // Inverse Binding Event Handlers

    public MultiseasonRowBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 5, sIncludes, sViewsWithIds));
    }
    private MultiseasonRowBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.TextView) bindings[2]
            , (android.widget.LinearLayout) bindings[0]
            , (android.widget.ImageView) bindings[3]
            , (android.widget.LinearLayout) bindings[4]
            , (android.widget.TextView) bindings[1]
            );
        this.detail.setTag(null);
        this.flMainLayout.setTag(null);
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
            setMoviesAdapter((com.uni.julio.supertv.adapter.MultiSeasonAdapter) variable);
        }
        else if (BR.moviesMenuItem == variableId) {
            setMoviesMenuItem((com.uni.julio.supertv.model.Episode) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setMoviesAdapter(@Nullable com.uni.julio.supertv.adapter.MultiSeasonAdapter MoviesAdapter) {
        this.mMoviesAdapter = MoviesAdapter;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.moviesAdapter);
        super.requestRebind();
    }
    public void setMoviesMenuItem(@Nullable com.uni.julio.supertv.model.Episode MoviesMenuItem) {
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
        int moviesMenuItemPosition = 0;
        com.uni.julio.supertv.adapter.MultiSeasonAdapter moviesAdapter = mMoviesAdapter;
        com.uni.julio.supertv.model.Episode moviesMenuItem = mMoviesMenuItem;

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
                    // read moviesMenuItem.position
                    moviesMenuItemPosition = moviesMenuItem.getPosition();
                }
        }
        // batch finished
        if ((dirtyFlags & 0x6L) != 0) {
            // api target 1

            com.uni.julio.supertv.binding.BindingAdapters.setTitleEpisode(this.detail, moviesMenuItemPosition);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.title, moviesMenuItemGetTitle);
        }
        if ((dirtyFlags & 0x5L) != 0) {
            // api target 1

            this.flMainLayout.setOnClickListener(moviesAdapterOnClickItemAndroidViewViewOnClickListener);
        }
    }
    // Listener Stub Implementations
    public static class OnClickListenerImpl implements android.view.View.OnClickListener{
        private com.uni.julio.supertv.adapter.MultiSeasonAdapter value;
        public OnClickListenerImpl setValue(com.uni.julio.supertv.adapter.MultiSeasonAdapter value) {
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