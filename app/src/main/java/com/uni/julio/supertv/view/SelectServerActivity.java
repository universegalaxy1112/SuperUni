package com.uni.julio.supertv.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.uni.julio.supertv.LiveTvApplication;
import com.uni.julio.supertv.R;
import com.uni.julio.supertv.adapter.GridViewAdapter;
import com.uni.julio.supertv.adapter.ServerAdapter;
import com.uni.julio.supertv.databinding.ActivityMoviesBinding;
import com.uni.julio.supertv.helper.RecyclerViewItemDecoration;
import com.uni.julio.supertv.helper.TVRecyclerView;
import com.uni.julio.supertv.helper.VideoStreamManager;
import com.uni.julio.supertv.listeners.LiveProgramSelectedListener;
import com.uni.julio.supertv.model.LiveProgram;
import com.uni.julio.supertv.model.ModelTypes;
import com.uni.julio.supertv.model.Movie;
import com.uni.julio.supertv.model.Serie;
import com.uni.julio.supertv.utils.Device;
import com.uni.julio.supertv.utils.Dialogs;
import com.uni.julio.supertv.view.exoplayer.VideoPlayFragment;
import com.uni.julio.supertv.viewmodel.Lifecycle;
import com.uni.julio.supertv.viewmodel.MoviesMenuViewModel;
import com.uni.julio.supertv.viewmodel.MoviesMenuViewModelContract;

import java.util.HashMap;
import java.util.List;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

public class SelectServerActivity extends AppCompatActivity implements LiveProgramSelectedListener {
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_selectserver);
            Bundle extras = getIntent().getExtras();
            HashMap<Integer, List<String>> serverList = (HashMap<Integer, List<String>>) extras.get("SERVERS");
            Toolbar toolbar = findViewById(R.id.toolbar);
            EditText searchInput = findViewById(R.id.location);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                toolbar.setElevation(0);
            }
            if(Device.treatAsBox){
                findViewById(R.id.appBarLayout).setVisibility(View.GONE);
            }
            TVRecyclerView serverRv = findViewById(R.id.server_recycler);
            final ServerAdapter serverAdapter = new ServerAdapter(this, serverList, serverRv, this);
            GridLayoutManager mLayoutManager = new GridLayoutManager(this, 1);
            mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            serverRv.setLayoutManager(mLayoutManager);
            serverRv.setAdapter(serverAdapter);
            if(serverRv.getItemDecorationCount() == 0 ){
                serverRv.addItemDecoration(new RecyclerViewItemDecoration(12,12,12,12));
            }
            searchInput.setOnEditorActionListener(new EditText.OnEditorActionListener(){
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                            actionId == EditorInfo.IME_ACTION_DONE ||
                            event != null &&
                                    event.getAction() == KeyEvent.ACTION_DOWN &&
                                    event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        serverAdapter.setSearchResult(v.getText().toString());
                    }
                    return false;
                }
            });
            searchInput.requestFocus();
        }catch (Exception e){
            Dialogs.showOneButtonDialog(this, R.string.exception_title, R.string.exception_content, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    finish();
                }
            });
            e.printStackTrace();
        }
    }

    public  void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onLiveProgramSelected(LiveProgram liveProgram, int programPosition) {
        Intent intent = new Intent();
        intent.putExtra("serverIndex", programPosition);
        setResult(100, intent);
        finish();
    }
}
