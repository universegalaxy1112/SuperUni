package com.uni.julio.supertvplus.utils.networing;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.uni.julio.supertvplus.LiveTvApplication;
import com.uni.julio.supertvplus.helper.VideoStreamManager;
import com.uni.julio.supertvplus.listeners.LoadEpisodesForSerieResponseListener;
import com.uni.julio.supertvplus.listeners.LoadMoviesForCategoryResponseListener;
import com.uni.julio.supertvplus.listeners.LoadProgramsForLiveTVCategoryResponseListener;
import com.uni.julio.supertvplus.listeners.LoadSeasonsForSerieResponseListener;
import com.uni.julio.supertvplus.listeners.LoadSubCategoriesResponseListener;
import com.uni.julio.supertvplus.listeners.StringRequestListener;
import com.uni.julio.supertvplus.model.CastDevice;
import com.uni.julio.supertvplus.model.LiveTVCategory;
import com.uni.julio.supertvplus.model.MainCategory;
import com.uni.julio.supertvplus.model.Movie;
import com.uni.julio.supertvplus.model.MovieCategory;
import com.uni.julio.supertvplus.model.Season;
import com.uni.julio.supertvplus.model.Serie;
import com.uni.julio.supertvplus.model.VideoStream;
import com.uni.julio.supertvplus.viewmodel.MovieDetailsViewModelContract;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

//import com.livetv.android.utils.networking.parser.FetchJSonFile;

public class NetManager {

    private static NetManager m_NetMInstante;
    private final RequestQueue queue;
    private RequestQueue searchQueue ;

    //    private final ImageLoader mImageLoader;

    public static NetManager getInstance() {
        if(m_NetMInstante == null) {
            m_NetMInstante = new NetManager();
        }
        return m_NetMInstante;
    }

    private NetManager() {
         ;//Log.d("NetManager", "NetManager constructor");

        queue = Volley.newRequestQueue(LiveTvApplication.getAppContext());
        searchQueue= Volley.newRequestQueue(LiveTvApplication.getAppContext());
    }

    public  void cancelAll() {

        NetManager.this.queue.cancelAll(new RequestQueue.RequestFilter() {
            public boolean apply(Request<?> request) {
                return true;
            }
        });

        this.queue.cancelAll("CurrentRequests");
    }

