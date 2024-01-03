package com.packages.scompass;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private TextView userNameTextView;
    private BottomNavigationView bottomNavigationView;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        userNameTextView = findViewById(R.id.user_name_text_view);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        LinearLayout cardQuoteAlign = findViewById(R.id.card_quote_align);
        TextView viewMoreText = findViewById(R.id.view_more_text);

        CardView cardHotels = findViewById(R.id.hotels);
        CardView cardHangouts = findViewById(R.id.hangouts);
        CardView cardEmbassy = findViewById(R.id.embassy);
        CardView cardCoffee = findViewById(R.id.coffee);
        CardView emergency = findViewById(R.id.emergency);

        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);
        Button button4 = findViewById(R.id.button4);
        Button button5 = findViewById(R.id.button5);

        button2.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, ChennaitoKodaikanal.class);
            startActivity(intent);
        });

        button3.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, ChennaitoOoty.class);
            startActivity(intent);
        });

        button4.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, ChennaitoKollihills.class);
            startActivity(intent);
        });

        button5.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, ChennaiToYelagiriActivity.class);
            startActivity(intent);
        });

        cardHotels.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, HotelsActivity.class);
            startActivity(intent);
        });

        cardHangouts.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, HangoutsActivity.class);
            startActivity(intent);
        });

        cardEmbassy.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, EmbassyActivity.class);
            startActivity(intent);
        });

        cardCoffee.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CoffeeActivity.class);
            startActivity(intent);
        });

        cardQuoteAlign.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, GoGreen.class);
            startActivity(intent);
        });

        emergency.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, EmergencyActivity.class);
            startActivity(intent);
        });

        if (!isNetworkConnected()) {
            Intent intent = new Intent(this, NoConnectionActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        if (!areNotificationsEnabled()) {
            showEnableNotificationsDialog();
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId != R.id.home_menu) {
                startActivityWithSelectedMenuItem(PackagesActivity.class, itemId);
            }
            return true;
        });

        int selectedMenuItemId = getIntent().getIntExtra("selectedMenuItemId", R.id.home_menu);
        bottomNavigationView.setSelectedItemId(selectedMenuItemId);

        viewMoreText.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, PackagesActivity.class);
            startActivity(intent);
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            mDatabase.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User userData = dataSnapshot.getValue(User.class);
                    if (userData != null) {
                        userName = userData.getName();
                        SharedPreferences.Editor editor = getSharedPreferences("USER", MODE_PRIVATE).edit();
                        editor.putString("user_name", userName);
                        editor.apply();
                        userNameTextView.setText(userName);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(HomeActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!areNotificationsEnabled()) {
            showEnableNotificationsDialog();
        }
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

    private boolean areNotificationsEnabled() {
        return true;
    }

    private void showEnableNotificationsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enable Notifications");
        builder.setMessage("Turn on notifications to stay updated with the latest Packages, Events and more.");
        builder.setPositiveButton("Enable", (dialog, which) -> {
            openAppSettings();
            dialog.dismiss();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void openAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(android.net.Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }
}
