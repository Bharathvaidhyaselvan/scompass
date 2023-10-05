package com.packages.scompass;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AboutUsActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);


        bottomNavigationView = findViewById(R.id.bottom_navigation);
        TextView instagramArish = findViewById(R.id.instagram_arish);
        TextView instagramBVS = findViewById(R.id.instagram_bvs);
        ImageView facebookImageView = findViewById(R.id.facebook);
        ImageView instagramImageView = findViewById(R.id.instagram);
        ImageView twitterImageView = findViewById(R.id.twitter);



        instagramArish.setOnClickListener(view -> openInstagramProfile("https://www.instagram.com/arishragaventhra/"));
        instagramBVS.setOnClickListener(view -> openInstagramProfile("https://www.instagram.com/bharathbvs/"));

        facebookImageView.setOnClickListener(view -> openUrl("https://www.facebook.com/people/Scompass/100087396342680/"));
        instagramImageView.setOnClickListener(view -> openUrl("https://www.instagram.com/_scompass_/"));
        twitterImageView.setOnClickListener(view -> openUrl("https://twitter.com/_Scompass_"));



        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.about_menu) {
                return true;
            } else if (itemId == R.id.home_menu) {
                startActivityWithSelectedMenuItem(HomeActivity.class, R.id.home_menu);
                return true;
            } else if (itemId == R.id.package_menu) {
                startActivityWithSelectedMenuItem(MomentsActivity.class, R.id.package_menu);
                return true;
            } else if (itemId == R.id.map_menu) {
                startActivityWithSelectedMenuItem(MapsActivity.class, R.id.map_menu);
                return true;
            } else if (itemId == R.id.moments_menu) {
                startActivityWithSelectedMenuItem(AboutActivity.class, R.id.moments_menu);
                return true;
            }
            return false;
        });

        int selectedMenuItemId = getIntent().getIntExtra("selectedMenuItemId", R.id.about_menu);
        bottomNavigationView.setSelectedItemId(selectedMenuItemId);

        if (!isNetworkConnected()) {
            Intent intent = new Intent(AboutUsActivity.this, NoConnectionActivity.class);
            startActivity(intent);
            finish();
        }


    }

    private void openInstagramProfile(String instagramUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(instagramUrl));
        startActivity(intent);
    }

    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void startActivityWithSelectedMenuItem(Class<?> destinationActivity, int selectedMenuItemId) {
        Intent intent = new Intent(this, destinationActivity);
        intent.putExtra("selectedMenuItemId", selectedMenuItemId);
        startActivity(intent);
    }

    public boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
