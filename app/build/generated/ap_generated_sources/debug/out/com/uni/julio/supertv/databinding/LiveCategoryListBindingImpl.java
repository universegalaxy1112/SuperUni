package com.uni.julio.supertv.databinding;
import com.uni.julio.supertv.R;
import com.uni.julio.supertv.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class LiveCategoryListBindingImpl extends LiveCategoryListBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.channel_title, 3);
        sViewsWithIds.put(R.id.total_channel, 4);
        sViewsWithIds.put(R.id.next_program, 5);
    }
    // views
    // variables
    // values
    // listeners
    private OnClickListenerImpl mLiveCategoryAdapterOnClickItemAndroidViewViewOnClickListener;
    // Inverse Binding Event Handlers

    public LiveCategoryListBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 6, sIncludes, sViewsWithIds));
    }
    private LiveCategoryListBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.LinearLayout) bindings[3]
            , (android.widget.TextView) bindings[1]
            , (android.widget.LinearLayout) bindings[0]
            , (android.widget.LinearLayout) bindings[5]
            , (android.widget.LinearLayout) bindings[4]
            , (android.widget.TextView) bindings[2]
            );
        this.channelTitleText.setTag(null);
        this.flMainLayout.setTag(null);
        this.totalChannelText.setTag(null);
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
        if (BR.liveCategoryAdapter == variableId) {
            setLiveCategoryAdapter((com.uni.julio.supertv.adapter.LiveCategoryAdapter) variable);
        }
        else if (BR.liveCategory == variableId) {
            setLiveCategory((com.uni.julio.supertv.model.LiveTVCategory) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setLiveCategoryAdapter(@Nullable com.uni.julio.supertv.adapter.LiveCategoryAdapter LiveCategoryAdapter) {
        this.mLiveCategoryAdapter = LiveCategoryAdapter;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.liveCategoryAdapter);
        super.requestRebind();
    }
    public void setLiveCategory(@Nullable com.uni.julio.supertv.model.LiveTVCategory LiveCategory) {
        this.mLiveCategory = LiveCategory;
        synchronized(this) {
            mDirtyFlags |= 0x2L;
        }
        notifyPropertyChanged(BR.liveCategory);
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
        com.uni.julio.supertv.adapter.LiveCategoryAdapter liveCategoryAdapter = mLiveCategoryAdapter;
        android.view.View.OnClickListener liveCategoryAdapterOnClickItemAndroidViewViewOnClickListener = null;
        java.lang.String liveCategoryTotalChannels = null;
        java.lang.String liveCategoryCatName = null;
        com.uni.julio.supertv.model.LiveTVCategory liveCategory = mLiveCategory;

        if ((dirtyFlags & 0x5L) != 0) {



                if (liveCategoryAdapter != null) {
                    // read liveCategoryAdapter::onClickItem
                    liveCategoryAdapterOnClickItemAndroidViewViewOnClickListener = (((mLiveCategoryAdapterOnClickItemAndroidViewViewOnClickListener == null) ? (mLiveCategoryAdapterOnClickItemAndroidViewViewOnClickListener = new OnClickListenerImpl()) : mLiveCategoryAdapterOnClickItemAndroidViewViewOnClickListener).setValue(liveCategoryAdapter));
                }
        }
        if ((dirtyFlags & 0x6L) != 0) {



                if (liveCategory != null) {
                    // read liveCategory.totalChannels
                    liveCategoryTotalChannels = liveCategory.getTotalChannels();
                    // read liveCategory.catName
                    liveCategoryCatName = liveCategory.getCatName();
                }
        }
        // batch finished
        if ((dirtyFlags & 0x6L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.channelTitleText, liveCategoryCatName);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.totalChannelText, liveCategoryTotalChannels);
        }
        if ((dirtyFlags & 0x5L) != 0) {
            // api target 1

            this.flMainLayout.setOnClickListener(liveCategoryAdapterOnClickItemAndroidViewViewOnClickListener);
        }
    }
    // Listener Stub Implementations
    public static class OnClickListenerImpl implements android.view.View.OnClickListener{
        private com.uni.julio.supertv.adapter.LiveCategoryAdapter value;
        public OnClickListenerImpl setValue(com.uni.julio.supertv.adapter.LiveCategoryAdapter value) {
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
        flag 0 (0x1L): liveCategoryAdapter
        flag 1 (0x2L): liveCategory
        flag 2 (0x3L): null
    flag mapping end*/
    //end
}