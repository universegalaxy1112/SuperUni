package com.uni.julio.supertv.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.google.android.material.appbar.AppBarLayout;
import com.uni.julio.supertv.model.User;
import com.uni.julio.supertv.viewmodel.AccountDetailsViewModel;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class ActivityAccountBinding extends ViewDataBinding {
  @NonNull
  public final AppBarLayout Appbarlayout;

  @NonNull
  public final LinearLayout accountDetailsContent;

  @NonNull
  public final TextView cvLoginScreenLogin;

  @NonNull
  public final TextView device0;

  @NonNull
  public final TextView device1;

  @NonNull
  public final TextView device2;

  @NonNull
  public final LinearLayout mainLayout;

  @NonNull
  public final ProgressBar progressBar;

  @NonNull
  public final LinearLayout splashMainContainer;

  @NonNull
  public final TextView testspeed;

  @NonNull
  public final Toolbar toolbar;

  @Bindable
  protected AccountDetailsViewModel mAccountDetailsVM;

  @Bindable
  protected User mUser;

  protected ActivityAccountBinding(Object _bindingComponent, View _root, int _localFieldCount,
      AppBarLayout Appbarlayout, LinearLayout accountDetailsContent, TextView cvLoginScreenLogin,
      TextView device0, TextView device1, TextView device2, LinearLayout mainLayout,
      ProgressBar progressBar, LinearLayout splashMainContainer, TextView testspeed,
      Toolbar toolbar) {
    super(_bindingComponent, _root, _localFieldCount);
    this.Appbarlayout = Appbarlayout;
    this.accountDetailsContent = accountDetailsContent;
    this.cvLoginScreenLogin = cvLoginScreenLogin;
    this.device0 = device0;
    this.device1 = device1;
    this.device2 = device2;
    this.mainLayout = mainLayout;
    this.progressBar = progressBar;
    this.splashMainContainer = splashMainContainer;
    this.testspeed = testspeed;
    this.toolbar = toolbar;
  }

  public abstract void setAccountDetailsVM(@Nullable AccountDetailsViewModel accountDetailsVM);

  @Nullable
  public AccountDetailsViewModel getAccountDetailsVM() {
    return mAccountDetailsVM;
  }

  public abstract void setUser(@Nullable User user);

  @Nullable
  public User getUser() {
    return mUser;
  }

  @NonNull
  public static ActivityAccountBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_account, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static ActivityAccountBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<ActivityAccountBinding>inflateInternal(inflater, com.uni.julio.supertv.R.layout.activity_account, root, attachToRoot, component);
  }

  @NonNull
  public static ActivityAccountBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_account, null, false, component)
   */
  @NonNull
  @Deprecated
  public static ActivityAccountBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<ActivityAccountBinding>inflateInternal(inflater, com.uni.julio.supertv.R.layout.activity_account, null, false, component);
  }

  public static ActivityAccountBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.bind(view, component)
   */
  @Deprecated
  public static ActivityAccountBinding bind(@NonNull View view, @Nullable Object component) {
    return (ActivityAccountBinding)bind(component, view, com.uni.julio.supertv.R.layout.activity_account);
  }
}
