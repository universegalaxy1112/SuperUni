package com.uni.julio.supertv.databinding;
import com.uni.julio.supertv.R;
import com.uni.julio.supertv.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivitySearchBindingImpl extends ActivitySearchBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.appBarLayout, 3);
        sViewsWithIds.put(R.id.toolbar, 4);
        sViewsWithIds.put(R.id.editPassword, 5);
        sViewsWithIds.put(R.id.noResult, 6);
    }
    // views
    @NonNull
    private final android.widget.LinearLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivitySearchBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 7, sIncludes, sViewsWithIds));
    }
    private ActivitySearchBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 1
            , (com.google.android.material.appbar.AppBarLayout) bindings[3]
            , (android.widget.EditText) bindings[5]
            , (android.widget.TextView) bindings[6]
            , (android.widget.ProgressBar) bindings[2]
            , (com.uni.julio.supertv.helper.TVRecyclerView) bindings[1]
            , (androidx.appcompat.widget.Toolbar) bindings[4]
            );
        this.mboundView0 = (android.widget.LinearLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.progressBar.setTag(null);
        this.searchRecycler.setTag(null);
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
        if (BR.SearchFM == variableId) {
            setSearchFM((com.uni.julio.supertv.viewmodel.SearchViewModel) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setSearchFM(@Nullable com.uni.julio.supertv.viewmodel.SearchViewModel SearchFM) {
        this.mSearchFM = SearchFM;
        synchronized(this) {
            mDirtyFlags |= 0x2L;
        }
        notifyPropertyChanged(BR.SearchFM);
        super.requestRebind();
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeSearchFMIsLoading((androidx.databinding.ObservableBoolean) object, fieldId);
        }
        return false;
    }
    private boolean onChangeSearchFMIsLoading(androidx.databinding.ObservableBoolean SearchFMIsLoading, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1L;
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
        androidx.databinding.ObservableBoolean searchFMIsLoading = null;
        boolean SearchFMIsLoading1 = false;
        boolean searchFMIsLoadingGet = false;
        com.uni.julio.supertv.viewmodel.SearchViewModel searchFM = mSearchFM;

        if ((dirtyFlags & 0x7L) != 0) {



                if (searchFM != null) {
                    // read SearchFM.isLoading
                    searchFMIsLoading = searchFM.isLoading;
                }
                updateRegistration(0, searchFMIsLoading);


                if (searchFMIsLoading != null) {
                    // read SearchFM.isLoading.get()
                    searchFMIsLoadingGet = searchFMIsLoading.get();
                }


                // read !SearchFM.isLoading.get()
                SearchFMIsLoading1 = !searchFMIsLoadingGet;
        }
        // batch finished
        if ((dirtyFlags & 0x7L) != 0) {
            // api target 1

            com.uni.julio.supertv.binding.BindingAdapters.bindHiddenVisibility(this.progressBar, SearchFMIsLoading1);
            com.uni.julio.supertv.binding.BindingAdapters.bindHiddenVisibility(this.searchRecycler, searchFMIsLoadingGet);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): SearchFM.isLoading
        flag 1 (0x2L): SearchFM
        flag 2 (0x3L): null
    flag mapping end*/
    //end
}