package com.example.maplocationapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.google.android.gms.maps.model.LatLng;

public class FavoriteLocationFragment extends DialogFragment {

    private EditText titleInput, descriptionInput;
    private RatingBar ratingInput;
    private Button btnSave, btnCancel;
    private OnSaveLocationListener saveLocationListener;
    private LatLng location;

    public interface OnSaveLocationListener {
        void onSaveLocation(String title, String description, float rating, LatLng location);
    }

    public static FavoriteLocationFragment newInstance(LatLng location) {
        FavoriteLocationFragment fragment = new FavoriteLocationFragment();
        Bundle args = new Bundle();
        args.putParcelable("location", location);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnSaveLocationListener) {
            saveLocationListener = (OnSaveLocationListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnSaveLocationListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_location, container, false);

        // Initialize UI elements
        titleInput = view.findViewById(R.id.titleInput);
        descriptionInput = view.findViewById(R.id.descriptionInput);
        ratingInput = view.findViewById(R.id.ratingInput);
        btnSave = view.findViewById(R.id.btnSave);
        btnCancel = view.findViewById(R.id.btnCancel);

        if (getArguments() != null) {
            location = getArguments().getParcelable("location");
        }

        btnSave.setOnClickListener(v -> {
            String title = titleInput.getText().toString();
            String description = descriptionInput.getText().toString();
            float rating = ratingInput.getRating();
            saveLocationListener.onSaveLocation(title, description, rating, location);
            dismiss();
        });

        btnCancel.setOnClickListener(v -> dismiss());

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        saveLocationListener = null;
    }
}
