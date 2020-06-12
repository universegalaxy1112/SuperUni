package com.uni.julio.supertv.databinding;
import com.uni.julio.supertv.R;
import com.uni.julio.supertv.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityLiveBindingImpl extends ActivityLiveBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.exo_player, 1);
        sViewsWithIds.put(R.id.live_category_tab, 2);
        sViewsWithIds.put(R.id.category_tab, 3);
        sViewsWithIds.put(R.id.live_programs, 4);
        sViewsWithIds.put(R.id.programming_recycler, 5);
    }
    // views
    @NonNull
    private final android.widget.FrameLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityLiveBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 6, sIncludes, sViewsWithIds));
    }
    private ActivityLiveBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (com.google.android.material.tabs.TabLayout) bindings[3]
            , (android.widget.LinearLayout) bindings[1]
            , (android.widget.LinearLayout) bindings[2]
            , (android.widget.LinearLayout) bindings[4]
            , (com.uni.julio.supertv.helper.TVRecyclerView) bindings[5]
            );
        this.mboundView0 = (android.widget.FrameLayout) bindings[0];
        this.mboundView0.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x2L;
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
        if (BR.liveTVFragmentVM == variableId) {
            setLiveTVFragmentVM((com.uni.julio.supertv.viewmodel.LiveTVViewModel) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setLiveTVFragmentVM(@Nullable com.uni.julio.supertv.viewmodel.LiveTVViewModel LiveTVFragmentVM) {
        this.mLiveTVFragmentVM = LiveTVFragmentVM;
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
        // batch finished
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): liveTVFragmentVM
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}