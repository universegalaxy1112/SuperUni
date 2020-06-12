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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextClock;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.BindingAdapter;
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
import com.google.android.exoplayer2.source.BehindLiveWindowException;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.uni.julio.supertv.LiveTvApplication;
import com.uni.julio.supertv.R;
import com.uni.julio.supertv.binding.BindingAdapters;
import com.uni.julio.supertv.listeners.DialogListener;
import com.uni.julio.supertv.listeners.LiveTVToggleUIListener;
import com.uni.julio.supertv.utils.DataManager;
import com.uni.julio.supertv.utils.Dialogs;
import com.uni.julio.supertv.utils.Tracking;
import com.uni.julio.supertv.view.SpeedTestActivity;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VideoPlayFragment extends Fragment implements View.OnClickListener, ExoPlayer.EventListener,  PlaybackControlView.VisibilityListener{
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
    private ConstraintLayout top_bar;
    private TextView titleText;
    private TextView debugTextView;
    private TextView hideNoChannelText;
    private Button retryButton;
    private ImageView channel_icon;
    private DataSource.Factory mediaDataSourceFactory;
    private SimpleExoPlayer player;
    private DefaultTrackSelector trackSelector;
    private TrackSelectionHelper trackSelectionHelper;
    private DebugTextViewHelper debugViewHelper;
    private boolean playerNeedsSource;
    private boolean shouldAutoPlay;
    private boolean isTimelineStatic;
    private boolean hidePlayback = false;
    private boolean isLiveTV = false;
    private long playerPosition;
    private int movieId;
    private String title = "";
    private LiveTVToggleUIListener liveTVToggleListener;
    private ProgressBar progressBarView;
    private int episodePosition = -1;
    private int seasonPosition = -1;
    private MediaSource mediaSource;
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
        Intent intent = getActivity().getIntent();
        seasonPosition = intent.getIntExtra("seasonPosition", -1);
        episodePosition = intent.getIntExtra("episodePosition", -1);
        this.title=intent.getStringExtra("title") == null ? "" :intent.getStringExtra("title") + ((seasonPosition == -1) ? "": " S" + (seasonPosition+1)) + ((seasonPosition == -1 || episodePosition == -1? "":" E"+ (episodePosition +1)));
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootPlayerView= inflater.inflate(R.layout.videofragment_normal, container, false);
        debugRootView =  rootPlayerView.findViewById(R.id.controls_root);
        top_bar =  rootPlayerView.findViewById(R.id.top_bar);
        progressBarView =rootPlayerView.findViewById(R.id.player_view_progress_bar);
        titleText =  rootPlayerView.findViewById(R.id.titleText);
        debugTextView =  rootPlayerView.findViewById(R.id.debug_text_view);
        hideNoChannelText = rootPlayerView.findViewById(R.id.no_channel_text);
        retryButton =  rootPlayerView.findViewById(R.id.retry_button);
        channel_icon = rootPlayerView.findViewById(R.id.channel_icon);
        // mediaRouteButton = rootPlayerView.findViewById(R.id.media_route_button);
        // CastButtonFactory.setUpMediaRouteButton(LiveTvApplication.getAppContext(),mediaRouteButton);
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

        simpleExoPlayerView =  rootPlayerView.findViewById(R.id.player_view);
        progressBarView =  rootPlayerView.findViewById(R.id.player_view_progress_bar);
        simpleExoPlayerView.setControllerVisibilityListener(this);
        simpleExoPlayerView.requestFocus();

        if (!isLiveTV) {
            hideNoChannelText.setVisibility(View.GONE);
        }else{
            hideNoChannelText.setVisibility(View.VISIBLE);
            top_bar.setVisibility(View.GONE);
        }
        if(hidePlayback){
            debugRootView.setVisibility(View.GONE);
            progressBarView.setVisibility(View.GONE);
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

    public void hidePlayBack(){
        this.hidePlayback = true;
    }

    private void unMute(){
        if(player != null)
            player.setVolume(1000f);
    }
    private  void mute(){
        if(player != null)
            player.setVolume(0f);
    }
    public void toggleMute(){
        if(player != null && player.getVolume() == 0L)
        {
            unMute();
        }else{
            mute();
        }
    }
    public void toggleTitle(){

        if(top_bar.getVisibility() == View.VISIBLE)
        {
            top_bar.setVisibility(View.GONE);
        }
        else{
            top_bar.setVisibility(View.VISIBLE);
        }

    }
    private void hideWhenForward(){
        top_bar.setVisibility(View.GONE);
    }

    public void setLiveTVToggleListener(LiveTVToggleUIListener liveTVToggleListener){
        this.liveTVToggleListener = liveTVToggleListener;
    }

    @Override
    public void onStart(){
        super.onStart();
        initializePlayer();
    }
    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
        releasePlayer();
    }
    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }
    public void onNewIntent(Intent intent) {
        releasePlayer();
        isTimelineStatic = false;
        seasonPosition = intent.getIntExtra("seasonPosition", -1);
        episodePosition = intent.getIntExtra("episodePosition" , -1);
        this.title=intent.getStringExtra("title") == null ? "" :intent.getStringExtra("title") + ((seasonPosition == -1) ? "": " S" + (seasonPosition+1)) + ((seasonPosition == -1 || episodePosition == -1? "":" E"+ (episodePosition +1)));
        BindingAdapters.loadImage(channel_icon, intent.getStringExtra("icon_url"));
        Tracking.getInstance().setAction(this.title);
        Tracking.getInstance().track();
        getActivity().setIntent(intent);
    }

    private void releasePlayer() {
        try{
            if (player != null) {
                debugViewHelper.stop();
                debugViewHelper = null;
                shouldAutoPlay = player.getPlayWhenReady();
                int playerWindow = player.getCurrentWindowIndex();
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
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onVisibilityChange(int visibility) {
        if(!isLiveTV)
            debugRootView.setVisibility(visibility);
        top_bar.setVisibility(visibility);
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
    public void dispatchKeyEvent() {
        if(simpleExoPlayerView == null) return;
        simpleExoPlayerView.showController();
    }
    public void doForwardVideo()
    {

        if (player == null)
            return;
        long pos = player.getCurrentPosition();
        pos += 15000; // milliseconds
        player.seekTo(seasonPosition == -1 || episodePosition == -1 ? 0:episodePosition, pos);
        hideWhenForward();
    }

    public void doRewindVideo()
    {
        if (player == null)
            return;
        long pos = player.getCurrentPosition();
        pos -= 5000; // milliseconds
        player.seekTo(seasonPosition == -1 || episodePosition == -1 ? 0:episodePosition, pos);
        hideWhenForward();
    }
    public void playPause() {
        if (player == null)
            return;
        if(player.getPlayWhenReady())
            player.setPlayWhenReady(false);
        else
            player.setPlayWhenReady(true);
    }

    public void hideNoChannel(){
        hideNoChannelText.setVisibility(View.GONE);
    }

    public void showNoChannel(){
        isLiveTV = true;
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
        try{
            Intent intent = getActivity().getIntent();
            //SuperTV add progressbar here
            movieId = intent.getIntExtra(MOVIE_ID_EXTRA, -1);
            int mainCategory = intent.getIntExtra("mainCategoryId", -1);
            playerPosition = C.TIME_UNSET;
            playerPosition = mainCategory == 4 ? 0L : intent.getLongExtra(SECONDS_TO_START_EXTRA,0L);
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


                eventLogger = new EventLogger(trackSelector);
                player.setAudioDebugListener(eventLogger);
                player.setVideoDebugListener(eventLogger);
                player.setId3Output(eventLogger);
                player.addListener(this);
                simpleExoPlayerView.setPlayer(player);

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
                mediaSource = mediaSources.length == 1 ? mediaSources[0] : new ConcatenatingMediaSource(mediaSources);

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
                if(player != null)
                    player.seekTo(seasonPosition == -1 || episodePosition == -1 ? 0:episodePosition, 0);
                if(mainCategory != 4 && intent.getIntExtra("type",1) != 2 && playerPosition != 0L) {//eventso
                    Dialogs.showTwoButtonsDialog((AppCompatActivity) this.getActivity(), R.string.accept, R.string.cancel, R.string.from_start, new DialogListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onAccept() {
                            if (playerPosition != C.TIME_UNSET && player != null) {
                                player.seekTo(seasonPosition == -1 || episodePosition == -1 ? 0 : episodePosition, playerPosition);
                            }
                        }

                        @Override
                        public void onCancel() {
                            if(player != null)
                            player.seekTo(seasonPosition == -1 || episodePosition == -1 ? 0:episodePosition, 0);
                        }

                        @Override
                        public void onDismiss() {
                            if(player != null)
                            player.seekTo(seasonPosition == -1 || episodePosition == -1 ? 0:episodePosition, 0);
                        }
                    });
                }
                playerNeedsSource = false;
                updateButtonVisibilities();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private DataSource.Factory buildDataSourceFactory(boolean useBandwidthMeter) {
        return ((LiveTvApplication) getActivity().getApplication())
                .buildDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
    }
    public boolean isAvailable()
    {
        GoogleApiAvailability availability = GoogleApiAvailability.getInstance();
        int resultCode = availability.isGooglePlayServicesAvailable(LiveTvApplication.getAppContext());
        if(resultCode == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED){
            return false;
        }else{
            return true;
        }
    }
    private DrmSessionManager<FrameworkMediaCrypto> buildDrmSessionManager(UUID uuid,
                                                                           String licenseUrl, Map<String, String> keyRequestProperties) throws UnsupportedDrmException {
        if (Util.SDK_INT < 18) {
            return null;
        }
        try {
            HttpMediaDrmCallback drmCallback = new HttpMediaDrmCallback(licenseUrl,
                    buildHttpDataSourceFactory(false), keyRequestProperties);
            return new StreamingDrmSessionManager<>(uuid,
                    FrameworkMediaDrm.newInstance(uuid), drmCallback, null, mainHandler, eventLogger);
        }catch (Exception e){
            return null;
        }

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
        titleText.setText(this.title);
        if(isLiveTV) return;
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
                    Button button = new Button(getActivity());
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
                    button.setFocusable(true);
                    button.setPadding(2,2,2,2);
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

        }
        else if (playbackState == ExoPlayer.STATE_BUFFERING) {
            progressBarView.setVisibility(View.VISIBLE);
        }
        else {
//        if (playbackState == ExoPlayer.STATE_READY) {
            progressBarView.setVisibility(View.GONE);
        }
        Tracking.getInstance().setAction((this.title));
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
        if(e.getCause() instanceof BehindLiveWindowException)
        {
            player.prepare(mediaSource, !isTimelineStatic, !isTimelineStatic);
            return;
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
        if(player != null){
            episodePosition = player.getCurrentWindowIndex();
            Intent intent = getActivity().getIntent();
            this.title=intent.getStringExtra("title") == null ? "" :intent.getStringExtra("title") + ((seasonPosition == -1) ? "": " S" + (seasonPosition+1)) + ((seasonPosition == -1 || episodePosition == -1? "":" E"+ (episodePosition +1)));
            Tracking.getInstance().track();
        }

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
        if(!isLiveTV)
            debugRootView.setVisibility(View.VISIBLE);
        top_bar.setVisibility(View.VISIBLE);
    }

    private void showToastError() {
        if(liveTVToggleListener != null)
            liveTVToggleListener.onToggleUI(true);
        Dialogs.showTwoButtonsDialog(getActivity(),R.string.ok_dialog,R.string.cancel,R.string.generic_video_loading_message, new DialogListener() {

            @Override
            public void onAccept() {
                try{
                    this.onDismiss();
                    startActivity(new Intent(getActivity(), SpeedTestActivity.class));
                    getActivity().finish();
                }catch (Exception e){

                }
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onDismiss() {

            }
        });
    }
}
