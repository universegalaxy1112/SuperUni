package com.uni.julio.supertv.view.exoplayer;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.drm.FrameworkMediaDrm;
import com.google.android.exoplayer2.drm.HttpMediaDrmCallback;
import com.google.android.exoplayer2.drm.StreamingDrmSessionManager;
import com.google.android.exoplayer2.drm.UnsupportedDrmException;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.SingleSampleMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.DebugTextViewHelper;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import com.uni.julio.supertv.BuildConfig;
import com.uni.julio.supertv.LiveTvApplication;
import com.uni.julio.supertv.R;
import com.uni.julio.supertv.listeners.DialogListener;
import com.uni.julio.supertv.listeners.LiveTVToggleUIListener;
import com.uni.julio.supertv.utils.DataManager;
import com.uni.julio.supertv.utils.Dialogs;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public   class VideoPlayFragment extends Fragment implements View.OnClickListener, ExoPlayer.EventListener, PlaybackControlView.VisibilityListener{
    public static final String DRM_SCHEME_UUID_EXTRA = "drm_scheme_uuid";
    public static final String DRM_LICENSE_URL = "drm_license_url";
    public static final String DRM_KEY_REQUEST_PROPERTIES = "drm_key_request_properties";
    public static final String PREFER_EXTENSION_DECODERS = "prefer_extension_decoders";

    public static final String ACTION_VIEW = "com.google.android.exoplayer.demo.action.VIEW";
    public static final String EXTENSION_EXTRA = "extension";

    public static final String ACTION_VIEW_LIST = "com.google.android.exoplayer.demo.action.VIEW_LIST";
    public static final String URI_LIST_EXTRA = "uri_list";
    public static final String EXTENSION_LIST_EXTRA = "extension_list";
    public static final String MOVIE_ID_EXTRA = "movie_id_extra";
    public static final String SECONDS_TO_START_EXTRA = "seconds_to_start";
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private static final CookieManager DEFAULT_COOKIE_MANAGER;
    static {
        DEFAULT_COOKIE_MANAGER = new CookieManager();
        DEFAULT_COOKIE_MANAGER.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
    }

    private Handler mainHandler;
    private Timeline.Window window;
    private EventLogger eventLogger;
    private SimpleExoPlayerView simpleExoPlayerView;
    private LinearLayout debugRootView;
    private TextView debugTextView;
    private Button retryButton;
    private DataSource.Factory mediaDataSourceFactory;
    private SimpleExoPlayer player;
    private DefaultTrackSelector trackSelector;
    private TrackSelectionHelper trackSelectionHelper;
    private DebugTextViewHelper debugViewHelper;
    private boolean playerNeedsSource;
    private boolean shouldAutoPlay;
    private boolean isTimelineStatic;
    private int playerWindow;
    private long playerPosition;
    private int movieId;
    private int type=0;
    private LiveTVToggleUIListener liveTVToggleListener;
    private ProgressBar progressBarView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shouldAutoPlay = true;
        mediaDataSourceFactory = buildDataSourceFactory(true);
        mainHandler = new Handler();
        window = new Timeline.Window();
        if (CookieHandler.getDefault() != DEFAULT_COOKIE_MANAGER) {
            CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER);
        }
    }

    private boolean hideControls = false;
    private boolean isLiveTV = false;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         View rootPlayerView= inflater.inflate(R.layout.activity_video_play, container, false);
        View rootView = rootPlayerView.findViewById(R.id.root);
        //rootView.setOnClickListener(this);
        debugRootView = (LinearLayout) rootPlayerView.findViewById(R.id.controls_root);
        debugTextView = (TextView) rootPlayerView.findViewById(R.id.debug_text_view);
        retryButton = (Button) rootPlayerView.findViewById(R.id.retry_button);
        retryButton.setOnClickListener(this);
        retryButton.setBackgroundResource(R.drawable.primary_button_selector);
        retryButton.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    v.setSelected(true);
                }
                else {
                    v.setSelected(false);
                }
            }
        });
        simpleExoPlayerView = (SimpleExoPlayerView) rootPlayerView.findViewById(R.id.player_view);

        progressBarView = (ProgressBar) rootPlayerView.findViewById(R.id.player_view_progress_bar);
        simpleExoPlayerView.setControllerVisibilityListener(this);
        simpleExoPlayerView.requestFocus();

        if(hideControls) {
            debugRootView.setVisibility(View.GONE);
            simpleExoPlayerView.setUseController(false);
        }
        simpleExoPlayerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(liveTVToggleListener != null)
                    liveTVToggleListener.onToggleUI(true);
                return false;
            }
        });
         return rootPlayerView;
    }

    public void hideControls(LiveTVToggleUIListener listener) {
        hideControls = true;
        isLiveTV = true;
        liveTVToggleListener = listener;
        setRetainInstance(true);
    }
    @Override
    public void onStart(){
        super.onStart();
        if(Util.SDK_INT>23){
            initializePlayer();
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        if((Util.SDK_INT<=23||player==null)){
            initializePlayer();
        }
    }
    @Override
    public void onPause(){
        super.onPause();
        if(Util.SDK_INT<=23){
            releasePlayer();
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }
    public void onNewIntent(Intent intent) {
        releasePlayer();
        isTimelineStatic = false;
        getActivity().setIntent(intent);
    }

    private void releasePlayer() {
        if (player != null) {
            debugViewHelper.stop();
            debugViewHelper = null;
            shouldAutoPlay = player.getPlayWhenReady();
            playerWindow = player.getCurrentWindowIndex();
            playerPosition = C.TIME_UNSET;
            Timeline timeline = player.getCurrentTimeline();
            if (!timeline.isEmpty() && timeline.getWindow(playerWindow, window).isSeekable) {
                playerPosition = player.getCurrentPosition();
                 DataManager.getInstance().saveDataLong("seconds"+movieId,playerPosition);
            }
            player.release();
            player = null;
            trackSelector = null;
            trackSelectionHelper = null;
            eventLogger = null;
        }
    }

    @Override
    public void onVisibilityChange(int visibility) {
        if(hideControls) {
            debugRootView.setVisibility(View.GONE);
            return;
        }
        debugRootView.setVisibility(visibility);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initializePlayer();
        } else {
            showToast(R.string.storage_permission_denied);
            getActivity().finish();
        }
    }
     public boolean dispatchKeyEvent() {
         simpleExoPlayerView.showController();
         return true;
     }
    public void doForwardVideo()
    {
        if (player == null) {
            return;
        }
        Long pos = player.getCurrentPosition();
        pos += 15000; // milliseconds
        player.seekTo(pos);
     }

    public void doRewindVideo()
    {
        if (player == null) {
            return;
        }

        Long pos = player.getCurrentPosition();
        pos -= 5000; // milliseconds
        player.seekTo(pos);
     }
     public void playPause(){
         if(player.getPlayWhenReady()){
             player.setPlayWhenReady(false);
         }
         else{
             player.setPlayWhenReady(true);
         }
     }
    @Override
    public void onClick(View view) {
        if (view == retryButton) {
            initializePlayer();
        } else if (view.getParent() == debugRootView) {
            MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
            if (mappedTrackInfo != null) {
                trackSelectionHelper.showSelectionDialog(getActivity(), ((Button) view).getText(),
                        trackSelector.getCurrentMappedTrackInfo(), (int) view.getTag());
            }
        }
    }
    private void showToast(int messageId) {
        showToast(getResources().getString(messageId));
    }
    private void showToast(String message){

    }
    private void initializePlayer(){

        Intent intent = getActivity().getIntent();
        //SuperTV add progressbar here
        movieId = intent.getIntExtra(MOVIE_ID_EXTRA, -1);
        playerPosition = C.TIME_UNSET;
        playerPosition = intent.getLongExtra(SECONDS_TO_START_EXTRA, 0);
        if(intent.getIntExtra("mainCategoryId", -1) == 4) {//eventso
            hideControls = true;
        }
        if (player == null) {
            boolean preferExtensionDecoders = intent.getBooleanExtra(PREFER_EXTENSION_DECODERS, false);
            UUID drmSchemeUuid = intent.hasExtra(DRM_SCHEME_UUID_EXTRA)
                    ? UUID.fromString(intent.getStringExtra(DRM_SCHEME_UUID_EXTRA)) : null;
            DrmSessionManager<FrameworkMediaCrypto> drmSessionManager = null;
            if (drmSchemeUuid != null) {
                String drmLicenseUrl = intent.getStringExtra(DRM_LICENSE_URL);
                String[] keyRequestPropertiesArray = intent.getStringArrayExtra(DRM_KEY_REQUEST_PROPERTIES);
                Map<String, String> keyRequestProperties;
                if (keyRequestPropertiesArray == null || keyRequestPropertiesArray.length < 2) {
                    keyRequestProperties = null;
                } else {
                    keyRequestProperties = new HashMap<>();
                    for (int i = 0; i < keyRequestPropertiesArray.length - 1; i += 2) {
                        keyRequestProperties.put(keyRequestPropertiesArray[i],
                                keyRequestPropertiesArray[i + 1]);
                    }
                }
                try {
                    drmSessionManager = buildDrmSessionManager(drmSchemeUuid, drmLicenseUrl,
                            keyRequestProperties);
                } catch (UnsupportedDrmException e) {
                    int errorStringId = Util.SDK_INT < 18 ? R.string.error_drm_not_supported
                            : (e.reason == UnsupportedDrmException.REASON_UNSUPPORTED_SCHEME
                            ? R.string.error_drm_unsupported_scheme : R.string.error_drm_unknown);
                    showToast(errorStringId);
                    return;
                }
            }

            @SimpleExoPlayer.ExtensionRendererMode int extensionRendererMode =
                    ((LiveTvApplication) getActivity().getApplication()).useExtensionRenderers()
                            ? (preferExtensionDecoders ? SimpleExoPlayer.EXTENSION_RENDERER_MODE_PREFER
                            : SimpleExoPlayer.EXTENSION_RENDERER_MODE_ON)
                            : SimpleExoPlayer.EXTENSION_RENDERER_MODE_OFF;
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveVideoTrackSelection.Factory(BANDWIDTH_METER);
            trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
            trackSelectionHelper = new TrackSelectionHelper(trackSelector, videoTrackSelectionFactory);
            player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, new DefaultLoadControl(),
                    drmSessionManager, extensionRendererMode);
            player.addListener(this);

            eventLogger = new EventLogger(trackSelector);
            player.addListener(eventLogger);
            player.setAudioDebugListener(eventLogger);
            player.setVideoDebugListener(eventLogger);
            player.setId3Output(eventLogger);

            simpleExoPlayerView.setPlayer(player);
            if(intent.getIntExtra("mainCategoryId", -1) != 4&& intent.getIntExtra("type",1)!=2 && playerPosition!=0) {//eventso
                Dialogs.showTwoButtonsDialog((AppCompatActivity) this.getActivity(), R.string.accept, R.string.cancel, R.string.from_start, new DialogListener() {
                    @TargetApi(Build.VERSION_CODES.M)
                    @Override
                    public void onAccept() {
                        if (playerPosition == C.TIME_UNSET) {
                            player.seekToDefaultPosition(playerWindow);
                        } else {
                            player.seekTo(playerWindow, playerPosition);
                        }
                    }

                    @Override
                    public void onCancel() {

                    }
                });

            }
            player.setPlayWhenReady(shouldAutoPlay);
            debugViewHelper = new DebugTextViewHelper(player, debugTextView);
            debugViewHelper.start();
            playerNeedsSource = true;
        }
        if (playerNeedsSource) {
            String action = intent.getAction();
            Uri[] uris;
            String[] extensions;
            if (ACTION_VIEW.equals(action)) {
                uris = new Uri[] {intent.getData()};
                extensions = new String[] {intent.getStringExtra(EXTENSION_EXTRA)};
            } else if (ACTION_VIEW_LIST.equals(action)) {
                String[] uriStrings = intent.getStringArrayExtra(URI_LIST_EXTRA);
                uris = new Uri[uriStrings.length];
                for (int i = 0; i < uriStrings.length; i++) {
                    uris[i] = Uri.parse(uriStrings[i]);
                }
                extensions = intent.getStringArrayExtra(EXTENSION_LIST_EXTRA);
                if (extensions == null) {
                    extensions = new String[uriStrings.length];
                }
            } else {
                showToast(getString(R.string.unexpected_intent_action));
                return;
            }
            if (Util.maybeRequestReadExternalStoragePermission(getActivity(), uris)) {
                // The player will be reinitialized if the permission is granted.
                return;
            }
            MediaSource[] mediaSources = new MediaSource[uris.length];
            for (int i = 0; i < uris.length; i++) {
                mediaSources[i] = buildMediaSource(uris[i], extensions[i]);
            }
            MediaSource mediaSource = mediaSources.length == 1 ? mediaSources[0] : new ConcatenatingMediaSource(mediaSources);

            //SuperTV add subtitles from SRT file
            String subsURL = intent.getStringExtra("subsURL");
            if(!TextUtils.isEmpty(subsURL)) {
                Format textFormat = Format.createTextSampleFormat(null, MimeTypes.APPLICATION_SUBRIP, null, Format.NO_VALUE, Format.NO_VALUE, getString(R.string.tag_esp_srt), null);
                MediaSource textMediaSource = new SingleSampleMediaSource(Uri.parse(subsURL), mediaDataSourceFactory, textFormat, C.TIME_UNSET);
                MediaSource mediaSourceWithText = new MergingMediaSource(mediaSource, textMediaSource);

                player.prepare(mediaSourceWithText, !isTimelineStatic, !isTimelineStatic);
            }
            else {
                player.prepare(mediaSource, !isTimelineStatic, !isTimelineStatic);
            }
            playerNeedsSource = false;
            updateButtonVisibilities();
        }

    }
    private DataSource.Factory buildDataSourceFactory(boolean useBandwidthMeter) {
        return ((LiveTvApplication) getActivity().getApplication())
                .buildDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
    }

    public boolean useExtensionRenderers() {
        return "withExtensions".equals(BuildConfig.FLAVOR);
    }
    private DrmSessionManager<FrameworkMediaCrypto> buildDrmSessionManager(UUID uuid,
                                                                           String licenseUrl, Map<String, String> keyRequestProperties) throws UnsupportedDrmException {
        if (Util.SDK_INT < 18) {
            return null;
        }
        HttpMediaDrmCallback drmCallback = new HttpMediaDrmCallback(licenseUrl,
                buildHttpDataSourceFactory(false), keyRequestProperties);
        return new StreamingDrmSessionManager<>(uuid,
                FrameworkMediaDrm.newInstance(uuid), drmCallback, null, mainHandler, eventLogger);
    }
    private MediaSource buildMediaSource(Uri uri, String overrideExtension) {
        int type = Util.inferContentType(!TextUtils.isEmpty(overrideExtension) ? "." + overrideExtension
                : uri.getLastPathSegment());
        switch (type) {
            case C.TYPE_SS:
                return new SsMediaSource(uri, buildDataSourceFactory(false),
                        new DefaultSsChunkSource.Factory(mediaDataSourceFactory), mainHandler, eventLogger);
            case C.TYPE_DASH:
                return new DashMediaSource(uri, buildDataSourceFactory(false),
                        new DefaultDashChunkSource.Factory(mediaDataSourceFactory), mainHandler, eventLogger);
            case C.TYPE_HLS:
                return new HlsMediaSource(uri, mediaDataSourceFactory, mainHandler, eventLogger);
            case C.TYPE_OTHER:
                return new ExtractorMediaSource(uri, mediaDataSourceFactory, new DefaultExtractorsFactory(),
                        mainHandler, eventLogger);
            default: {
                throw new IllegalStateException("Unsupported type: " + type);
            }
        }
    }
    private void updateButtonVisibilities() {
        if(hideControls) {
            debugRootView.setVisibility(View.GONE);
            return;
        }
        debugRootView.removeAllViews();

        retryButton.setVisibility(playerNeedsSource ? View.VISIBLE : View.GONE);
        debugRootView.addView(retryButton);

        if (player == null) {
            return;
        }

        MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
        if (mappedTrackInfo == null) {
            return;
        }

        for (int i = 0; i < mappedTrackInfo.length; i++) {
            TrackGroupArray trackGroups = mappedTrackInfo.getTrackGroups(i);
            if (trackGroups.length != 0) {
                if(player.getRendererType(i) != C.TRACK_TYPE_VIDEO) {//SuperTV
                    Button button = new Button(getContext());
                    button.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if(hasFocus) {
                                v.setSelected(true);
                            }
                            else {
                                v.setSelected(false);
                            }
                        }
                    });
                    button.setBackgroundResource(R.drawable.primary_button_selector);
                    int label;
                    switch (player.getRendererType(i)) {
                        case C.TRACK_TYPE_AUDIO:
                            label = R.string.audio;
                            break;
                        case C.TRACK_TYPE_VIDEO:
                            label = R.string.video;
                            break;
                        case C.TRACK_TYPE_TEXT:
                            label = R.string.text;
                            break;
                        default:
                            continue;
                    }
                    button.setText(label);
                    button.setTag(i);
                    button.setOnClickListener(this);
                    debugRootView.addView(button, debugRootView.getChildCount() - 1);
                }
            }
        }
    }
    private HttpDataSource.Factory buildHttpDataSourceFactory(boolean useBandwidthMeter) {
        return ((LiveTvApplication) getActivity().getApplication())
                .buildHttpDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
    }
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        ;//Log.d("liveTV","onPlayerStateChanged "+playWhenReady+"  state "+playbackState);
        if (playbackState == ExoPlayer.STATE_ENDED) {
            showControls();
        }
        if (playbackState == ExoPlayer.STATE_BUFFERING) {
            progressBarView.setVisibility(View.VISIBLE);
        }
        else {
//        if (playbackState == ExoPlayer.STATE_READY) {
            progressBarView.setVisibility(View.GONE);
        }
        updateButtonVisibilities();
    }
    @Override
    public void onPlayerError(ExoPlaybackException e) {
        String errorString = null;
        if (e.type == ExoPlaybackException.TYPE_RENDERER) {
            Exception cause = e.getRendererException();
            if (cause instanceof MediaCodecRenderer.DecoderInitializationException) {
                // Special case for decoder initialization failures.
                MediaCodecRenderer.DecoderInitializationException decoderInitializationException =
                        (MediaCodecRenderer.DecoderInitializationException) cause;
                if (decoderInitializationException.decoderName == null) {
                    if (decoderInitializationException.getCause() instanceof MediaCodecUtil.DecoderQueryException) {
                        errorString = getString(R.string.error_querying_decoders);
                    } else if (decoderInitializationException.secureDecoderRequired) {
                        errorString = getString(R.string.error_no_secure_decoder,
                                decoderInitializationException.mimeType);
                    } else {
                        errorString = getString(R.string.error_no_decoder,
                                decoderInitializationException.mimeType);
                    }
                } else {
                    errorString = getString(R.string.error_instantiating_decoder,
                            decoderInitializationException.decoderName);
                }
            }
        }
        if (errorString != null) {
            showToast(errorString);
        }
        showToastError();
        playerNeedsSource = true;
        updateButtonVisibilities();
        showControls();
    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
        isTimelineStatic = !timeline.isEmpty()
                && !timeline.getWindow(timeline.getWindowCount() - 1, window).isDynamic;
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        updateButtonVisibilities();
        MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
        if (mappedTrackInfo != null) {
            if (mappedTrackInfo.getTrackTypeRendererSupport(C.TRACK_TYPE_VIDEO)
                    == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                showToast(R.string.error_unsupported_video);
            }
            if (mappedTrackInfo.getTrackTypeRendererSupport(C.TRACK_TYPE_AUDIO)
                    == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                showToast(R.string.error_unsupported_audio);
            }
        }
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    private void showControls() {
        if(hideControls) {
            debugRootView.setVisibility(View.GONE);
            return;
        }
        debugRootView.setVisibility(View.VISIBLE);
    }
    private void showToastError() {
         if(liveTVToggleListener != null)
            liveTVToggleListener.onToggleUI(true);
        Dialogs.showOneButtonDialog((AppCompatActivity) getActivity(), R.string.generic_error_message_title, R.string.generic_video_loading_message);
     }
}