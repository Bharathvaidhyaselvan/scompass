package com.packages.scompass;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.Manifest;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CoffeeActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
        }

        webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url.startsWith("https://scompass.in/map-coffee/")) {

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        webView.loadUrl("https://scompass.in/map-coffee/");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home_menu) {
                startActivityWithSelectedMenuItem(PackagesActivity.class, R.id.home_menu);
                return true;
            } else if (itemId == R.id.package_menu) {
                startActivityWithSelectedMenuItem(PackagesActivity.class, R.id.package_menu);
                return true;
            } else if (itemId == R.id.moments_menu) {
                startActivityWithSelectedMenuItem(MomentsActivity.class, R.id.moments_menu);
                return true;
            } else if (itemId == R.id.map_menu) {
                return true;
            } else if (itemId == R.id.about_menu) {
                startActivityWithSelectedMenuItem(AboutActivity.class, R.id.about_menu);
                return true;
            }
            return false;
        });
        int selectedMenuItemId = getIntent().getIntExtra("selectedMenuItemId", R.id.map_menu);
        bottomNavigationView.setSelectedItemId(selectedMenuItemId);

    }

    private void startActivityWithSelectedMenuItem(Class<?> destinationActivity, int selectedMenuItemId) {
        Intent intent = new Intent(this, destinationActivity);
        intent.putExtra("selectedMenuItemId", selectedMenuItemId);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(CoffeeActivity.this, "Sorry, We couldn't load your location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
