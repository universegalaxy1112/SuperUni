package com.uni.julio.supertvplus.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.uni.julio.supertvplus.helper.TVRecyclerView;
import com.uni.julio.supertvplus.model.LiveProgram;
import com.uni.julio.supertvplus.model.LiveTVCategory;
import com.uni.julio.supertvplus.viewmodel.LiveTVViewModel;
import de.hdodenhof.circleimageview.CircleImageView;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class ActivityLivetvnewBinding extends ViewDataBinding {
  @NonNull
  public final TextView epg;

  @NonNull
  public final LinearLayout exoPlayer;

  @NonNull
  public final LinearLayout exoPlayerVirtual;

  @NonNull
  public final LinearLayout guide;

  @NonNull
  public final TVRecyclerView liveCategoryRecycler;

  @NonNull
  public final LinearLayout logo;

  @NonNull
  public final CircleImageView markImg;

  @NonNull
  public final FrameLayout parent;

  @NonNull
  public final LinearLayout playerContainer;

  @NonNull
  public final TVRecyclerView programmingRecycler;

  @NonNull
  public final TextView subTitle;

  @NonNull
  public final TextView title;

  @Bindable
  protected LiveTVViewModel mLiveTVFragmentVM;

  @Bindable
  protected LiveTVCategory mCurrentCategory;

  @Bindable
  protected LiveProgram mCurrentProgram;

  protected ActivityLivetvnewBinding(Object _bindingComponent, View _root, int _localFieldCount,
      TextView epg, LinearLayout exoPlayer, LinearLayout exoPlayerVirtual, LinearLayout guide,
      TVRecyclerView liveCategoryRecycler, LinearLayout logo, CircleImageView markImg,
      FrameLayout parent, LinearLayout playerContainer, TVRecyclerView programmingRecycler,
      TextView subTitle, TextView title) {
    super(_bindingComponent, _root, _localFieldCount);
    this.epg = epg;
    this.exoPlayer = exoPlayer;
    this.exoPlayerVirtual = exoPlayerVirtual;
    this.guide = guide;
    this.liveCategoryRecycler = liveCategoryRecycler;
    this.logo = logo;
    this.markImg = markImg;
    this.parent = parent;
    this.playerContainer = playerContainer;
    this.programmingRecycler = programmingRecycler;
    this.subTitle = subTitle;
    this.title = title;
  }

  public abstract void setLiveTVFragmentVM(@Nullable LiveTVViewModel liveTVFragmentVM);

  @Nullable
  public LiveTVViewModel getLiveTVFragmentVM() {
    return mLiveTVFragmentVM;
  }

  public abstract void setCurrentCategory(@Nullable LiveTVCategory currentCategory);

  @Nullable
  public LiveTVCategory getCurrentCategory() {
    return mCurrentCategory;
  }

  public abstract void setCurrentProgram(@Nullable LiveProgram currentProgram);

  @Nullable
  public LiveProgram getCurrentProgram() {
    return mCurrentProgram;
  }

  @NonNull
  public static ActivityLivetvnewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_livetvnew, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static ActivityLivetvnewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<ActivityLivetvnewBinding>inflateInternal(inflater, com.uni.julio.supertvplus.R.layout.activity_livetvnew, root, attachToRoot, component);
  }

  @NonNull
  public static ActivityLivetvnewBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_livetvnew, null, false, component)
   */
  @NonNull
  @Deprecated
  public static ActivityLivetvnewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<ActivityLivetvnewBinding>inflateInternal(inflater, com.uni.julio.supertvplus.R.layout.activity_livetvnew, null, false, component);
  }

  public static ActivityLivetvnewBinding bind(@NonNull View view) {
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
  public static ActivityLivetvnewBinding bind(@NonNull View view, @Nullable Object component) {
    return (ActivityLivetvnewBinding)bind(component, view, com.uni.julio.supertvplus.R.layout.activity_livetvnew);
  }
}