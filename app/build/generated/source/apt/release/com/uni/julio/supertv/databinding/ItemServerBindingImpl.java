package com.uni.julio.supertv.databinding;
import com.uni.julio.supertv.R;
import com.uni.julio.supertv.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ItemServerBindingImpl extends ItemServerBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.serverName, 1);
        sViewsWithIds.put(R.id.serverLocation, 2);
        sViewsWithIds.put(R.id.distance, 3);
    }
    // views
    // variables
    // values
    // listeners
    private OnClickListenerImpl mServerAdapterOnClickItemAndroidViewViewOnClickListener;
    // Inverse Binding Event Handlers

    public ItemServerBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 4, sIncludes, sViewsWithIds));
    }
    private ItemServerBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.TextView) bindings[3]
            , (android.widget.LinearLayout) bindings[0]
            , (android.widget.TextView) bindings[2]
            , (android.widget.TextView) bindings[1]
            );
        this.flMainLayout.setTag(null);
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
        if (BR.serverAdapter == variableId) {
            setServerAdapter((com.uni.julio.supertv.adapter.ServerAdapter) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setServerAdapter(@Nullable com.uni.julio.supertv.adapter.ServerAdapter ServerAdapter) {
        this.mServerAdapter = ServerAdapter;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.serverAdapter);
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
        android.view.View.OnClickListener serverAdapterOnClickItemAndroidViewViewOnClickListener = null;
        com.uni.julio.supertv.adapter.ServerAdapter serverAdapter = mServerAdapter;

        if ((dirtyFlags & 0x3L) != 0) {



                if (serverAdapter != null) {
                    // read serverAdapter::onClickItem
                    serverAdapterOnClickItemAndroidViewViewOnClickListener = (((mServerAdapterOnClickItemAndroidViewViewOnClickListener == null) ? (mServerAdapterOnClickItemAndroidViewViewOnClickListener = new OnClickListenerImpl()) : mServerAdapterOnClickItemAndroidViewViewOnClickListener).setValue(serverAdapter));
                }
        }
        // batch finished
        if ((dirtyFlags & 0x3L) != 0) {
            // api target 1

            this.flMainLayout.setOnClickListener(serverAdapterOnClickItemAndroidViewViewOnClickListener);
        }
    }
    // Listener Stub Implementations
    public static class OnClickListenerImpl implements android.view.View.OnClickListener{
        private com.uni.julio.supertv.adapter.ServerAdapter value;
        public OnClickListenerImpl setValue(com.uni.julio.supertv.adapter.ServerAdapter value) {
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
        flag 0 (0x1L): serverAdapter
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}