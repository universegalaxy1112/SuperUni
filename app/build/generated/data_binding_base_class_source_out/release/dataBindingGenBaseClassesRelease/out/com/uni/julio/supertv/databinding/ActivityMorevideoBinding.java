package com.uni.julio.supertv.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.google.android.material.appbar.AppBarLayout;
import com.uni.julio.supertv.helper.TVRecyclerView;
import com.uni.julio.supertv.viewmodel.MoviesGridViewModel;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class ActivityMorevideoBinding extends ViewDataBinding {
  @NonNull
  public final AppBarLayout appBarLayout;

  @NonNull
  public final TVRecyclerView moreVideoRecycler;

  @NonNull
  public final Toolbar toolbar;

  @Bindable
  protected MoviesGridViewModel mMoviesGridFragmentVM;

  protected ActivityMorevideoBinding(Object _bindingComponent, View _root, int _localFieldCount,
      AppBarLayout appBarLayout, TVRecyclerView moreVideoRecycler, Toolbar toolbar) {
    super(_bindingComponent, _root, _localFieldCount);
    this.appBarLayout = appBarLayout;
    this.moreVideoRecycler = moreVideoRecycler;
    this.toolbar = toolbar;
  }

  public abstract void setMoviesGridFragmentVM(@Nullable MoviesGridViewModel moviesGridFragmentVM);

  @Nullable
  public MoviesGridViewModel getMoviesGridFragmentVM() {
    return mMoviesGridFragmentVM;
  }

  @NonNull
  public static ActivityMorevideoBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_morevideo, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static ActivityMorevideoBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<ActivityMorevideoBinding>inflateInternal(inflater, com.uni.julio.supertv.R.layout.activity_morevideo, root, attachToRoot, component);
  }

  @NonNull
  public static ActivityMorevideoBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_morevideo, null, false, component)
   */
  @NonNull
  @Deprecated
  public static ActivityMorevideoBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<ActivityMorevideoBinding>inflateInternal(inflater, com.uni.julio.supertv.R.layout.activity_morevideo, null, false, component);
  }

  public static ActivityMorevideoBinding bind(@NonNull View view) {
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
  public static ActivityMorevideoBinding bind(@NonNull View view, @Nullable Object component) {
    return (ActivityMorevideoBinding)bind(component, view, com.uni.julio.supertv.R.layout.activity_morevideo);
  }
}