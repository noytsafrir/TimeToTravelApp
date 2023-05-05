package com.example.miniapppointsofinterest.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatRadioButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.example.miniapppointsofinterest.My_Screen_Utils;
import com.example.miniapppointsofinterest.My_Signal;
import com.example.miniapppointsofinterest.R;
import com.example.miniapppointsofinterest.api.ObjectApi;
import com.example.miniapppointsofinterest.api.RetrofitClient;
import com.example.miniapppointsofinterest.api.UserApi;
import com.example.miniapppointsofinterest.fragments.MapFragment;
import com.example.miniapppointsofinterest.model.CurrentUser;
import com.example.miniapppointsofinterest.model.object.CreatedBy;
import com.example.miniapppointsofinterest.model.object.LocationBoundary;
import com.example.miniapppointsofinterest.model.object.ObjectBoundary;
import com.example.miniapppointsofinterest.model.user.UserBoundary;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Add_Poing_Page extends AppCompatActivity {

    private RadioGroup addPoint_RAG_radiogroup;
    private AppCompatRadioButton addPoint_RAB_private;
    private AppCompatRadioButton addPoint_RAB_Public;
    private AppCompatEditText addPoint_EDT_pointName;
    private AppCompatEditText addPoint_EDT_description;
    private AppCompatEditText addPoint_EDT_picture;
    private Spinner addPoint_SPN_level;
    private Spinner addPoint_SPN_type;
    private MaterialButton addPoint_BTN_addPoint;

    private ObjectApi mApi;
    private MapFragment mapFragment;

    private GoogleMap myMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_poing_page);
        My_Screen_Utils.hideSystemUI(this);
        mApi = RetrofitClient.getInstance().create(ObjectApi.class);
        startView();
        mapFragment = new MapFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.addPoint_LAY_map,mapFragment).commit();

        addPoint_BTN_addPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int radioBtnId = addPoint_RAG_radiogroup.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) addPoint_RAG_radiogroup.findViewById(radioBtnId);
                String isPrivate = (String) radioButton.getText();
                createObject(addPoint_EDT_pointName.getText().toString(),10.5, 12.8 , isPrivate ,
                        addPoint_EDT_description.getText().toString(), addPoint_EDT_picture.getText().toString(),
                        addPoint_SPN_type.getSelectedItem().toString());
            }
        });
    }

    private void startView() {
        addPoint_RAG_radiogroup = findViewById(R.id.addPoint_RAG_radiogroup);
        addPoint_RAB_private= findViewById(R.id.addPoint_RAB_private);
        addPoint_RAB_Public= findViewById(R.id.addPoint_RAB_Public);
        addPoint_EDT_pointName = findViewById(R.id.addPoint_EDT_pointName);
        addPoint_EDT_description = findViewById(R.id.addPoint_EDT_description);
        addPoint_EDT_picture = findViewById(R.id.addPoint_EDT_picture);
        addPoint_SPN_level = findViewById(R.id.addPoint_SPN_level);
        addPoint_SPN_type = findViewById(R.id.addPoint_SPN_type);
        addPoint_BTN_addPoint = findViewById(R.id.addPoint_BTN_addPoint);
        String[] levelsArray = getResources().getStringArray(R.array.levels);
        ArrayAdapter<String> levelAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item,levelsArray);
        addPoint_SPN_level.setAdapter(levelAdapter);
        String[] typesArray = getResources().getStringArray(R.array.types);
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item,typesArray);
        addPoint_SPN_type.setAdapter(typeAdapter);
    }

    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;
        LatLng sydney = new LatLng(-34, 151);
        myMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        myMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void createObject(String alias, Double lat, Double lng, String privateOrPublic , String description , String pictures , String type) {
        ObjectBoundary newObject = new ObjectBoundary();
        newObject.setType("Point");
        newObject.setAlias(alias);
        newObject.setActive(true);
        newObject.setLocation(new LocationBoundary(lat, lng));
        newObject.setCreatedBy(new CreatedBy(CurrentUser.getInstance().getTheUser().getUserId()));
        HashMap<String, Object> details = new HashMap<String, Object>();
        details.put("private or public", privateOrPublic);
        details.put("description", description);
        details.put("pitcures", pictures);
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
        startActivity(intent);    }
}