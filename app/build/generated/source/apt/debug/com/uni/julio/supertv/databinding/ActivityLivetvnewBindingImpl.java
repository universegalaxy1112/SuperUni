package com.uni.julio.supertv.databinding;
import com.uni.julio.supertv.R;
import com.uni.julio.supertv.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityLivetvnewBindingImpl extends ActivityLivetvnewBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.exo_player_virtual, 7);
        sViewsWithIds.put(R.id.logo, 8);
        sViewsWithIds.put(R.id.guide, 9);
        sViewsWithIds.put(R.id.programming_recycler, 10);
        sViewsWithIds.put(R.id.live_category_recycler, 11);
        sViewsWithIds.put(R.id.exo_player, 12);
    }
    // views
    @NonNull
    private final android.widget.TextView mboundView5;
    // variables
    // values
    // listeners
    private OnClickListenerImpl mLiveTVFragmentVMShowCategoriesAndroidViewViewOnClickListener;
    private OnClickListenerImpl1 mLiveTVFragmentVMToggleFullscreenAndroidViewViewOnClickListener;
    // Inverse Binding Event Handlers

    public ActivityLivetvnewBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 13, sIncludes, sViewsWithIds));
    }
    private ActivityLivetvnewBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.TextView) bindings[4]
            , (android.widget.LinearLayout) bindings[12]
            , (android.widget.LinearLayout) bindings[7]
            , (android.widget.LinearLayout) bindings[9]
            , (com.uni.julio.supertv.helper.TVRecyclerView) bindings[11]
            , (android.widget.LinearLayout) bindings[8]
            , (de.hdodenhof.circleimageview.CircleImageView) bindings[1]
            , (android.widget.FrameLayout) bindings[0]
            , (android.widget.LinearLayout) bindings[6]
            , (com.uni.julio.supertv.helper.TVRecyclerView) bindings[10]
            , (android.widget.TextView) bindings[3]
            , (android.widget.TextView) bindings[2]
            );
        this.epg.setTag(null);
        this.markImg.setTag(null);
        this.mboundView5 = (android.widget.TextView) bindings[5];
        this.mboundView5.setTag(null);
        this.parent.setTag(null);
        this.playerContainer.setTag(null);
        this.subTitle.setTag(null);
        this.title.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x8L;
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
        else if (BR.currentCategory == variableId) {
            setCurrentCategory((com.uni.julio.supertv.model.LiveTVCategory) variable);
        }
        else if (BR.currentProgram == variableId) {
            setCurrentProgram((com.uni.julio.supertv.model.LiveProgram) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setLiveTVFragmentVM(@Nullable com.uni.julio.supertv.viewmodel.LiveTVViewModel LiveTVFragmentVM) {
        this.mLiveTVFragmentVM = LiveTVFragmentVM;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.liveTVFragmentVM);
        super.requestRebind();
    }
    public void setCurrentCategory(@Nullable com.uni.julio.supertv.model.LiveTVCategory CurrentCategory) {
        this.mCurrentCategory = CurrentCategory;
        synchronized(this) {
            mDirtyFlags |= 0x2L;
        }
        notifyPropertyChanged(BR.currentCategory);
        super.requestRebind();
    }
    public void setCurrentProgram(@Nullable com.uni.julio.supertv.model.LiveProgram CurrentProgram) {
        this.mCurrentProgram = CurrentProgram;
        synchronized(this) {
            mDirtyFlags |= 0x4L;
        }
        notifyPropertyChanged(BR.currentProgram);
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
        com.uni.julio.supertv.viewmodel.LiveTVViewModel liveTVFragmentVM = mLiveTVFragmentVM;
        com.uni.julio.supertv.model.LiveTVCategory currentCategory = mCurrentCategory;
        com.uni.julio.supertv.model.LiveProgram currentProgram = mCurrentProgram;
        java.lang.String currentProgramTitle = null;
        java.lang.String currentProgramDescription = null;
        android.view.View.OnClickListener liveTVFragmentVMShowCategoriesAndroidViewViewOnClickListener = null;
        java.lang.String currentProgramSubTitle = null;
        java.lang.String currentProgramIconUrl = null;
        android.view.View.OnClickListener liveTVFragmentVMToggleFullscreenAndroidViewViewOnClickListener = null;
        java.lang.String currentCategoryCatName = null;

        if ((dirtyFlags & 0x9L) != 0) {



                if (liveTVFragmentVM != null) {
                    // read liveTVFragmentVM::showCategories
                    liveTVFragmentVMShowCategoriesAndroidViewViewOnClickListener = (((mLiveTVFragmentVMShowCategoriesAndroidViewViewOnClickListener == null) ? (mLiveTVFragmentVMShowCategoriesAndroidViewViewOnClickListener = new OnClickListenerImpl()) : mLiveTVFragmentVMShowCategoriesAndroidViewViewOnClickListener).setValue(liveTVFragmentVM));
                    // read liveTVFragmentVM::toggleFullscreen
                    liveTVFragmentVMToggleFullscreenAndroidViewViewOnClickListener = (((mLiveTVFragmentVMToggleFullscreenAndroidViewViewOnClickListener == null) ? (mLiveTVFragmentVMToggleFullscreenAndroidViewViewOnClickListener = new OnClickListenerImpl1()) : mLiveTVFragmentVMToggleFullscreenAndroidViewViewOnClickListener).setValue(liveTVFragmentVM));
                }
        }
        if ((dirtyFlags & 0xaL) != 0) {



                if (currentCategory != null) {
                    // read currentCategory.catName
                    currentCategoryCatName = currentCategory.getCatName();
                }
        }
        if ((dirtyFlags & 0xcL) != 0) {



                if (currentProgram != null) {
                    // read currentProgram.title
                    currentProgramTitle = currentProgram.getTitle();
                    // read currentProgram.description
                    currentProgramDescription = currentProgram.getDescription();
                    // read currentProgram.sub_title
                    currentProgramSubTitle = currentProgram.getSub_title();
                    // read currentProgram.iconUrl
                    currentProgramIconUrl = currentProgram.getIconUrl();
                }
        }
        // batch finished
        if ((dirtyFlags & 0xcL) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.epg, currentProgramDescription);
            com.uni.julio.supertv.binding.BindingAdapters.loadImage(this.markImg, currentProgramIconUrl);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.subTitle, currentProgramSubTitle);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.title, currentProgramTitle);
        }
        if ((dirtyFlags & 0xaL) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView5, currentCategoryCatName);
        }
        if ((dirtyFlags & 0x9L) != 0) {
            // api target 1

            this.mboundView5.setOnClickListener(liveTVFragmentVMShowCategoriesAndroidViewViewOnClickListener);
            this.playerContainer.setOnClickListener(liveTVFragmentVMToggleFullscreenAndroidViewViewOnClickListener);
        }
    }
    // Listener Stub Implementations
    public static class OnClickListenerImpl implements android.view.View.OnClickListener{
        private com.uni.julio.supertv.viewmodel.LiveTVViewModel value;
        public OnClickListenerImpl setValue(com.uni.julio.supertv.viewmodel.LiveTVViewModel value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.showCategories(arg0); 
        }
    }
    public static class OnClickListenerImpl1 implements android.view.View.OnClickListener{
        private com.uni.julio.supertv.viewmodel.LiveTVViewModel value;
        public OnClickListenerImpl1 setValue(com.uni.julio.supertv.viewmodel.LiveTVViewModel value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.toggleFullscreen(arg0); 
        }
    }
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): liveTVFragmentVM
        flag 1 (0x2L): currentCategory
        flag 2 (0x3L): currentProgram
        flag 3 (0x4L): null
    flag mapping end*/
    //end
}