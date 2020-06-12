package com.uni.julio.supertv.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.google.android.material.appbar.AppBarLayout;
import com.uni.julio.supertv.model.Movie;
import com.uni.julio.supertv.viewmodel.MovieDetailsViewModel;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class ActivityOneseasonDetailBinding extends ViewDataBinding {
  @NonNull
  public final TextView actors;

  @NonNull
  public final TextView actorsDetail;

  @NonNull
  public final AppBarLayout appBarLayout;

  @NonNull
  public final TextView description;

  @NonNull
  public final TextView descriptionDetail;

  @NonNull
  public final TextView director;

  @NonNull
  public final TextView directorDetail;

  @NonNull
  public final TextView dislike;

  @NonNull
  public final ImageView fondoUrl;

  @NonNull
  public final ImageView imageView7;

  @NonNull
  public final ImageView imageView8;

  @NonNull
  public final TextView like;

  @NonNull
  public final LinearLayout play;

  @NonNull
  public final RatingBar ratingBar;

  @NonNull
  public final NestedScrollView scroll;

  @NonNull
  public final TextView textView5;

  @Bindable
  protected MovieDetailsViewModel mMovieDetailsVM;

  @Bindable
  protected Movie mMovieDetailItem;

  protected ActivityOneseasonDetailBinding(Object _bindingComponent, View _root,
      int _localFieldCount, TextView actors, TextView actorsDetail, AppBarLayout appBarLayout,
      TextView description, TextView descriptionDetail, TextView director, TextView directorDetail,
      TextView dislike, ImageView fondoUrl, ImageView imageView7, ImageView imageView8,
      TextView like, LinearLayout play, RatingBar ratingBar, NestedScrollView scroll,
      TextView textView5) {
    super(_bindingComponent, _root, _localFieldCount);
    this.actors = actors;
    this.actorsDetail = actorsDetail;
    this.appBarLayout = appBarLayout;
    this.description = description;
    this.descriptionDetail = descriptionDetail;
    this.director = director;
    this.directorDetail = directorDetail;
    this.dislike = dislike;
    this.fondoUrl = fondoUrl;
    this.imageView7 = imageView7;
    this.imageView8 = imageView8;
    this.like = like;
    this.play = play;
    this.ratingBar = ratingBar;
    this.scroll = scroll;
    this.textView5 = textView5;
  }

  public abstract void setMovieDetailsVM(@Nullable MovieDetailsViewModel movieDetailsVM);

  @Nullable
  public MovieDetailsViewModel getMovieDetailsVM() {
    return mMovieDetailsVM;
  }

  public abstract void setMovieDetailItem(@Nullable Movie movieDetailItem);

  @Nullable
  public Movie getMovieDetailItem() {
    return mMovieDetailItem;
  }

  @NonNull
  public static ActivityOneseasonDetailBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_oneseason_detail, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static ActivityOneseasonDetailBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<ActivityOneseasonDetailBinding>inflateInternal(inflater, com.uni.julio.supertv.R.layout.activity_oneseason_detail, root, attachToRoot, component);
  }

  @NonNull
  public static ActivityOneseasonDetailBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_oneseason_detail, null, false, component)
   */
  @NonNull
  @Deprecated
  public static ActivityOneseasonDetailBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<ActivityOneseasonDetailBinding>inflateInternal(inflater, com.uni.julio.supertv.R.layout.activity_oneseason_detail, null, false, component);
  }

  public static ActivityOneseasonDetailBinding bind(@NonNull View view) {
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
  public static ActivityOneseasonDetailBinding bind(@NonNull View view,
      @Nullable Object component) {
    return (ActivityOneseasonDetailBinding)bind(component, view, com.uni.julio.supertv.R.layout.activity_oneseason_detail);
  }
}
