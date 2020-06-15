package com.uni.julio.supertvplus.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import com.uni.julio.supertvplus.R;
import com.uni.julio.supertvplus.databinding.ActivitySearchBinding;
import com.uni.julio.supertvplus.helper.VideoStreamManager;
import com.uni.julio.supertvplus.model.ModelTypes;
import com.uni.julio.supertvplus.utils.Device;
import com.uni.julio.supertvplus.utils.Dialogs;
import com.uni.julio.supertvplus.viewmodel.Lifecycle;
import com.uni.julio.supertvplus.viewmodel.SearchViewModel;
import com.uni.julio.supertvplus.viewmodel.SearchViewModelContract;

public class SearchActivity extends BaseActivity implements SearchViewModelContract.View {
    private SearchViewModel searchViewModel;
    private ActivitySearchBinding activitySearchBinding;
    @Override
    protected Lifecycle.ViewModel getViewModel() {
        return searchViewModel;
    }

    @Override
    protected Lifecycle.View getLifecycleView() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            String query = "";
            Bundle extras = getActivity().getIntent().getExtras();
            if(extras != null) {
                selectedType = (ModelTypes.SelectedType) extras.get("selectedType");
                mainCategoryId = extras.getInt("mainCategoryId",0);
                movieCategoryId = extras.getInt("movieCategoryId",0);
                query=extras.getString("query","");
            }

            searchViewModel=new SearchViewModel(this, mainCategoryId);
            activitySearchBinding= DataBindingUtil.setContentView(this, R.layout.activity_search);
            activitySearchBinding.setSearchFM(searchViewModel);
            Toolbar toolbar=activitySearchBinding.toolbar;
            toolbar.setTitle(VideoStreamManager.getInstance().getMainCategory(mainCategoryId).getCatName()+"->"+query);
            setSupportActionBar(toolbar);
            if(getSupportActionBar() != null){
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }
            if(Device.treatAsBox){
                findViewById(R.id.appBarLayout).setVisibility(View.GONE);
            }
            boolean searchSerie=false;
            if((mainCategoryId == 1 || mainCategoryId == 2) && selectedType == ModelTypes.SelectedType.MAIN_CATEGORY) {
                searchSerie = true;
            }
            if(query.equals("") && Device.treatAsBox){
                (activitySearchBinding.editPassword).setOnEditorActionListener(new EditText.OnEditorActionListener(){

                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_DONE ||
                                event != null &&
                                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            if (event == null || !event.isShiftPressed()) {
                                hideKeyboard();
                                searchViewModel.showMovieList(activitySearchBinding,activitySearchBinding.searchRecycler,v.getText().toString(),true);
                                return true;
                            }
                        }
                        return false;
                    }
                });
                activitySearchBinding.editPassword.requestFocus();
            }
            else{
                activitySearchBinding.editPassword.setVisibility(View.GONE);
                searchViewModel.showMovieList(activitySearchBinding,activitySearchBinding.searchRecycler,query,searchSerie);
            }
        }catch (Exception e){
            e.printStackTrace();
            Dialogs.showOneButtonDialog(getActivity(), R.string.exception_title, R.string.exception_content, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    getActivity().finish();
                }
            });
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishActivity();
            return true;
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
    public boolean onCreateOptionsMenu(Menu menu){

        return true;
    }


}
