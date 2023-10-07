package com.packages.scompass;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AboutActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private DatabaseReference mDatabase;
    private TextView userNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        userNameTextView = findViewById(R.id.user_name_text_view);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.about_menu) {
                return true;
            } else if (itemId == R.id.home_menu) {
                startActivityWithSelectedMenuItem(HomeActivity.class, R.id.home_menu);
                return true;
            } else if (itemId == R.id.package_menu) {
                startActivityWithSelectedMenuItem(PackagesActivity.class, R.id.package_menu);
                return true;
            } else if (itemId == R.id.map_menu) {
                startActivityWithSelectedMenuItem(MapsActivity.class, R.id.map_menu);
                return true;
            } else if (itemId == R.id.moments_menu) {
                startActivityWithSelectedMenuItem(MomentsActivity.class, R.id.moments_menu);
                return true;
            }
            return false;
        });
        int selectedMenuItemId = getIntent().getIntExtra("selectedMenuItemId", R.id.about_menu);
        bottomNavigationView.setSelectedItemId(selectedMenuItemId);

        Button button1 = findViewById(R.id.aboutus);
        button1.setOnClickListener(v -> {
            Intent intent = new Intent(AboutActivity.this, AboutUsActivity.class);
            startActivity(intent);
        });

        Button button15 = findViewById(R.id.button15);
        button15.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(AboutActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            mDatabase.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User userData = dataSnapshot.getValue(User.class);
                    if (userData != null) {
                        String userName = userData.getName();
                        userNameTextView.setText(userName);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            Button button13 = findViewById(R.id.button13);
            button13.setOnClickListener(v -> {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"scompass@gmail.com"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Suggestion");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello, I have a suggestion:");
                startActivity(Intent.createChooser(emailIntent, "Choose an Email Client"));
            });

            Button button11 = findViewById(R.id.button11);
            button11.setOnClickListener(v -> {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"scompass@gmail.com"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Delete My Post");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello, I Want to delete my post            (ATTACH THE POST SCREENSHOT YOU WANT TO REMOVE)");
                startActivity(Intent.createChooser(emailIntent, "Choose an Email Client"));
            });

        }
    }

    private void startActivityWithSelectedMenuItem(Class<?> destinationActivity, int selectedMenuItemId) {
        Intent intent = new Intent(this, destinationActivity);
        intent.putExtra("selectedMenuItemId", selectedMenuItemId);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
