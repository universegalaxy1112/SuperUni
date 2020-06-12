package com.uni.julio.supertv.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.uni.julio.supertv.adapter.MoviesRecyclerAdapter;
import com.uni.julio.supertv.model.Movie;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class VideoListRowBinding extends ViewDataBinding {
  @NonNull
  public final LinearLayout flMainLayout;

  @NonNull
  public final ImageView img;

  @NonNull
  public final TextView title;

  @Bindable
  protected Movie mMoviesMenuItem;

  @Bindable
  protected MoviesRecyclerAdapter mMoviesAdapter;

  protected VideoListRowBinding(Object _bindingComponent, View _root, int _localFieldCount,
      LinearLayout flMainLayout, ImageView img, TextView title) {
    super(_bindingComponent, _root, _localFieldCount);
    this.flMainLayout = flMainLayout;
    this.img = img;
    this.title = title;
  }

  public abstract void setMoviesMenuItem(@Nullable Movie moviesMenuItem);

  @Nullable
  public Movie getMoviesMenuItem() {
    return mMoviesMenuItem;
  }

  public abstract void setMoviesAdapter(@Nullable MoviesRecyclerAdapter moviesAdapter);

  @Nullable
  public MoviesRecyclerAdapter getMoviesAdapter() {
    return mMoviesAdapter;
  }

  @NonNull
  public static VideoListRowBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.video_list_row, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static VideoListRowBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<VideoListRowBinding>inflateInternal(inflater, com.uni.julio.supertv.R.layout.video_list_row, root, attachToRoot, component);
  }

  @NonNull
  public static VideoListRowBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.video_list_row, null, false, component)
   */
  @NonNull
  @Deprecated
  public static VideoListRowBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<VideoListRowBinding>inflateInternal(inflater, com.uni.julio.supertv.R.layout.video_list_row, null, false, component);
  }

  public static VideoListRowBinding bind(@NonNull View view) {
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
  public static VideoListRowBinding bind(@NonNull View view, @Nullable Object component) {
    return (VideoListRowBinding)bind(component, view, com.uni.julio.supertv.R.layout.video_list_row);
  }
}
