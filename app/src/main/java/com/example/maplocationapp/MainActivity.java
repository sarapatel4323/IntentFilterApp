package com.example.maplocationapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private Button btnGoToMap;
    private Button btnOpenBrowser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);

        // Initialize buttons
        btnGoToMap = findViewById(R.id.btnGoToMap);
        btnOpenBrowser = findViewById(R.id.btnOpenBrowser);

        // Set onClick listeners
        btnGoToMap.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MapViewActivity.class);
            startActivity(intent);
        });

        btnOpenBrowser.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HumberBrowserActivity.class);
            startActivity(intent);
        });
    }
}
