package com.uni.julio.supertv.databinding;
import com.uni.julio.supertv.R;
import com.uni.julio.supertv.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class LivetvnewListBindingImpl extends LivetvnewListBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.channel_title, 4);
        sViewsWithIds.put(R.id.now_playing, 5);
        sViewsWithIds.put(R.id.next_program, 6);
    }
    // views
    // variables
    // values
    // listeners
    private OnClickListenerImpl mLivetvAdapterOnClickAndroidViewViewOnClickListener;
    // Inverse Binding Event Handlers

    public LivetvnewListBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 7, sIncludes, sViewsWithIds));
    }
    private LivetvnewListBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.LinearLayout) bindings[4]
            , (android.widget.TextView) bindings[1]
            , (android.widget.LinearLayout) bindings[0]
            , (android.widget.LinearLayout) bindings[6]
            , (android.widget.TextView) bindings[3]
            , (android.widget.LinearLayout) bindings[5]
            , (android.widget.TextView) bindings[2]
            );
        this.channelTitleText.setTag(null);
        this.flMainLayout.setTag(null);
        this.nextProgramText.setTag(null);
        this.nowPlayingText.setTag(null);
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
        if (BR.livetvAdapter == variableId) {
            setLivetvAdapter((com.uni.julio.supertv.adapter.LivetvAdapterNew) variable);
        }
        else if (BR.liveProgramItem == variableId) {
            setLiveProgramItem((com.uni.julio.supertv.model.LiveProgram) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setLivetvAdapter(@Nullable com.uni.julio.supertv.adapter.LivetvAdapterNew LivetvAdapter) {
        this.mLivetvAdapter = LivetvAdapter;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.livetvAdapter);
        super.requestRebind();
    }
    public void setLiveProgramItem(@Nullable com.uni.julio.supertv.model.LiveProgram LiveProgramItem) {
        this.mLiveProgramItem = LiveProgramItem;
        synchronized(this) {
            mDirtyFlags |= 0x2L;
        }
        notifyPropertyChanged(BR.liveProgramItem);
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
        android.view.View.OnClickListener livetvAdapterOnClickAndroidViewViewOnClickListener = null;
        java.lang.String liveProgramItemEpgDespues = null;
        java.lang.String liveProgramItemEpgAhora = null;
        java.lang.String liveProgramItemTitle = null;
        com.uni.julio.supertv.adapter.LivetvAdapterNew livetvAdapter = mLivetvAdapter;
        com.uni.julio.supertv.model.LiveProgram liveProgramItem = mLiveProgramItem;

        if ((dirtyFlags & 0x5L) != 0) {



                if (livetvAdapter != null) {
                    // read livetvAdapter::onClick
                    livetvAdapterOnClickAndroidViewViewOnClickListener = (((mLivetvAdapterOnClickAndroidViewViewOnClickListener == null) ? (mLivetvAdapterOnClickAndroidViewViewOnClickListener = new OnClickListenerImpl()) : mLivetvAdapterOnClickAndroidViewViewOnClickListener).setValue(livetvAdapter));
                }
        }
        if ((dirtyFlags & 0x6L) != 0) {



                if (liveProgramItem != null) {
                    // read liveProgramItem.epg_despues
                    liveProgramItemEpgDespues = liveProgramItem.getEpg_despues();
                    // read liveProgramItem.epg_ahora
                    liveProgramItemEpgAhora = liveProgramItem.getEpg_ahora();
                    // read liveProgramItem.title
                    liveProgramItemTitle = liveProgramItem.getTitle();
                }
        }
        // batch finished
        if ((dirtyFlags & 0x6L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.channelTitleText, liveProgramItemTitle);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.nextProgramText, liveProgramItemEpgDespues);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.nowPlayingText, liveProgramItemEpgAhora);
        }
        if ((dirtyFlags & 0x5L) != 0) {
            // api target 1

            this.flMainLayout.setOnClickListener(livetvAdapterOnClickAndroidViewViewOnClickListener);
        }
    }
    // Listener Stub Implementations
    public static class OnClickListenerImpl implements android.view.View.OnClickListener{
        private com.uni.julio.supertv.adapter.LivetvAdapterNew value;
        public OnClickListenerImpl setValue(com.uni.julio.supertv.adapter.LivetvAdapterNew value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.onClick(arg0); 
        }
    }
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): livetvAdapter
        flag 1 (0x2L): liveProgramItem
        flag 2 (0x3L): null
    flag mapping end*/
    //end
}