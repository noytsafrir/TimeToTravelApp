package com.example.miniapppointsofinterest.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.miniapppointsofinterest.My_Screen_Utils;
import com.example.miniapppointsofinterest.My_Signal;
import com.example.miniapppointsofinterest.R;
import com.example.miniapppointsofinterest.api.MiniAppCommandApi;
import com.example.miniapppointsofinterest.api.ObjectApi;
import com.example.miniapppointsofinterest.api.RetrofitClient;
import com.example.miniapppointsofinterest.model.CurrentUser;
import com.example.miniapppointsofinterest.model.miniappCommand.InvocationUser;
import com.example.miniapppointsofinterest.model.miniappCommand.MiniAppCommandBoundary;
import com.example.miniapppointsofinterest.model.miniappCommand.SuperAppObjectIdBoundary;
import com.example.miniapppointsofinterest.model.miniappCommand.TargetObject;
import com.example.miniapppointsofinterest.model.object.ObjectBoundary;
import com.example.miniapppointsofinterest.recycleView.Adapter_Points;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Show_My_Points_Page extends AppCompatActivity {

    private RecyclerView ShowMyPoints_LST_points;
    private AppCompatImageView main_IMG_background;
    private ArrayList<ObjectBoundary> points;
    private Adapter_Points adapter_points;
    private MiniAppCommandApi mApi;
    private ObjectApi oApi;
    private MaterialButton ShowMyPoints_BTN_return;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_my_points_page);
        My_Screen_Utils.hideSystemUI(this);
        mApi = RetrofitClient.getInstance().create(MiniAppCommandApi.class);
        oApi = RetrofitClient.getInstance().create(ObjectApi.class);

        ShowMyPoints_LST_points = findViewById(R.id.ShowMyPoints_LST_points);
        main_IMG_background = findViewById(R.id.main_IMG_background);
        ShowMyPoints_BTN_return = findViewById(R.id.ShowMyPoints_BTN_return);

        points = new ArrayList<>();
        initList();
        getDummyObjectAndPointsFromServer();

        ShowMyPoints_BTN_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Show_My_Points_Page.this, Home_Page.class);
                startActivity(intent);
            }
        });

    }

    private void initList() {
        adapter_points = new Adapter_Points(points, this);
        adapter_points.setOnPointClickListener(new Adapter_Points.OnPointClickListener() {
            @Override
            public void onClick(View view, ObjectBoundary point, int position) {

            }
        });

        // Grid view
        ShowMyPoints_LST_points.setLayoutManager(new GridLayoutManager(this, 2));

        ShowMyPoints_LST_points.setHasFixedSize(true);
        ShowMyPoints_LST_points.setAdapter(adapter_points);
    }
    private void getDummyObjectAndPointsFromServer(){
        String superapp =CurrentUser.getInstance().getTheUser().getUserId().getSuperapp();
        String email = CurrentUser.getInstance().getTheUser().getUserId().getEmail();
        oApi.getObjectByType("DummyObject", superapp, email,1,0).enqueue(new Callback<List<ObjectBoundary>>() {
            @Override
            public void onResponse(Call<List<ObjectBoundary>> call, Response<List<ObjectBoundary>> response) {
                if (response.isSuccessful()) {
                    List<ObjectBoundary> responseBody = response.body();
                    updatePointsFromServer(responseBody.get(0));
                } else {
                    My_Signal.getInstance().toast("API call failed: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<ObjectBoundary>> call, Throwable t) {
                My_Signal.getInstance().toast("API call failed: " + t.getMessage());
            }
        });
    }

    private void updatePointsFromServer(ObjectBoundary targetObject) {
        MiniAppCommandBoundary miniappCommand = new MiniAppCommandBoundary();
        miniappCommand.setCommandAttributes(new HashMap<>());
        miniappCommand.setCommand("timeToTravel_findMyPoints");
        InvocationUser invokedBy = new InvocationUser(CurrentUser.getInstance().getTheUser().getUserId());
        miniappCommand.setInvokedBy(invokedBy);
        TargetObject target = new TargetObject(targetObject.getObjectId());
        miniappCommand.setTargetObject(target);

        mApi.invokeCommand("TimeToTravel",false,miniappCommand).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    Object responseBody = response.body();
                    ArrayList<Object> points = (ArrayList<Object>) responseBody;
                    Gson gson = new Gson();
                    String json = gson.toJson(points);
                    Type type = new TypeToken<ArrayList<ObjectBoundary>>(){}.getType();
                    ArrayList<ObjectBoundary> pointsList = gson.fromJson(json, type);
                    getAllPointsByUser(pointsList);

                } else {
                    My_Signal.getInstance().toast("API call failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                My_Signal.getInstance().toast("API call failed: " + t.getMessage());
            }
        });
    }

    private void getAllPointsByUser(List<ObjectBoundary> points) {
        this.points.addAll(points);
        updateList(this.points);
    }

    private void updateList(ArrayList<ObjectBoundary> points) {
        adapter_points.updateList(points);
    }

}