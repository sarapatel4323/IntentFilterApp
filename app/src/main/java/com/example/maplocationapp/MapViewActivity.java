package com.example.maplocationapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Map;

public class MapViewActivity extends AppCompatActivity implements OnMapReadyCallback, FavoriteLocationFragment.OnSaveLocationListener {

    private GoogleMap mMap;
    private MapView mapView;
    private SharedPreferences sharedPreferences;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private static final String FAVORITES_PREFIX = "FavoriteLocation_";
    private static final String TAG = "MapViewActivity"; // For logging

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarMap);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("com.example.maplocationapp", Context.MODE_PRIVATE);

        // Initialize MapView
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Center map on user location
        LatLng userLocation = new LatLng(-34, 151);  // Replace with actual location
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 10));

        // Load and display all favorite markers when the map is ready
        loadAllFavoriteMarkers();

        // Set a map click listener to add a new favorite
        mMap.setOnMapClickListener(latLng -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FavoriteLocationFragment favoriteLocationFragment = FavoriteLocationFragment.newInstance(latLng);
            favoriteLocationFragment.show(fragmentManager, "FavoriteLocationFragment");
        });

        // Set a long-click listener on info windows to remove markers with confirmation
        mMap.setOnInfoWindowLongClickListener(marker -> {
            showDeleteConfirmationDialog(marker);
        });
    }

    private void showDeleteConfirmationDialog(Marker marker) {
        new AlertDialog.Builder(this)
                .setTitle("Remove Favorite Location")
                .setMessage("Are you sure you want to remove this favorite location?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Remove the marker from SharedPreferences and map
                    removeFavoriteLocation(marker.getPosition());
                    marker.remove();
                    loadAllFavoriteMarkers();  // Reload markers to ensure consistency
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    public void onSaveLocation(String title, String description, float rating, LatLng location) {
        // Save the favorite location in SharedPreferences using a unique key
        saveFavoriteLocation(title, description, rating, location);

        // Add a marker for the new favorite location
        mMap.addMarker(new MarkerOptions().position(location).title(title));
    }

    private void saveFavoriteLocation(String title, String description, float rating, LatLng location) {
        String key = FAVORITES_PREFIX + location.latitude + "_" + location.longitude;
        JSONObject favoriteLocation = new JSONObject();
        try {
            favoriteLocation.put("title", title);
            favoriteLocation.put("description", description);
            favoriteLocation.put("rating", rating);
            favoriteLocation.put("latitude", location.latitude);
            favoriteLocation.put("longitude", location.longitude);

            sharedPreferences.edit().putString(key, favoriteLocation.toString()).apply();
            Log.d(TAG, "Saved new favorite location with key: " + key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadAllFavoriteMarkers() {
        mMap.clear();  // Clear existing markers

        Map<String, ?> allEntries = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if (entry.getKey().startsWith(FAVORITES_PREFIX)) {
                try {
                    JSONObject favoriteLocation = new JSONObject(entry.getValue().toString());
                    LatLng location = new LatLng(favoriteLocation.getDouble("latitude"), favoriteLocation.getDouble("longitude"));
                    String title = favoriteLocation.getString("title");

                    // Add marker for each favorite location
                    mMap.addMarker(new MarkerOptions().position(location).title(title));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d(TAG, "Loaded all favorite markers");
    }

    private void removeFavoriteLocation(LatLng location) {
        String key = FAVORITES_PREFIX + location.latitude + "_" + location.longitude;

        if (sharedPreferences.contains(key)) {
            sharedPreferences.edit().remove(key).apply();
            Log.d(TAG, "Removed favorite location with key: " + key);
        } else {
            Log.d(TAG, "Favorite location not found for key: " + key);
        }
    }
}
