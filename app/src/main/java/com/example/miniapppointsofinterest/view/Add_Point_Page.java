package com.example.miniapppointsofinterest.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatRadioButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.miniapppointsofinterest.My_Screen_Utils;
import com.example.miniapppointsofinterest.My_Signal;
import com.example.miniapppointsofinterest.R;
import com.example.miniapppointsofinterest.api.ObjectApi;
import com.example.miniapppointsofinterest.api.RetrofitClient;
import com.example.miniapppointsofinterest.model.CurrentUser;
import com.example.miniapppointsofinterest.model.object.CreatedBy;
import com.example.miniapppointsofinterest.model.object.LocationBoundary;
import com.example.miniapppointsofinterest.model.object.ObjectBoundary;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;

import com.google.android.material.button.MaterialButton;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Add_Point_Page extends AppCompatActivity {

    private static final int IMAGE_UPLOAD_REQUEST_CODE = 1;
    private RadioGroup addPoint_RAG_radiogroup;
    private AppCompatRadioButton addPoint_RAB_private;
    private AppCompatRadioButton addPoint_RAB_Public;
    private AppCompatEditText addPoint_EDT_pointName;
    private AppCompatEditText addPoint_EDT_description;
    private Spinner addPoint_SPN_type;
    private MaterialButton addPoint_BTN_addPoint;
    private ObjectApi mApi;
    private MaterialButton addPoint_BTN_confirmLocation;
    private SupportMapFragment mapFragment;
    private GoogleMap myMap;
    private Marker selectedLocationMarker;
    private boolean isLocationSelected = false;
    private Button addPoint_BTN_uploadImage;
    private ImageView imageView;

    private double lat ,lng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_point_page);
        My_Screen_Utils.hideSystemUI(this);
        mApi = RetrofitClient.getInstance().create(ObjectApi.class);
        startView();

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        assert mapFragment != null;

        addPoint_BTN_uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionAndUploadImage();
            }
        });

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                myMap = googleMap;
                LatLng israel = new LatLng(31.0461, 34.8516);
                myMap.addMarker(new MarkerOptions().position(israel).title("Marker in israel"));
                myMap.moveCamera(CameraUpdateFactory.newLatLng(israel));

                myMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        // Clear any existing marker
                        if (selectedLocationMarker != null) {
                            selectedLocationMarker.remove();
                        }

                        // Add a marker at the clicked location
                        selectedLocationMarker = myMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title("Selected Location"));

                        // Update the flag to indicate a location is selected
                        isLocationSelected = true;
                    }
                });

                addPoint_BTN_confirmLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isLocationSelected) {
                            // Get the selected location LatLng
                            LatLng selectedLocation = selectedLocationMarker.getPosition();

                            // TODO: save the selected location in the database

                            // Show a toast message to indicate the selected location
                            String message = "Selected Location: " + selectedLocation.latitude + ", " + selectedLocation.longitude;
                            Toast.makeText(Add_Point_Page.this, message, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Add_Point_Page.this, "Please select a location on the map", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        addPoint_BTN_addPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLocationSelected) {
                    // Get the selected location LatLng
                    LatLng selectedLocation = selectedLocationMarker.getPosition();

                    // Extract the latitude and longitude
                    lat = selectedLocation.latitude;
                    lng = selectedLocation.longitude;

                    // TODO: save the latitude and longitude in the database

                    // Show a toast message to indicate the selected location
                    String message = "Selected Location: " + lat + ", " + lng;
                    Toast.makeText(Add_Point_Page.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Add_Point_Page.this, "Please select a location on the map", Toast.LENGTH_SHORT).show();
                }

                //TODO: check that the image is ok
                createObject(addPoint_EDT_pointName.getText().toString(),lat, lng,
                        addPoint_EDT_description.getText().toString(),
                        addPoint_BTN_uploadImage.getText().toString(),
                        addPoint_SPN_type.getSelectedItem().toString());
            }
        });
    }


    private void checkPermissionAndUploadImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Check if permission to read external storage is granted
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Request the permission if not granted
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        IMAGE_UPLOAD_REQUEST_CODE);
            } else {
                // Permission already granted, start image selection process
                openImagePicker();
            }
        } else {
            // No runtime permission required for lower API levels, start image selection process
            openImagePicker();
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_UPLOAD_REQUEST_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == IMAGE_UPLOAD_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start image selection process
                openImagePicker();
            } else {
                // Permission denied
                Log.e("Permission Denied", "Storage permission denied");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_UPLOAD_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri imageUri = data.getData();
                ImageView imageView = findViewById(R.id.imageView);
                imageView.setImageURI(imageUri);
            }
        }
    }


    private void createObject(String alias, Double lat, Double lng, String description , String image , String type) {
        ObjectBoundary newObject = new ObjectBoundary();
        newObject.setType("Point");
        newObject.setAlias(alias);
        newObject.setActive(true);
        newObject.setLocation(new LocationBoundary(lat, lng));
        newObject.setCreatedBy(new CreatedBy(CurrentUser.getInstance().getTheUser().getUserId()));
        HashMap<String, Object> details = new HashMap<String, Object>();
        details.put("description", description);
        details.put("image", image);
        details.put("type", type);
        newObject.setObjectDetails(details);
        mApi.createObject(newObject).enqueue(new Callback<ObjectBoundary>() {
            @Override
            public void onResponse(Call<ObjectBoundary> call, Response<ObjectBoundary> response) {
                if (response.isSuccessful()) {
                    createNewObject(response.body());
                } else {
                    My_Signal.getInstance().toast("API call failed: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<ObjectBoundary> call, Throwable t) {
                My_Signal.getInstance().toast("API call failed: " + t.getMessage());
            }
        });
    }

    private void createNewObject(ObjectBoundary body){
        My_Signal.getInstance().toast("the point added");
        Intent intent = new Intent(this, Home_Page.class);
        startActivity(intent);
    }

    private void startView() {
        addPoint_EDT_pointName = findViewById(R.id.addPoint_EDT_pointName);
        addPoint_EDT_description = findViewById(R.id.addPoint_EDT_description);
        addPoint_BTN_uploadImage = findViewById(R.id.addPoint_BTN_uploadImage);
        addPoint_SPN_type = findViewById(R.id.addPoint_SPN_type);
        addPoint_BTN_addPoint = findViewById(R.id.addPoint_BTN_addPoint);
        addPoint_BTN_confirmLocation = findViewById(R.id.addPoint_BTN_confirmLocation);
        imageView = findViewById(R.id.imageView);
        String[] typesArray = getResources().getStringArray(R.array.types);
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item,typesArray);
        addPoint_SPN_type.setAdapter(typeAdapter);
    }
}