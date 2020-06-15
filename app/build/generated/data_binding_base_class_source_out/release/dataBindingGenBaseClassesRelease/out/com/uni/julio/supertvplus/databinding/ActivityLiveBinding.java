package com.uni.julio.supertvplus.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.google.android.material.tabs.TabLayout;
import com.uni.julio.supertvplus.helper.TVRecyclerView;
import com.uni.julio.supertvplus.viewmodel.LiveTVViewModel;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class ActivityLiveBinding extends ViewDataBinding {
  @NonNull
  public final TabLayout categoryTab;

  @NonNull
  public final LinearLayout exoPlayer;

  @NonNull
  public final LinearLayout liveCategoryTab;

  @NonNull
  public final LinearLayout livePrograms;

  @NonNull
  public final TVRecyclerView programmingRecycler;

  @Bindable
  protected LiveTVViewModel mLiveTVFragmentVM;

  protected ActivityLiveBinding(Object _bindingComponent, View _root, int _localFieldCount,
      TabLayout categoryTab, LinearLayout exoPlayer, LinearLayout liveCategoryTab,
      LinearLayout livePrograms, TVRecyclerView programmingRecycler) {
    super(_bindingComponent, _root, _localFieldCount);
    this.categoryTab = categoryTab;
    this.exoPlayer = exoPlayer;
    this.liveCategoryTab = liveCategoryTab;
    this.livePrograms = livePrograms;
    this.programmingRecycler = programmingRecycler;
  }

  public abstract void setLiveTVFragmentVM(@Nullable LiveTVViewModel liveTVFragmentVM);

  @Nullable
  public LiveTVViewModel getLiveTVFragmentVM() {
    return mLiveTVFragmentVM;
  }

  @NonNull
  public static ActivityLiveBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_live, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static ActivityLiveBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<ActivityLiveBinding>inflateInternal(inflater, com.uni.julio.supertvplus.R.layout.activity_live, root, attachToRoot, component);
  }

  @NonNull
  public static ActivityLiveBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_live, null, false, component)
   */
  @NonNull
  @Deprecated
  public static ActivityLiveBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<ActivityLiveBinding>inflateInternal(inflater, com.uni.julio.supertvplus.R.layout.activity_live, null, false, component);
  }

  public static ActivityLiveBinding bind(@NonNull View view) {
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
  public static ActivityLiveBinding bind(@NonNull View view, @Nullable Object component) {
    return (ActivityLiveBinding)bind(component, view, com.uni.julio.supertvplus.R.layout.activity_live);
  }
}