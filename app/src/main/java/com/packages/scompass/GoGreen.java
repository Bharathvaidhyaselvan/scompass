package com.packages.scompass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;


import com.google.android.material.bottomnavigation.BottomNavigationView;

public class GoGreen extends AppCompatActivity {


    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_green);

        bottomNavigationView = findViewById(R.id.bottom_navigation);


        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home_menu) {
                return true;
            } else if (itemId == R.id.package_menu) {
                startActivityWithSelectedMenuItem(PackagesActivity.class, R.id.package_menu);
                return true;
            } else if (itemId == R.id.moments_menu) {
                startActivityWithSelectedMenuItem(MomentsActivity.class, R.id.moments_menu);
                return true;
            } else if (itemId == R.id.map_menu) {
                startActivityWithSelectedMenuItem(MapsActivity.class, R.id.map_menu);
                return true;
            } else if (itemId == R.id.about_menu) {
                startActivityWithSelectedMenuItem(AboutActivity.class, R.id.about_menu);
                return true;
            }
            return false;
        });
        int selectedMenuItemId = getIntent().getIntExtra("selectedMenuItemId", R.id.home_menu);
        bottomNavigationView.setSelectedItemId(selectedMenuItemId);
    }

    private void startActivityWithSelectedMenuItem(Class<?> destinationActivity, int selectedMenuItemId) {
        Intent intent = new Intent(this, destinationActivity);
        intent.putExtra("selectedMenuItemId", selectedMenuItemId);
        startActivity(intent);
    }
}