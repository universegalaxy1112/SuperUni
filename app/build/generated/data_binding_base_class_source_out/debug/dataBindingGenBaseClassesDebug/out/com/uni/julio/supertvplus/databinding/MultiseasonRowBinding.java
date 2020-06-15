package com.uni.julio.supertvplus.databinding;

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
import com.uni.julio.supertvplus.adapter.MultiSeasonAdapter;
import com.uni.julio.supertvplus.model.Episode;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class MultiseasonRowBinding extends ViewDataBinding {
  @NonNull
  public final TextView detail;

  @NonNull
  public final LinearLayout flMainLayout;

  @NonNull
  public final ImageView img;

  @NonNull
  public final LinearLayout textArea;

  @NonNull
  public final TextView title;

  @Bindable
  protected Episode mMoviesMenuItem;

  @Bindable
  protected MultiSeasonAdapter mMoviesAdapter;

  protected MultiseasonRowBinding(Object _bindingComponent, View _root, int _localFieldCount,
      TextView detail, LinearLayout flMainLayout, ImageView img, LinearLayout textArea,
      TextView title) {
    super(_bindingComponent, _root, _localFieldCount);
    this.detail = detail;
    this.flMainLayout = flMainLayout;
    this.img = img;
    this.textArea = textArea;
    this.title = title;
  }

  public abstract void setMoviesMenuItem(@Nullable Episode moviesMenuItem);

  @Nullable
  public Episode getMoviesMenuItem() {
    return mMoviesMenuItem;
  }

  public abstract void setMoviesAdapter(@Nullable MultiSeasonAdapter moviesAdapter);

  @Nullable
  public MultiSeasonAdapter getMoviesAdapter() {
    return mMoviesAdapter;
  }

  @NonNull
  public static MultiseasonRowBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.multiseason_row, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static MultiseasonRowBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<MultiseasonRowBinding>inflateInternal(inflater, com.uni.julio.supertvplus.R.layout.multiseason_row, root, attachToRoot, component);
  }

  @NonNull
  public static MultiseasonRowBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.multiseason_row, null, false, component)
   */
  @NonNull
  @Deprecated
  public static MultiseasonRowBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<MultiseasonRowBinding>inflateInternal(inflater, com.uni.julio.supertvplus.R.layout.multiseason_row, null, false, component);
  }

  public static MultiseasonRowBinding bind(@NonNull View view) {
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
  public static MultiseasonRowBinding bind(@NonNull View view, @Nullable Object component) {
    return (MultiseasonRowBinding)bind(component, view, com.uni.julio.supertvplus.R.layout.multiseason_row);
  }
}
