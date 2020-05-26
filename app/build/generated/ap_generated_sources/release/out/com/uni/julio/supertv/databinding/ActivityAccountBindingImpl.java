package com.uni.julio.supertv.databinding;
import com.uni.julio.supertv.R;
import com.uni.julio.supertv.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityAccountBindingImpl extends ActivityAccountBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.Appbarlayout, 7);
        sViewsWithIds.put(R.id.toolbar, 8);
        sViewsWithIds.put(R.id.splash_main_container, 9);
        sViewsWithIds.put(R.id.device0, 10);
        sViewsWithIds.put(R.id.device1, 11);
        sViewsWithIds.put(R.id.device2, 12);
        sViewsWithIds.put(R.id.testspeed, 13);
    }
    // views
    @NonNull
    private final android.widget.TextView mboundView3;
    @NonNull
    private final android.widget.TextView mboundView4;
    @NonNull
    private final android.widget.TextView mboundView5;
    // variables
    // values
    // listeners
    private OnClickListenerImpl mAccountDetailsVMOnCloseSessionAndroidViewViewOnClickListener;
    // Inverse Binding Event Handlers

    public ActivityAccountBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 14, sIncludes, sViewsWithIds));
    }
    private ActivityAccountBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 1
            , (com.google.android.material.appbar.AppBarLayout) bindings[7]
            , (android.widget.LinearLayout) bindings[2]
            , (android.widget.TextView) bindings[6]
            , (android.widget.TextView) bindings[10]
            , (android.widget.TextView) bindings[11]
            , (android.widget.TextView) bindings[12]
            , (android.widget.LinearLayout) bindings[0]
            , (android.widget.ProgressBar) bindings[1]
            , (android.widget.LinearLayout) bindings[9]
            , (android.widget.TextView) bindings[13]
            , (androidx.appcompat.widget.Toolbar) bindings[8]
            );
        this.accountDetailsContent.setTag(null);
        this.cvLoginScreenLogin.setTag(null);
        this.mainLayout.setTag(null);
        this.mboundView3 = (android.widget.TextView) bindings[3];
        this.mboundView3.setTag(null);
        this.mboundView4 = (android.widget.TextView) bindings[4];
        this.mboundView4.setTag(null);
        this.mboundView5 = (android.widget.TextView) bindings[5];
        this.mboundView5.setTag(null);
        this.progressBar.setTag(null);
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
        if (BR.accountDetailsVM == variableId) {
            setAccountDetailsVM((com.uni.julio.supertv.viewmodel.AccountDetailsViewModel) variable);
        }
        else if (BR.user == variableId) {
            setUser((com.uni.julio.supertv.model.User) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setAccountDetailsVM(@Nullable com.uni.julio.supertv.viewmodel.AccountDetailsViewModel AccountDetailsVM) {
        this.mAccountDetailsVM = AccountDetailsVM;
        synchronized(this) {
            mDirtyFlags |= 0x2L;
        }
        notifyPropertyChanged(BR.accountDetailsVM);
        super.requestRebind();
    }
    public void setUser(@Nullable com.uni.julio.supertv.model.User User) {
        this.mUser = User;
        synchronized(this) {
            mDirtyFlags |= 0x4L;
        }
        notifyPropertyChanged(BR.user);
        super.requestRebind();
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeAccountDetailsVMIsLoading((androidx.databinding.ObservableBoolean) object, fieldId);
        }
        return false;
    }
    private boolean onChangeAccountDetailsVMIsLoading(androidx.databinding.ObservableBoolean AccountDetailsVMIsLoading, int fieldId) {
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
        com.uni.julio.supertv.viewmodel.AccountDetailsViewModel accountDetailsVM = mAccountDetailsVM;
        boolean accountDetailsVMIsLoading = false;
        com.uni.julio.supertv.model.User user = mUser;
        boolean accountDetailsVMIsLoadingGet = false;
        java.lang.String userVersion = null;
        java.lang.String userName = null;
        androidx.databinding.ObservableBoolean AccountDetailsVMIsLoading1 = null;
        java.lang.String userExpirationDate = null;
        android.view.View.OnClickListener accountDetailsVMOnCloseSessionAndroidViewViewOnClickListener = null;

        if ((dirtyFlags & 0xbL) != 0) {



                if (accountDetailsVM != null) {
                    // read accountDetailsVM.isLoading
                    AccountDetailsVMIsLoading1 = accountDetailsVM.isLoading;
                }
                updateRegistration(0, AccountDetailsVMIsLoading1);


                if (AccountDetailsVMIsLoading1 != null) {
                    // read accountDetailsVM.isLoading.get()
                    accountDetailsVMIsLoadingGet = AccountDetailsVMIsLoading1.get();
                }


                // read !accountDetailsVM.isLoading.get()
                accountDetailsVMIsLoading = !accountDetailsVMIsLoadingGet;
            if ((dirtyFlags & 0xaL) != 0) {

                    if (accountDetailsVM != null) {
                        // read accountDetailsVM::onCloseSession
                        accountDetailsVMOnCloseSessionAndroidViewViewOnClickListener = (((mAccountDetailsVMOnCloseSessionAndroidViewViewOnClickListener == null) ? (mAccountDetailsVMOnCloseSessionAndroidViewViewOnClickListener = new OnClickListenerImpl()) : mAccountDetailsVMOnCloseSessionAndroidViewViewOnClickListener).setValue(accountDetailsVM));
                    }
            }
        }
        if ((dirtyFlags & 0xcL) != 0) {



                if (user != null) {
                    // read user.version
                    userVersion = user.getVersion();
                    // read user.name
                    userName = user.getName();
                    // read user.expiration_date
                    userExpirationDate = user.getExpiration_date();
                }
        }
        // batch finished
        if ((dirtyFlags & 0xbL) != 0) {
            // api target 1

            com.uni.julio.supertv.binding.BindingAdapters.bindHiddenVisibility(this.accountDetailsContent, accountDetailsVMIsLoadingGet);
            com.uni.julio.supertv.binding.BindingAdapters.bindHiddenVisibility(this.progressBar, accountDetailsVMIsLoading);
        }
        if ((dirtyFlags & 0xaL) != 0) {
            // api target 1

            this.cvLoginScreenLogin.setOnClickListener(accountDetailsVMOnCloseSessionAndroidViewViewOnClickListener);
        }
        if ((dirtyFlags & 0xcL) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView3, userName);
            com.uni.julio.supertv.binding.BindingAdapters.setDate(this.mboundView4, userExpirationDate);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView5, userVersion);
        }
    }
    // Listener Stub Implementations
    public static class OnClickListenerImpl implements android.view.View.OnClickListener{
        private com.uni.julio.supertv.viewmodel.AccountDetailsViewModel value;
        public OnClickListenerImpl setValue(com.uni.julio.supertv.viewmodel.AccountDetailsViewModel value) {
            this.value = value;
            return value == null ? null : this;
        }
        @Override
        public void onClick(android.view.View arg0) {
            this.value.onCloseSession(arg0); 
        }
    }
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): accountDetailsVM.isLoading
        flag 1 (0x2L): accountDetailsVM
        flag 2 (0x3L): user
        flag 3 (0x4L): null
    flag mapping end*/
    //end
}