package com.uni.julio.supertv;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import androidx.databinding.DataBinderMapper;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import com.uni.julio.supertv.databinding.ActivityAccountBindingImpl;
import com.uni.julio.supertv.databinding.ActivityAccountBindingLandImpl;
import com.uni.julio.supertv.databinding.ActivityLiveBindingImpl;
import com.uni.julio.supertv.databinding.ActivityLivetvnewBindingImpl;
import com.uni.julio.supertv.databinding.ActivityMainBindingImpl;
import com.uni.julio.supertv.databinding.ActivityMainBindingLandImpl;
import com.uni.julio.supertv.databinding.ActivityMorevideoBindingImpl;
import com.uni.julio.supertv.databinding.ActivityMoviesBindingImpl;
import com.uni.julio.supertv.databinding.ActivityMoviesBindingLargeImpl;
import com.uni.julio.supertv.databinding.ActivityMultiSeasonDetailBindingImpl;
import com.uni.julio.supertv.databinding.ActivityMultiSeasonDetailBindingLandImpl;
import com.uni.julio.supertv.databinding.ActivityOneseasonDetailBindingImpl;
import com.uni.julio.supertv.databinding.ActivityOneseasonDetailBindingLandImpl;
import com.uni.julio.supertv.databinding.ActivitySearchBindingImpl;
import com.uni.julio.supertv.databinding.ActivitySelectserverBindingImpl;
import com.uni.julio.supertv.databinding.GridviewRowBindingImpl;
import com.uni.julio.supertv.databinding.ItemServerBindingImpl;
import com.uni.julio.supertv.databinding.LiveCategoryListBindingImpl;
import com.uni.julio.supertv.databinding.LivetvnewListBindingImpl;
import com.uni.julio.supertv.databinding.MultiseasonRowBindingImpl;
import com.uni.julio.supertv.databinding.MultiseasonRowBindingLandImpl;
import com.uni.julio.supertv.databinding.VideoListRowBindingImpl;
import com.uni.julio.supertv.databinding.VideoListSectionBindingImpl;
import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.RuntimeException;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBinderMapperImpl extends DataBinderMapper {
  private static final int LAYOUT_ACTIVITYACCOUNT = 1;

  private static final int LAYOUT_ACTIVITYLIVE = 2;

  private static final int LAYOUT_ACTIVITYLIVETVNEW = 3;

  private static final int LAYOUT_ACTIVITYMAIN = 4;

  private static final int LAYOUT_ACTIVITYMOREVIDEO = 5;

  private static final int LAYOUT_ACTIVITYMOVIES = 6;

  private static final int LAYOUT_ACTIVITYMULTISEASONDETAIL = 7;

  private static final int LAYOUT_ACTIVITYONESEASONDETAIL = 8;

  private static final int LAYOUT_ACTIVITYSEARCH = 9;

  private static final int LAYOUT_ACTIVITYSELECTSERVER = 10;

  private static final int LAYOUT_GRIDVIEWROW = 11;

  private static final int LAYOUT_ITEMSERVER = 12;

  private static final int LAYOUT_LIVECATEGORYLIST = 13;

  private static final int LAYOUT_LIVETVNEWLIST = 14;

  private static final int LAYOUT_MULTISEASONROW = 15;

  private static final int LAYOUT_VIDEOLISTROW = 16;

  private static final int LAYOUT_VIDEOLISTSECTION = 17;

  private static final SparseIntArray INTERNAL_LAYOUT_ID_LOOKUP = new SparseIntArray(17);

  static {
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.uni.julio.supertv.R.layout.activity_account, LAYOUT_ACTIVITYACCOUNT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.uni.julio.supertv.R.layout.activity_live, LAYOUT_ACTIVITYLIVE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.uni.julio.supertv.R.layout.activity_livetvnew, LAYOUT_ACTIVITYLIVETVNEW);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.uni.julio.supertv.R.layout.activity_main, LAYOUT_ACTIVITYMAIN);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.uni.julio.supertv.R.layout.activity_morevideo, LAYOUT_ACTIVITYMOREVIDEO);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.uni.julio.supertv.R.layout.activity_movies, LAYOUT_ACTIVITYMOVIES);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.uni.julio.supertv.R.layout.activity_multi_season_detail, LAYOUT_ACTIVITYMULTISEASONDETAIL);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.uni.julio.supertv.R.layout.activity_oneseason_detail, LAYOUT_ACTIVITYONESEASONDETAIL);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.uni.julio.supertv.R.layout.activity_search, LAYOUT_ACTIVITYSEARCH);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.uni.julio.supertv.R.layout.activity_selectserver, LAYOUT_ACTIVITYSELECTSERVER);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.uni.julio.supertv.R.layout.gridview_row, LAYOUT_GRIDVIEWROW);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.uni.julio.supertv.R.layout.item_server, LAYOUT_ITEMSERVER);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.uni.julio.supertv.R.layout.live_category_list, LAYOUT_LIVECATEGORYLIST);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.uni.julio.supertv.R.layout.livetvnew_list, LAYOUT_LIVETVNEWLIST);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.uni.julio.supertv.R.layout.multiseason_row, LAYOUT_MULTISEASONROW);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.uni.julio.supertv.R.layout.video_list_row, LAYOUT_VIDEOLISTROW);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.uni.julio.supertv.R.layout.video_list_section, LAYOUT_VIDEOLISTSECTION);
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View view, int layoutId) {
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = view.getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
        case  LAYOUT_ACTIVITYACCOUNT: {
          if ("layout-land/activity_account_0".equals(tag)) {
            return new ActivityAccountBindingLandImpl(component, view);
          }
          if ("layout/activity_account_0".equals(tag)) {
            return new ActivityAccountBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_account is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYLIVE: {
          if ("layout/activity_live_0".equals(tag)) {
            return new ActivityLiveBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_live is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYLIVETVNEW: {
          if ("layout/activity_livetvnew_0".equals(tag)) {
            return new ActivityLivetvnewBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_livetvnew is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYMAIN: {
          if ("layout/activity_main_0".equals(tag)) {
            return new ActivityMainBindingImpl(component, view);
          }
          if ("layout-land/activity_main_0".equals(tag)) {
            return new ActivityMainBindingLandImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_main is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYMOREVIDEO: {
          if ("layout/activity_morevideo_0".equals(tag)) {
            return new ActivityMorevideoBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_morevideo is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYMOVIES: {
          if ("layout/activity_movies_0".equals(tag)) {
            return new ActivityMoviesBindingImpl(component, view);
          }
          if ("layout-large/activity_movies_0".equals(tag)) {
            return new ActivityMoviesBindingLargeImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_movies is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYMULTISEASONDETAIL: {
          if ("layout/activity_multi_season_detail_0".equals(tag)) {
            return new ActivityMultiSeasonDetailBindingImpl(component, view);
          }
          if ("layout-land/activity_multi_season_detail_0".equals(tag)) {
            return new ActivityMultiSeasonDetailBindingLandImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_multi_season_detail is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYONESEASONDETAIL: {
          if ("layout/activity_oneseason_detail_0".equals(tag)) {
            return new ActivityOneseasonDetailBindingImpl(component, view);
          }
          if ("layout-land/activity_oneseason_detail_0".equals(tag)) {
            return new ActivityOneseasonDetailBindingLandImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_oneseason_detail is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYSEARCH: {
          if ("layout/activity_search_0".equals(tag)) {
            return new ActivitySearchBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_search is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYSELECTSERVER: {
          if ("layout/activity_selectserver_0".equals(tag)) {
            return new ActivitySelectserverBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_selectserver is invalid. Received: " + tag);
        }
        case  LAYOUT_GRIDVIEWROW: {
          if ("layout/gridview_row_0".equals(tag)) {
            return new GridviewRowBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for gridview_row is invalid. Received: " + tag);
        }
        case  LAYOUT_ITEMSERVER: {
          if ("layout/item_server_0".equals(tag)) {
            return new ItemServerBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for item_server is invalid. Received: " + tag);
        }
        case  LAYOUT_LIVECATEGORYLIST: {
          if ("layout/live_category_list_0".equals(tag)) {
            return new LiveCategoryListBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for live_category_list is invalid. Received: " + tag);
        }
        case  LAYOUT_LIVETVNEWLIST: {
          if ("layout/livetvnew_list_0".equals(tag)) {
            return new LivetvnewListBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for livetvnew_list is invalid. Received: " + tag);
        }
        case  LAYOUT_MULTISEASONROW: {
          if ("layout/multiseason_row_0".equals(tag)) {
            return new MultiseasonRowBindingImpl(component, view);
          }
          if ("layout-land/multiseason_row_0".equals(tag)) {
            return new MultiseasonRowBindingLandImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for multiseason_row is invalid. Received: " + tag);
        }
        case  LAYOUT_VIDEOLISTROW: {
          if ("layout/video_list_row_0".equals(tag)) {
            return new VideoListRowBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for video_list_row is invalid. Received: " + tag);
        }
        case  LAYOUT_VIDEOLISTSECTION: {
          if ("layout/video_list_section_0".equals(tag)) {
            return new VideoListSectionBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for video_list_section is invalid. Received: " + tag);
        }
      }
    }
    return null;
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View[] views, int layoutId) {
    if(views == null || views.length == 0) {
      return null;
    }
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = views[0].getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
      }
    }
    return null;
  }

  @Override
  public int getLayoutId(String tag) {
    if (tag == null) {
      return 0;
    }
    Integer tmpVal = InnerLayoutIdLookup.sKeys.get(tag);
    return tmpVal == null ? 0 : tmpVal;
  }

  @Override
  public String convertBrIdToString(int localId) {
    String tmpVal = InnerBrLookup.sKeys.get(localId);
    return tmpVal;
  }

  @Override
  public List<DataBinderMapper> collectDependencies() {
    ArrayList<DataBinderMapper> result = new ArrayList<DataBinderMapper>(1);
    result.add(new androidx.databinding.library.baseAdapters.DataBinderMapperImpl());
    return result;
  }

  private static class InnerBrLookup {
    static final SparseArray<String> sKeys = new SparseArray<String>(22);

    static {
      sKeys.put(0, "_all");
      sKeys.put(1, "currentProgram");
      sKeys.put(2, "liveCategoryAdapter");
      sKeys.put(3, "serverAdapter");
      sKeys.put(4, "currentCategory");
      sKeys.put(5, "activityMainBindingVM");
      sKeys.put(6, "accountDetailsVM");
      sKeys.put(7, "moviesAdapter");
      sKeys.put(8, "moviesMenuItem");
      sKeys.put(9, "liveTVFragmentVM");
      sKeys.put(10, "movieDetailItem");
      sKeys.put(11, "liveProgramItem");
      sKeys.put(12, "categoryAdapter");
      sKeys.put(13, "moviesGridFragmentVM");
      sKeys.put(14, "movieDetailsVM");
      sKeys.put(15, "livetvAdapter");
      sKeys.put(16, "movieCategory");
      sKeys.put(17, "liveCategory");
      sKeys.put(18, "SearchFM");
      sKeys.put(19, "user");
      sKeys.put(20, "moviesMenuFragmentVM");
    }
  }

  private static class InnerLayoutIdLookup {
    static final HashMap<String, Integer> sKeys = new HashMap<String, Integer>(23);

    static {
      sKeys.put("layout-land/activity_account_0", com.uni.julio.supertv.R.layout.activity_account);
      sKeys.put("layout/activity_account_0", com.uni.julio.supertv.R.layout.activity_account);
      sKeys.put("layout/activity_live_0", com.uni.julio.supertv.R.layout.activity_live);
      sKeys.put("layout/activity_livetvnew_0", com.uni.julio.supertv.R.layout.activity_livetvnew);
      sKeys.put("layout/activity_main_0", com.uni.julio.supertv.R.layout.activity_main);
      sKeys.put("layout-land/activity_main_0", com.uni.julio.supertv.R.layout.activity_main);
      sKeys.put("layout/activity_morevideo_0", com.uni.julio.supertv.R.layout.activity_morevideo);
      sKeys.put("layout/activity_movies_0", com.uni.julio.supertv.R.layout.activity_movies);
      sKeys.put("layout-large/activity_movies_0", com.uni.julio.supertv.R.layout.activity_movies);
      sKeys.put("layout/activity_multi_season_detail_0", com.uni.julio.supertv.R.layout.activity_multi_season_detail);
      sKeys.put("layout-land/activity_multi_season_detail_0", com.uni.julio.supertv.R.layout.activity_multi_season_detail);
      sKeys.put("layout/activity_oneseason_detail_0", com.uni.julio.supertv.R.layout.activity_oneseason_detail);
      sKeys.put("layout-land/activity_oneseason_detail_0", com.uni.julio.supertv.R.layout.activity_oneseason_detail);
      sKeys.put("layout/activity_search_0", com.uni.julio.supertv.R.layout.activity_search);
      sKeys.put("layout/activity_selectserver_0", com.uni.julio.supertv.R.layout.activity_selectserver);
      sKeys.put("layout/gridview_row_0", com.uni.julio.supertv.R.layout.gridview_row);
      sKeys.put("layout/item_server_0", com.uni.julio.supertv.R.layout.item_server);
      sKeys.put("layout/live_category_list_0", com.uni.julio.supertv.R.layout.live_category_list);
      sKeys.put("layout/livetvnew_list_0", com.uni.julio.supertv.R.layout.livetvnew_list);
      sKeys.put("layout/multiseason_row_0", com.uni.julio.supertv.R.layout.multiseason_row);
      sKeys.put("layout-land/multiseason_row_0", com.uni.julio.supertv.R.layout.multiseason_row);
      sKeys.put("layout/video_list_row_0", com.uni.julio.supertv.R.layout.video_list_row);
      sKeys.put("layout/video_list_section_0", com.uni.julio.supertv.R.layout.video_list_section);
    }
  }
}
