package com.uni.julio.supertv.view;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.uni.julio.supertv.R;
import com.uni.julio.supertv.helper.TVRecyclerView;
import com.uni.julio.supertv.model.MainCategory;
import com.uni.julio.supertv.model.ModelTypes;
import com.uni.julio.supertv.utils.DataManager;
import com.uni.julio.supertv.utils.Device;
import com.uni.julio.supertv.viewmodel.Lifecycle;
import com.uni.julio.supertv.viewmodel.MainCategoriesMenuViewModel;
import com.uni.julio.supertv.viewmodel.MainCategoriesMenuViewModelContract;


public class MainActivity extends BaseActivity implements MainCategoriesMenuViewModelContract.View{
    private MainCategoriesMenuViewModel mainCategoriesMenuViewModel;
     @Override
    protected Lifecycle.ViewModel getViewModel() {
        return mainCategoriesMenuViewModel;
    }

    @Override
    protected Lifecycle.View getLifecycleView() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainCategoriesMenuViewModel = new MainCategoriesMenuViewModel(this.getBaseContext());
        setContentView(R.layout.activity_main);
        getViewModel().onViewAttached(getLifecycleView());
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("SuperTV");
         setSupportActionBar(toolbar);
        if(Device.treatAsBox){
            findViewById(R.id.Appbarlayout).setVisibility(View.GONE);
        }
        TVRecyclerView mainCategoryRecycler=findViewById(R.id.maincategory);
        mainCategoriesMenuViewModel.showMainCategories(mainCategoryRecycler);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            android.os.Process.killProcess(android.os.Process.myPid());
            return true;
        }
        return false;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_setting) {
            onAccountPressed();
        }else if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMainCategorySelected(MainCategory mainCategory) {
        int mainCategoryId = mainCategory.getId();
        if(mainCategoryId==8){
            onAccountPressed();
            return;
        }
        if(mainCategoryId == 7) {
            String pin=DataManager.getInstance().getString("adultsPassword", "");
            if(TextUtils.isEmpty(pin)) {
                //openSetPasswordDialog();
             }
            else{
                openPasswordDialog(pin);
            }
        }
        else {
            Bundle extras = new Bundle();
            extras.putSerializable("selectedType", ModelTypes.SelectedType.MAIN_CATEGORY);
            extras.putInt("mainCategoryId", mainCategory.getId());
            launchActivity(LoadingActivity.class, extras);
        }
    }
    private void openPasswordDialog(String pin) {
        new MaterialDialog.Builder(this)
                .title(R.string.prompt_password)
                .content(R.string.adults_password_message)
                .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
                .input("", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {

                        if(input.toString().equals(DataManager.getInstance().getString("adultsPassword", ""))) {
                            Bundle extras = new Bundle();
                            extras.putSerializable("selectedType", ModelTypes.SelectedType.MAIN_CATEGORY);
                            extras.putInt("mainCategoryId", 7);
                            launchActivity(LoadingActivity.class, extras);
                        }
                        else {
                            dialog.dismiss();
                            Toast.makeText(getBaseContext(), R.string.error_invalid_password, Toast.LENGTH_SHORT).show();
                        }
                     }
                })
                .backgroundColor( getResources().getColor(R.color.white))
                .positiveColorRes(R.color.netflix_red)
                .negativeColorRes(R.color.netflix_red)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .contentColorRes(R.color.bg_general)
                .titleColorRes(R.color.bg_general)
                .contentLineSpacing(1)
                .show();
    }

    @Override
    public void onAccountPressed() {
        launchActivity(AccountActivity.class);


    }
}
