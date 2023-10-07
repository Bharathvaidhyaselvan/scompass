package com.packages.scompass;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            mDatabase.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User userData = dataSnapshot.getValue(User.class);
                    if (userData != null) {
                        userName = userData.getName();
                        SharedPreferences.Editor s = getSharedPreferences("USER", MODE_PRIVATE).edit();
                        s.putString("user_name", userName);
                        s.apply();
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
