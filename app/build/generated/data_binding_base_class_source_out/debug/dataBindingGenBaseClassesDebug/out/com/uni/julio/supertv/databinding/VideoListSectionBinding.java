package com.uni.julio.supertv.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.uni.julio.supertv.adapter.MoviesCategoryAdapter;
import com.uni.julio.supertv.helper.TVRecyclerView;
import com.uni.julio.supertv.model.MovieCategory;
import com.wang.avi.AVLoadingIndicatorView;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class VideoListSectionBinding extends ViewDataBinding {
  @NonNull
  public final ConstraintLayout allPaneBtn;

  @NonNull
  public final TextView categoryName;

  @NonNull
  public final TextView errorTxt;

  @NonNull
  public final TextView icMore;

  @NonNull
  public final AVLoadingIndicatorView loadingBar;

  @NonNull
  public final TVRecyclerView recyclerView;

  @NonNull
  public final ImageView reload;

  @Bindable
  protected MovieCategory mMovieCategory;

  @Bindable
  protected MoviesCategoryAdapter mCategoryAdapter;

  protected VideoListSectionBinding(Object _bindingComponent, View _root, int _localFieldCount,
      ConstraintLayout allPaneBtn, TextView categoryName, TextView errorTxt, TextView icMore,
      AVLoadingIndicatorView loadingBar, TVRecyclerView recyclerView, ImageView reload) {
    super(_bindingComponent, _root, _localFieldCount);
    this.allPaneBtn = allPaneBtn;
    this.categoryName = categoryName;
    this.errorTxt = errorTxt;
    this.icMore = icMore;
    this.loadingBar = loadingBar;
    this.recyclerView = recyclerView;
    this.reload = reload;
  }

  public abstract void setMovieCategory(@Nullable MovieCategory movieCategory);

  @Nullable
  public MovieCategory getMovieCategory() {
    return mMovieCategory;
  }

  public abstract void setCategoryAdapter(@Nullable MoviesCategoryAdapter categoryAdapter);

  @Nullable
  public MoviesCategoryAdapter getCategoryAdapter() {
    return mCategoryAdapter;
  }

  @NonNull
  public static VideoListSectionBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.video_list_section, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static VideoListSectionBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<VideoListSectionBinding>inflateInternal(inflater, com.uni.julio.supertv.R.layout.video_list_section, root, attachToRoot, component);
  }

  @NonNull
  public static VideoListSectionBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.video_list_section, null, false, component)
   */
  @NonNull
  @Deprecated
  public static VideoListSectionBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<VideoListSectionBinding>inflateInternal(inflater, com.uni.julio.supertv.R.layout.video_list_section, null, false, component);
  }

  public static VideoListSectionBinding bind(@NonNull View view) {
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
  public static VideoListSectionBinding bind(@NonNull View view, @Nullable Object component) {
    return (VideoListSectionBinding)bind(component, view, com.uni.julio.supertv.R.layout.video_list_section);
  }
}
