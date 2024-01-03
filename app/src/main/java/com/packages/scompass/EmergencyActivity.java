package com.packages.scompass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class EmergencyActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        CardView cardView1 = findViewById(R.id.card1);
        CardView cardView2 = findViewById(R.id.card2);
        CardView cardView3 = findViewById(R.id.card3);
        CardView cardView4 = findViewById(R.id.card4);
        CardView cardView5 = findViewById(R.id.card5);
        CardView cardView6 = findViewById(R.id.card6);

        cardView1.setOnClickListener(v -> {
            String phoneNumber = "100";

            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
            startActivity(intent);
        });

        cardView2.setOnClickListener(v -> {
            String phoneNumber = "108";

            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
            startActivity(intent);
        });

        cardView3.setOnClickListener(v -> {
            String phoneNumber = "1098";

            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
            startActivity(intent);
        });

        cardView4.setOnClickListener(v -> {
            String phoneNumber = "101";

            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
            startActivity(intent);
        });

        cardView5.setOnClickListener(v -> {
            String phoneNumber = "112";

            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
            startActivity(intent);
        });

        cardView6.setOnClickListener(v -> {
            String phoneNumber = "181";

            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
            startActivity(intent);
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home_menu) {
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