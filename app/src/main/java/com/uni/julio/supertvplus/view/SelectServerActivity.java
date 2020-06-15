package com.uni.julio.supertvplus.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;

import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.uni.julio.supertvplus.R;
import com.uni.julio.supertvplus.adapter.ServerAdapter;
import com.uni.julio.supertvplus.helper.DividerDecoration;
import com.uni.julio.supertvplus.helper.TVRecyclerView;
import com.uni.julio.supertvplus.listeners.LiveProgramSelectedListener;
import com.uni.julio.supertvplus.model.LiveProgram;
import com.uni.julio.supertvplus.utils.Device;
import com.uni.julio.supertvplus.utils.Dialogs;
import java.util.HashMap;
import java.util.List;
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
            if(getSupportActionBar() != null){
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }
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
            if (serverRv.getItemDecorationCount() == 0)
                serverRv.addItemDecoration(new DividerDecoration(this, R.drawable.divider));
            searchInput.setOnEditorActionListener(new EditText.OnEditorActionListener(){
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                            actionId == EditorInfo.IME_ACTION_DONE ||
                            event != null &&
                                    event.getAction() == KeyEvent.ACTION_DOWN &&
                                    event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        serverAdapter.setSearchResult(v.getText().toString());
                        hideKeyboard();
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
        if(imm != null)
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
