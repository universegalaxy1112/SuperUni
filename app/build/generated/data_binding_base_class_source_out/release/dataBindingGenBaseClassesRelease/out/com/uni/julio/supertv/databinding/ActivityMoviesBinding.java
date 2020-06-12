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
import com.uni.julio.supertv.viewmodel.MoviesMenuViewModel;
import java.lang.Deprecated;
import java.lang.Object;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

public abstract class ActivityMoviesBinding extends ViewDataBinding {
  @NonNull
  public final AppBarLayout appBarLayout;

  @Nullable
  public final EditText editPassword;

  @NonNull
  public final WaveSwipeRefreshLayout mainSwipe;

  @NonNull
  public final TVRecyclerView moviecategoryrecycler;

  @NonNull
  public final Toolbar toolbar;

  @Bindable
  protected MoviesMenuViewModel mMoviesMenuFragmentVM;

  protected ActivityMoviesBinding(Object _bindingComponent, View _root, int _localFieldCount,
      AppBarLayout appBarLayout, EditText editPassword, WaveSwipeRefreshLayout mainSwipe,
      TVRecyclerView moviecategoryrecycler, Toolbar toolbar) {
    super(_bindingComponent, _root, _localFieldCount);
    this.appBarLayout = appBarLayout;
    this.editPassword = editPassword;
    this.mainSwipe = mainSwipe;
    this.moviecategoryrecycler = moviecategoryrecycler;
    this.toolbar = toolbar;
  }

  public abstract void setMoviesMenuFragmentVM(@Nullable MoviesMenuViewModel moviesMenuFragmentVM);

  @Nullable
  public MoviesMenuViewModel getMoviesMenuFragmentVM() {
    return mMoviesMenuFragmentVM;
  }

  @NonNull
  public static ActivityMoviesBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_movies, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static ActivityMoviesBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<ActivityMoviesBinding>inflateInternal(inflater, com.uni.julio.supertv.R.layout.activity_movies, root, attachToRoot, component);
  }

  @NonNull
  public static ActivityMoviesBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_movies, null, false, component)
   */
  @NonNull
  @Deprecated
  public static ActivityMoviesBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<ActivityMoviesBinding>inflateInternal(inflater, com.uni.julio.supertv.R.layout.activity_movies, null, false, component);
  }

  public static ActivityMoviesBinding bind(@NonNull View view) {
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
  public static ActivityMoviesBinding bind(@NonNull View view, @Nullable Object component) {
    return (ActivityMoviesBinding)bind(component, view, com.uni.julio.supertv.R.layout.activity_movies);
  }
}
