package com.cscodetech.movers.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.cscodetech.movers.R;
import com.cscodetech.movers.fragment.HomeFragment;
import com.cscodetech.movers.fragment.ProfileFragment;
import com.cscodetech.movers.ui.findlorry.MyBookLoadActivity;
import com.cscodetech.movers.ui.postload.MyPostLoadActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity {

    @BindView(R.id.bottom_navigation)
    public BottomNavigationView bottomNavigation;
    @BindView(R.id.container)
    public FrameLayout container;

    HomeActivity activity;
    public HomeActivity getInstance(){
        if(activity == null){
            activity = new HomeActivity();
        }
        return activity;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        activity =this;
        ActivityCompat.requestPermissions(this,
                new String[] {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.CALL_PHONE
                }, 1010);



        bottomNavigation.setOnItemSelectedListener(item -> {
            // Handle navigation item selection
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {

                openFragment(new HomeFragment());

            } else if (itemId == R.id.navigation_sms) {
                item.setCheckable(false);

                startActivity(new Intent(HomeActivity.this, MyPostLoadActivity.class));

            } else if (itemId == R.id.navigation_notifications) {
                item.setCheckable(false);

                startActivity(new Intent(HomeActivity.this, MyBookLoadActivity.class));

            } else if (itemId == R.id.navigation_notifications2) {

                openFragment(new ProfileFragment());

            }
            return true;

        });

        openFragment(new HomeFragment());

    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void profileMenuClick() {
        Log.e("click","99999");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment instanceof HomeFragment && fragment.isVisible()) {
            //DO STUF
            Log.e("dada","active");
        }
        else {
            finish();
            //Whatever
            Log.e("dada","Inactive");

        }
    }






}