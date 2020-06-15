package com.uni.julio.supertvplus.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.uni.julio.supertvplus.adapter.ServerAdapter;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class ItemServerBinding extends ViewDataBinding {
  @NonNull
  public final TextView distance;

  @NonNull
  public final LinearLayout flMainLayout;

  @NonNull
  public final TextView serverLocation;

  @NonNull
  public final TextView serverName;

  @Bindable
  protected ServerAdapter mServerAdapter;

  protected ItemServerBinding(Object _bindingComponent, View _root, int _localFieldCount,
      TextView distance, LinearLayout flMainLayout, TextView serverLocation, TextView serverName) {
    super(_bindingComponent, _root, _localFieldCount);
    this.distance = distance;
    this.flMainLayout = flMainLayout;
    this.serverLocation = serverLocation;
    this.serverName = serverName;
  }

  public abstract void setServerAdapter(@Nullable ServerAdapter serverAdapter);

  @Nullable
  public ServerAdapter getServerAdapter() {
    return mServerAdapter;
  }

  @NonNull
  public static ItemServerBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.item_server, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static ItemServerBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<ItemServerBinding>inflateInternal(inflater, com.uni.julio.supertvplus.R.layout.item_server, root, attachToRoot, component);
  }

  @NonNull
  public static ItemServerBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.item_server, null, false, component)
   */
  @NonNull
  @Deprecated
  public static ItemServerBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<ItemServerBinding>inflateInternal(inflater, com.uni.julio.supertvplus.R.layout.item_server, null, false, component);
  }

  public static ItemServerBinding bind(@NonNull View view) {
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
  public static ItemServerBinding bind(@NonNull View view, @Nullable Object component) {
    return (ItemServerBinding)bind(component, view, com.uni.julio.supertvplus.R.layout.item_server);
  }
}
