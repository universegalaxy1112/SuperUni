package com.uni.julio.supertv.utils.networing;

import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;

import com.uni.julio.supertv.listeners.StringRequestListener;
import com.uni.julio.supertv.model.LiveProgram;
import com.uni.julio.supertv.model.LiveTVCategory;
import com.uni.julio.supertv.model.MainCategory;
import com.uni.julio.supertv.model.MovieCategory;
import com.uni.julio.supertv.model.Serie;
import com.uni.julio.supertv.model.VideoStream;
import com.uni.julio.supertv.utils.Device;
import com.uni.julio.supertv.utils.networing.parser.FetchJSonFileSync;

import java.net.URLEncoder;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

//import com.livetv.android.utils.networking.parser.FetchJSonFile;

public class LiveTVServicesManual {

    public static Observable<Boolean> performLogin (final String usr, final String pss, final StringRequestListener stringRequestListener) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(loginRequest(usr, pss, stringRequestListener));
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.computation())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    public static Observable<List<LiveTVCategory>> getLiveTVCategories(final MainCategory category) {
        return Observable.create(new Observable.OnSubscribe<List<LiveTVCategory>>() {
            @Override
            public void call(Subscriber<? super List<LiveTVCategory>> subscriber) {
                subscriber.onNext(retrieveLiveTVCategories(category));
                subscriber.onCompleted();
            }
        })      .subscribeOn(Schedulers.computation())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    private static List<? extends VideoStream> retrieveEpisodesForSerie(Serie serie, Integer season) {
        FetchJSonFileSync fetch = new FetchJSonFileSync();
        return fetch.retrieveMoviesForSerie(serie, season);
    }
    private static List<LiveTVCategory> retrieveLiveTVCategories(MainCategory category) {
        FetchJSonFileSync fetch = new FetchJSonFileSync();
        return fetch.retrieveLiveTVCategories(category);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private static boolean loginRequest(String usr, String pss, final StringRequestListener stringRequestListener) {
        String loginUrl;
         try {

               loginUrl = WebConfig.loginURL
                    .replace("{USER}",usr)
                    .replace("{PASS}",pss)
                    .replace("{DEVICE_ID}", Device.getIdentifier())
                    .replace("{MODEL}", URLEncoder.encode(Device.getModel(), "UTF-8"))
                    .replace("{FW}", URLEncoder.encode(Device.getFW(), "UTF-8"))
                    .replace("{COUNTRY}", URLEncoder.encode(Device.getCountry(), "UTF-8"));
        } catch (Exception e) {
            loginUrl = "";
        }
//        Log.d("liveTV","PerformLogin + "+ loginUrl);
        if(!TextUtils.isEmpty(loginUrl)) {

            NetManager.getInstance().makeStringRequest(loginUrl, new StringRequestListener() {
                @Override
                public void onCompleted(String response) {
                    stringRequestListener.onCompleted(response);
                }
                @Override
                public void onError() {
                    stringRequestListener.onError();
                }
            });        }
        return true;
    }
    public static Observable<Boolean> performLoginCode(final String code, final StringRequestListener stringRequestListener) {
        return Observable.create( new Observable.OnSubscribe<Boolean>() {
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(Boolean.valueOf(LiveTVServicesManual.loginCodeRequest(code, stringRequestListener)));
                subscriber.onCompleted();
            }
        }) .subscribeOn(Schedulers.computation())
                .unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
    public static boolean loginCodeRequest(String code, final StringRequestListener stringRequestListener) {
        String loginCodeUrl;
        try {
            loginCodeUrl = WebConfig.LoginCodeURL.replace("{CODE}", code);
        } catch (Exception e) {
            loginCodeUrl = "";
        }
         if (!TextUtils.isEmpty(loginCodeUrl)) {
            NetManager.getInstance().makeStringRequest(loginCodeUrl, new StringRequestListener() {
                public void onCompleted(String response) {
                    stringRequestListener.onCompleted(response);
                }

                public void onError() {
                    stringRequestListener.onError();
                }
            });
        }
        return true;
    }

    public static Observable<Boolean> performGetCode(final StringRequestListener stringRequestListener) {
        return Observable.create(  new Observable.OnSubscribe<Boolean>() {
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(Boolean.valueOf(LiveTVServicesManual.getCodeRequest(stringRequestListener)));
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.computation())
                .unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static boolean getCodeRequest(final StringRequestListener stringRequestListener) {
        String getCodeUrl = WebConfig.GetCodeURL;
        if (!TextUtils.isEmpty(getCodeUrl)) {
            NetManager.getInstance().makeStringRequest(getCodeUrl, new StringRequestListener() {
                public void onCompleted(String response) {
                    stringRequestListener.onCompleted(response);
                }

                public void onError() {
                    stringRequestListener.onError();
                }
            });
        }
        return true;
    }




    public static Observable<Integer> getSeasonsForSerie(final Serie serie) {

        return Observable.create( new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(retrieveSeasonsForSerie(serie));
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.computation())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private static Integer retrieveSeasonsForSerie(Serie serie) {
        ;//Log.d("liveTV","retrieveSeasonsForSerie + "+ serie.getTitle());
        int seasons;
        try {
            seasons = Integer.parseInt(serie.getSeasonCountText().replaceAll("[^0-9]", ""));
            ;//Log.d("liveTV","retrieveSeasonsForSerie + "+ seasons);
        }
        catch (Exception e) {
            seasons = 0;
        }
        return seasons;
    }
    public static Observable<LiveTVCategory> getProgramsForLiveTVCategory(final LiveTVCategory liveTVCategory) {

        return Observable.create(new Observable.OnSubscribe<LiveTVCategory>() {
            @Override
            public void call(Subscriber<? super LiveTVCategory> subscriber) {

                List<LiveProgram> livePrograms = retrieveProgramsForLiveTVCategory(liveTVCategory);
//                livePrograms = null;
                liveTVCategory.setTotalChannels(livePrograms.size());
                liveTVCategory.setLivePrograms(livePrograms);

                subscriber.onNext(liveTVCategory);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.computation())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    private static List<LiveProgram> retrieveProgramsForLiveTVCategory(LiveTVCategory liveTVCategory) {
        FetchJSonFileSync fetch = new FetchJSonFileSync();
        return fetch.retrieveProgramsForLiveTVCategory(liveTVCategory);
    }
    public static Observable<List<? extends VideoStream>> getMoviesForSubCat(final String mainCategory, final String movieCategory, final int timeOut) {

        return Observable.create(new Observable.OnSubscribe<List<? extends VideoStream>>() {
            @Override
            public void call(Subscriber<? super List<? extends VideoStream>> subscriber) {
                subscriber.onNext(retrieveMoviesForSubCat(mainCategory, movieCategory, timeOut));
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.computation())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    private static List<? extends VideoStream> retrieveMoviesForSubCat(String mainCategory, String movieCategory, int timeOut) {
        FetchJSonFileSync fetch = new FetchJSonFileSync();
        return fetch.retrieveMovies(mainCategory, movieCategory, timeOut);
    }
    public static Observable<List<MovieCategory>> getSubCategories(final MainCategory category) {

        return Observable.create(new Observable.OnSubscribe<List<MovieCategory>>() {
            @Override
            public void call(Subscriber<? super List<MovieCategory>> subscriber) {
                subscriber.onNext(retrieveSubCategories(category));
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.computation())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    private static List<MovieCategory> retrieveSubCategories(MainCategory category) {
        FetchJSonFileSync fetch = new FetchJSonFileSync();
        return fetch.retrieveSubCategories(category);
    }
    public static Observable<List<? extends VideoStream>> getEpisodesForSerie(final Serie serie, final Integer season) {

        return Observable.create( new Observable.OnSubscribe<List<? extends VideoStream>>() {
            @Override
            public void call(Subscriber<? super List<? extends VideoStream>> subscriber) {
                subscriber.onNext(retrieveEpisodesForSerie(serie, season));
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.computation())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


}