    public void makeStringRequest(String url, final StringRequestListener stringRequestListener) {
        UTF8StringRequest stringRequest = new UTF8StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                         stringRequestListener.onCompleted(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        stringRequestListener.onError();
                    }
                }
        );
        queue.add(stringRequest);
    }

    public String makeSyncStringRequest(String url) {
        return makeSyncStringRequest(url, 20);
    }
    public String makeSearchStringRequest(String url, int timeOutSeconds) {
        RequestFuture<String> future = RequestFuture.newFuture();
        UTF8StringRequest stringRequest = new UTF8StringRequest(0, url, future, future);
        this.searchQueue.cancelAll(new RequestQueue.RequestFilter() {
            public boolean apply(Request<?> request) {
                return true;
            }
        });
        this.searchQueue.add(stringRequest);
        try {
            return  future.get( timeOutSeconds, TimeUnit.SECONDS);
        } catch (Exception e) {
            return null;
        }
    }
    public String makeSyncStringRequest(String url, int timeOutSeconds) {
        RequestFuture<String> future = RequestFuture.newFuture();
        UTF8StringRequest stringRequest = new UTF8StringRequest(Request.Method.GET, url, future, future);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
        try {
            return future.get(timeOutSeconds, TimeUnit.SECONDS);
        } catch (Exception e) {
           e.printStackTrace();
        }
        return null;
    }
    public void performLogin(String usr, String pss, final StringRequestListener stringRequestListener) {
//        Log.i("NetManager", "performLogin ");
        LiveTVServicesManual.performLogin(usr,pss, stringRequestListener)
                .delay(2, TimeUnit.SECONDS, Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean result) {

                    }
                });
    }
    public void getCastDevices(MovieDetailsViewModelContract.View viewcallback){
        CastDevice castDevice = new CastDevice();
        castDevice.setName("New");
        viewcallback.onDeviceLoaded(castDevice);

    }
    public void getMessages(String user,StringRequestListener stringRequestListener){
        LiveTVServicesManual.getMessages(user,stringRequestListener).delay(2,TimeUnit.SECONDS, Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                    }
                });
    }

    public void performLoginCode(String user,String code,String device_id, StringRequestListener stringRequestListener) {
        LiveTVServicesManual.performLoginCode(user,code,device_id, stringRequestListener).delay(2, TimeUnit.SECONDS, Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe( new Subscriber<Boolean>() {
            public void onCompleted() {

            }

            public void onError(Throwable e) {
            }

            public void onNext(Boolean result) {
            }
        });
    }


    public void performCheckForUpdate(StringRequestListener stringRequestListener) {
        LiveTVServicesManual.performCheckForUpdate(stringRequestListener).delay(2, TimeUnit.SECONDS, Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(  new Subscriber<Boolean>() {
            public void onCompleted() {
            }

            public void onError(Throwable e) {
            }

            public void onNext(Boolean result) {
            }
        });
    }
    public void retrieveLiveTVPrograms(final MainCategory mainCategory, final LoadProgramsForLiveTVCategoryResponseListener liveTVCategoryResponseListener) {
        LiveTVServicesManual.getLiveTVCategories(mainCategory)
                .subscribe(new Subscriber<List<LiveTVCategory>>() {
                    @Override
                    public void onCompleted() {
                        //System.out.println("Categories for main category completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        ;//Log.d("liveTV","retrieveLiveTVPrograms error "+e.getMessage());
                        liveTVCategoryResponseListener.onError();
                    }

                    @Override
                    public void onNext(final List<LiveTVCategory> liveTVCategories) {
                        if(liveTVCategories == null || liveTVCategories.size() == 0) {
                           liveTVCategoryResponseListener.onError();
                        }
                        else {
                            List<Observable<LiveTVCategory>> observableList = new ArrayList<>();
                            for (final LiveTVCategory cat : liveTVCategories) {
                                ;//Log.d("liveTV", "NetManager liveTVCategories : " + cat.getCatName());
                                observableList.add(LiveTVServicesManual.getProgramsForLiveTVCategory(cat));
                            }

                            VideoStreamManager.getInstance().resetLiveTVCategory(liveTVCategories.size());

                            ;//Log.d("liveTV", "NetManager Start loading programs for LIVETV categories");
                            Observable.mergeDelayError(observableList)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Observer<LiveTVCategory>() {
                                        @Override
                                        public void onCompleted() {
                                            ;//Log.d("liveTV", "All list retrieved here onCompleted()");
//                                            listener.onAllMoviesForCategoriesLoaded(mainCategory);
                                            liveTVCategoryResponseListener.onProgramsForLiveTVCategoriesCompleted();
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            ;//Log.d("liveTV", "NetManager getProgramsForLiveTVCategory error "+e.getMessage());
                                            liveTVCategoryResponseListener.onProgramsForLiveTVCategoryError(null);
                                        }

                                        @Override
                                        public void onNext(LiveTVCategory liveTVCategory) {
//                                            ;//Log.d("liveTV", "I have the movie list for " + movies.getCategory().getCatName() + " with elements " + movies.getMovieList().size());

                                            ;//Log.d("liveTV", "NetManager LOADED liveTVCategories : " + liveTVCategory.getCatName());
                                            ;//Log.d("liveTV", "NetManager  " + liveTVCategory.getLivePrograms().size());
                                            liveTVCategoryResponseListener.onProgramsForLiveTVCategoryCompleted(liveTVCategory);
                                        }
                                    });
                        }
                    }
                });
    }

    public void retrieveSubCategories(final MainCategory mainCategory, final LoadSubCategoriesResponseListener subCategoriesResponseListener) {
        //Log.i("NetManager", "retrieveSubCategories for " + mainCategory.getModelType());
        LiveTVServicesManual.getSubCategories(mainCategory)
                .subscribe(new Subscriber<List<MovieCategory>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        subCategoriesResponseListener.onSubCategoriesLoadedError();
                    }

                    @Override
                    public void onNext(List<MovieCategory> movieCategories) {
                        subCategoriesResponseListener.onSubCategoriesLoaded(mainCategory, movieCategories);
                    }
                });
    }

    public void retrieveMoviesForSubCategory(final MainCategory mainCategory, final MovieCategory movieCategory, final LoadMoviesForCategoryResponseListener listener, final int timeOut) {

        LiveTVServicesManual.getMoviesForSubCat(mainCategory.getModelType(), movieCategory.getCatName(), timeOut)
                .subscribe(new Subscriber<List<? extends VideoStream>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        movieCategory.setErrorLoading(true);
                        listener.onMoviesForCategoryCompletedError(movieCategory);
                    }
                    @Override
                    public void onNext(List<? extends VideoStream> movies) {
                        if(movies != null ) {
                            for(VideoStream video : movies) {
                                if(video instanceof Serie) {
                                    ((Serie) video).setMovieCategoryIdOwner(movieCategory.getId());
                                }
                                else if(video instanceof Movie){
                                    ((Movie) video).setMovieCategoryIdOwner(movieCategory.getId());
                                }
                            }
                            movieCategory.setMovieList(movies);
                           listener.onMoviesForCategoryCompleted(movieCategory);
                        }else{
                            listener.onMoviesForCategoryCompletedError(movieCategory);
                        }
                        }
                });
    }
     public void retrieveSeasons(final Serie serie, final LoadSeasonsForSerieResponseListener seriesListener) {

        //Log.i("NetManager", "retrieveSeasons");

        LiveTVServicesManual.getSeasonsForSerie(serie)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        //System.out.println("retrieveSeasons completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        //System.out.println("retrieveSeasons error "+e.getMessage());
                        seriesListener.onError();
                    }

                    @Override
                    public void onNext(Integer seasonCount) {
                        seriesListener.onSeasonsLoaded(serie, seasonCount);
                    }
                });
    }
    public void retrieveEpisodesForSerie(final Serie serie, final Season season, final LoadEpisodesForSerieResponseListener episodesForSerieResponseListener) {

        //Log.i("NetManager", "retrieveEpisodesForSerie");


        LiveTVServicesManual.getEpisodesForSerie(serie, season.getPosition() + 1)
                .subscribe(new Subscriber<List<? extends VideoStream>>() {
                    @Override
                    public void onCompleted() {
                        //System.out.println("onCompleted retrieveEpisodesForSerie " +serie.getTitle() + "  season "+ season.getPosition());
                    }

                    @Override
                    public void onError(Throwable e) {
                        //   ;//Log.d("liveTV", "error episodes is "+e.getMessage());
                        episodesForSerieResponseListener.onError();
                    }
                    @Override
                    public void onNext(List<? extends VideoStream> movies) {
                        season.setEpisodeList(movies);
                        episodesForSerieResponseListener.onEpisodesForSerieCompleted(season);
//
                    }
                });

    }

}