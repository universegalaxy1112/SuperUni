package com.uni.julio.supertv.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.google.android.material.appbar.AppBarLayout;
import com.uni.julio.supertv.helper.TVRecyclerView;
import com.uni.julio.supertv.viewmodel.SearchViewModel;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class ActivitySelectserverBinding extends ViewDataBinding {
  @NonNull
  public final AppBarLayout appBarLayout;

  @NonNull
  public final EditText location;

  @NonNull
  public final TVRecyclerView serverRecycler;

  @NonNull
  public final Toolbar toolbar;

  @Bindable
  protected SearchViewModel mSearchFM;

  protected ActivitySelectserverBinding(Object _bindingComponent, View _root, int _localFieldCount,
      AppBarLayout appBarLayout, EditText location, TVRecyclerView serverRecycler,
      Toolbar toolbar) {
    super(_bindingComponent, _root, _localFieldCount);
    this.appBarLayout = appBarLayout;
    this.location = location;
    this.serverRecycler = serverRecycler;
    this.toolbar = toolbar;
  }

  public abstract void setSearchFM(@Nullable SearchViewModel SearchFM);

  @Nullable
  public SearchViewModel getSearchFM() {
    return mSearchFM;
  }

  @NonNull
  public static ActivitySelectserverBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_selectserver, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static ActivitySelectserverBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<ActivitySelectserverBinding>inflateInternal(inflater, com.uni.julio.supertv.R.layout.activity_selectserver, root, attachToRoot, component);
  }

  @NonNull
  public static ActivitySelectserverBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_selectserver, null, false, component)
   */
  @NonNull
  @Deprecated
  public static ActivitySelectserverBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<ActivitySelectserverBinding>inflateInternal(inflater, com.uni.julio.supertv.R.layout.activity_selectserver, null, false, component);
  }

  public static ActivitySelectserverBinding bind(@NonNull View view) {
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
  public static ActivitySelectserverBinding bind(@NonNull View view, @Nullable Object component) {
    return (ActivitySelectserverBinding)bind(component, view, com.uni.julio.supertv.R.layout.activity_selectserver);
  }
}
