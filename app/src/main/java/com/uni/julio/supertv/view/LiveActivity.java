package com.uni.julio.supertv.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.uni.julio.supertv.LiveTvApplication;
import com.uni.julio.supertv.R;
import com.uni.julio.supertv.databinding.ActivityLiveBinding;
import com.uni.julio.supertv.listeners.LiveProgramSelectedListener;
import com.uni.julio.supertv.listeners.LiveTVToggleUIListener;
import com.uni.julio.supertv.model.LiveProgram;
import com.uni.julio.supertv.view.exoplayer.VideoPlayFragment;
import com.uni.julio.supertv.viewmodel.Lifecycle;
import com.uni.julio.supertv.viewmodel.LiveTVViewModel;
import com.uni.julio.supertv.viewmodel.LiveTVViewModelContract;

public class LiveActivity extends BaseActivity  implements LiveProgramSelectedListener , LiveTVToggleUIListener, LiveTVViewModelContract.View{
    private VideoPlayFragment videoPlayFragment;
    private LiveTVViewModel liveTVViewModel;
    private LiveProgramSelectedListener liveProgramSelectedListener;
    private ActivityLiveBinding activityLiveBinding;
    @Override
    protected Lifecycle.ViewModel getViewModel() {
        return liveTVViewModel;
    }

    @Override
    protected Lifecycle.View getLifecycleView() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);
        videoPlayFragment=new VideoPlayFragment();
        videoPlayFragment.hideControls(this);
        FragmentManager manager=getSupportFragmentManager();
         FragmentTransaction transaction=manager.beginTransaction();
         transaction.add(R.id.exo_player,videoPlayFragment,"Frag_top_tag");
         transaction.commit();
         liveProgramSelectedListener=this;
         liveTVViewModel=new LiveTVViewModel(this);
         activityLiveBinding= DataBindingUtil.setContentView(this,R.layout.activity_live);
         activityLiveBinding.setLiveTVFragmentVM(liveTVViewModel);
         liveTVViewModel.showProgramList(activityLiveBinding);
     }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.general, menu);
        return true;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishActivity();
            return true;
        }
        if(keyCode==KeyEvent.KEYCODE_DPAD_CENTER){
            liveTVViewModel.toggleChannels();
        }

        return false;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLiveProgramSelected(LiveProgram liveProgram, int programPosition) {
        String[] uris = new String[] {liveProgram.getStreamUrl()};
        String movieUrl = liveProgram.getStreamUrl().replace(".mkv.mkv", ".mkv").replace(".mp4.mp4", ".mp4");
        String extension = liveProgram.getStreamUrl().substring(movieUrl.lastIndexOf(".") + 1);
        String[] extensions = new String[] {extension};
        Intent launchIntent = new Intent(LiveTvApplication.getAppContext(), VideoPlayActivity.class);
        launchIntent.putExtra(VideoPlayFragment.URI_LIST_EXTRA, uris)
                .putExtra(VideoPlayFragment.EXTENSION_LIST_EXTRA, extensions)
                .putExtra("title",liveProgram.getTitle())
                .setAction(VideoPlayFragment.ACTION_VIEW_LIST);
         videoPlayFragment = (VideoPlayFragment)getSupportFragmentManager().findFragmentById(R.id.exo_player);
        videoPlayFragment.onNewIntent(launchIntent);
        videoPlayFragment.onStart();
        videoPlayFragment.onResume();
    }
    @Override
    public void onToggleUI(boolean show) {
            liveTVViewModel.toggleChannels();
    }

    @Override
    public void onProgramAccepted(LiveProgram liveProgram) {
        onLiveProgramSelected(liveProgram,0);
    }

    @Override
    public void showActionBar() {

    }

    @Override
    public void hideActionBar() {

    }
}
