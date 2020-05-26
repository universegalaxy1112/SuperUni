package com.uni.julio.supertv.databinding;
import com.uni.julio.supertv.R;
import com.uni.julio.supertv.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class VideoListSectionBindingImpl extends VideoListSectionBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.ic_more, 3);
        sViewsWithIds.put(R.id.reload, 4);
        sViewsWithIds.put(R.id.loadingBar, 5);
        sViewsWithIds.put(R.id.error_txt, 6);
        sViewsWithIds.put(R.id.recycler_view, 7);
    }
    // views
    @NonNull
    private final android.widget.LinearLayout mboundView0;
    // variables
    // values
    // listeners
    private OnClickListenerImpl mCategoryAdapterOnClickItemAndroidViewViewOnClickListener;
    // Inverse Binding Event Handlers

    public VideoListSectionBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 8, sIncludes, sViewsWithIds));
    }
    private VideoListSectionBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (androidx.constraintlayout.widget.ConstraintLayout) bindings[1]
            , (android.widget.TextView) bindings[2]
            , (android.widget.TextView) bindings[6]
            , (android.widget.TextView) bindings[3]
            , (com.wang.avi.AVLoadingIndicatorView) bindings[5]
            , (com.uni.julio.supertv.helper.TVRecyclerView) bindings[7]
            , (android.widget.ImageView) bindings[4]
            );
        this.allPaneBtn.setTag(null);
        this.categoryName.setTag(null);
        this.mboundView0 = (android.widget.LinearLayout) bindings[0];
        this.mboundView0.setTag(null);
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
        if (BR.categoryAdapter == variableId) {
            setCategoryAdapter((com.uni.julio.supertv.adapter.MoviesCategoryAdapter) variable);
        }
        else if (BR.movieCategory == variableId) {
            setMovieCategory((com.uni.julio.supertv.model.MovieCategory) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setCategoryAdapter(@Nullable com.uni.julio.supertv.adapter.MoviesCategoryAdapter CategoryAdapter) {
        this.mCategoryAdapter = CategoryAdapter;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.categoryAdapter);
        super.requestRebind();
    }
    public void setMovieCategory(@Nullable com.uni.julio.supertv.model.MovieCategory MovieCategory) {
        this.mMovieCategory = MovieCategory;
        synchronized(this) {
            mDirtyFlags |= 0x2L;
        }
        notifyPropertyChanged(BR.movieCategory);
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
        android.view.View.OnClickListener categoryAdapterOnClickItemAndroidViewViewOnClickListener = null;
        com.uni.julio.supertv.adapter.MoviesCategoryAdapter categoryAdapter = mCategoryAdapter;
        com.uni.julio.supertv.model.MovieCategory movieCategory = mMovieCategory;
        java.lang.String movieCategoryCatName = null;

        if ((dirtyFlags & 0x5L) != 0) {



                if (categoryAdapter != null) {
                    // read categoryAdapter::onClickItem
                    categoryAdapterOnClickItemAndroidViewViewOnClickListener = (((mCategoryAdapterOnClickItemAndroidViewViewOnClickListener == null) ? (mCategoryAdapterOnClickItemAndroidViewViewOnClickListener = new OnClickListenerImpl()) : mCategoryAdapterOnClickItemAndroidViewViewOnClickListener).setValue(categoryAdapter));
                }
        }
        if ((dirtyFlags & 0x6L) != 0) {



                if (movieCategory != null) {
                    // read movieCategory.catName
                    movieCategoryCatName = movieCategory.getCatName();
                }
        }
        // batch finished
        if ((dirtyFlags & 0x5L) != 0) {
            // api target 1

            this.allPaneBtn.setOnClickListener(categoryAdapterOnClickItemAndroidViewViewOnClickListener);
        }
        if ((dirtyFlags & 0x6L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.categoryName, movieCategoryCatName);
        }
    }
    // Listener Stub Implementations
    public static class OnClickListenerImpl implements android.view.View.OnClickListener{
        private com.uni.julio.supertv.adapter.MoviesCategoryAdapter value;
        public OnClickListenerImpl setValue(com.uni.julio.supertv.adapter.MoviesCategoryAdapter value) {
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
        flag 0 (0x1L): categoryAdapter
        flag 1 (0x2L): movieCategory
        flag 2 (0x3L): null
    flag mapping end*/
    //end
}