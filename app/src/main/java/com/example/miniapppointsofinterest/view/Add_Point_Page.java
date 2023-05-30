package com.example.miniapppointsofinterest.view;

import android.Manifest;
import android.app.Activity;
import android.app.Instrumentation;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Add_Point_Page extends AppCompatActivity {

    private static final int IMAGE_UPLOAD_REQUEST_CODE = 1;
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
    private MaterialButton addPoint_BTN_uploadImage;
    private ImageView addPoint_IMV_imageView;
    private double lat ,lng;
    private Uri imageUri;
    private String fileName;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_point_page);
        My_Screen_Utils.hideSystemUI(this);
        mApi = RetrofitClient.getInstance().create(ObjectApi.class);
        startView();

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        assert mapFragment != null;


        databaseReference = FirebaseDatabase.getInstance().getReference("Images");
        storageReference = FirebaseStorage.getInstance().getReference("images");

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
                            lat = selectedLocation.latitude;
                            lng = selectedLocation.longitude;

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
                if (!isLocationSelected)
                    Toast.makeText(Add_Point_Page.this, "Please select a location on the map", Toast.LENGTH_SHORT).show();
                uploadImageAndCreateObject();
            }
        });
    }

    private void uploadImageAndCreateObject() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Create new Point...");

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Date now = new Date();
        this.fileName = formatter.format(now);
        StorageReference reference = storageReference.child(this.fileName);
        reference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                String imageUrl = task.getResult().toString();
                                createObject(addPoint_EDT_pointName.getText().toString(),lat, lng,
                                        addPoint_EDT_description.getText().toString(),
                                        imageUrl,
                                        addPoint_SPN_type.getSelectedItem().toString());
                            }
                        });
                        addPoint_IMV_imageView.setImageURI(null);
                        if(progressDialog.isShowing())
                            progressDialog.dismiss();
                        Toast.makeText(Add_Point_Page.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(progressDialog.isShowing())
                            progressDialog.dismiss();
                        Toast.makeText(Add_Point_Page.this, "failed Uploaded Image", Toast.LENGTH_SHORT).show();
                    }
                });
        }

    private void checkPermissionAndUploadImage() {
        // Check if permission to read external storage is granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                != PackageManager.PERMISSION_GRANTED) {
            // Request the permission if not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.READ_MEDIA_IMAGES},
                            IMAGE_UPLOAD_REQUEST_CODE);
        } else {
            // Permission already granted, start image selection process
            openImagePicker();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
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

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_UPLOAD_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_UPLOAD_REQUEST_CODE && resultCode == RESULT_OK && data!= null && data.getData() != null) {
                // Get the URI of the selected image
                imageUri = data.getData();
                addPoint_IMV_imageView.setImageURI(imageUri);
        }
    }

    private void createObject(String alias, Double lat, Double lng, String description , String imageUrl , String type) {
        ObjectBoundary newObject = new ObjectBoundary();
        newObject.setType("Point");
        newObject.setAlias(alias);
        newObject.setActive(true);
        newObject.setLocation(new LocationBoundary(lat, lng));
        newObject.setCreatedBy(new CreatedBy(CurrentUser.getInstance().getTheUser().getUserId()));
        HashMap<String, Object> details = new HashMap<String, Object>();
        details.put("description", description);
        details.put("image", imageUrl);
        details.put("type", type);
        details.put("rating", 0.0);
        details.put("totalRating", 0.0);
        details.put("ratingCount", 0);
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
        My_Signal.getInstance().toast("The point added");
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
        addPoint_IMV_imageView = findViewById(R.id.addPoint_IMV_imageView);

        String[] typesArray = getResources().getStringArray(R.array.types);
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item,typesArray);
        addPoint_SPN_type.setAdapter(typeAdapter);
    }
}