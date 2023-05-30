package com.example.miniapppointsofinterest.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import com.example.miniapppointsofinterest.My_Screen_Utils;
import com.example.miniapppointsofinterest.My_Signal;
import com.example.miniapppointsofinterest.R;
import com.example.miniapppointsofinterest.api.MiniAppCommandApi;
import com.example.miniapppointsofinterest.api.ObjectApi;
import com.example.miniapppointsofinterest.api.RetrofitClient;
import com.example.miniapppointsofinterest.model.CurrentUser;
import com.example.miniapppointsofinterest.model.miniappCommand.InvocationUser;
import com.example.miniapppointsofinterest.model.miniappCommand.MiniAppCommandBoundary;
import com.example.miniapppointsofinterest.model.object.ObjectBoundary;
import com.example.miniapppointsofinterest.recycleView.Adapter_Points;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Show_Recommended_Points_Page extends AppCompatActivity {

    private RecyclerView ShowRecPoints_LST_points;
    private AppCompatImageView main_IMG_background;
    private ArrayList<ObjectBoundary> points;
    private Adapter_Points adapter_points;
    private MaterialButton specificPoint_BTN_return;
    private ObjectApi oApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_recommended_points_page);
        My_Screen_Utils.hideSystemUI(this);
        oApi = RetrofitClient.getInstance().create(ObjectApi.class);
        main_IMG_background = findViewById(R.id.main_IMG_background);
        ShowRecPoints_LST_points = findViewById(R.id.ShowRecPoints_LST_points);
        specificPoint_BTN_return = findViewById(R.id.specificPoint_BTN_return);
        points = new ArrayList<>();
        initList();
        updatePointsFromServer();
        specificPoint_BTN_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Show_Recommended_Points_Page.this, Home_Page.class);
                startActivity(intent);
            }
        });
    }

    private void initList() {
        adapter_points = new Adapter_Points(points, this);
        adapter_points.setOnPointClickListener(new Adapter_Points.OnPointClickListener() {
            @Override
            public void onClick(View view, ObjectBoundary point, int position) {
                Intent intent = new Intent(Show_Recommended_Points_Page.this, Specific_Point_Page.class);
                intent.putExtra(Specific_Point_Page.KEY_NAME, point.getAlias());
                intent.putExtra(Specific_Point_Page.KEY_TYPE, point.getObjectDetails().get("type").toString());
                intent.putExtra(Specific_Point_Page.KEY_USER, point.getCreatedBy().getUserId().getEmail());
                intent.putExtra(Specific_Point_Page.KEY_LAT, point.getLocation().getLat());
                intent.putExtra(Specific_Point_Page.KEY_LNG, point.getLocation().getLng());
                intent.putExtra(Specific_Point_Page.KEY_DESCRIPTION, point.getObjectDetails().get("description").toString());
                intent.putExtra(Specific_Point_Page.KEY_IMAGE, point.getObjectDetails().get("image").toString());
                intent.putExtra(Specific_Point_Page.KEY_SUPERAPP, point.getObjectId().getSuperapp());
                intent.putExtra(Specific_Point_Page.KEY_ID, point.getObjectId().getInternalObjectId());
                startActivity(intent);
            }
        });

        // Grid view
        ShowRecPoints_LST_points.setLayoutManager(new GridLayoutManager(this, 2));
        ShowRecPoints_LST_points.setHasFixedSize(true);
        ShowRecPoints_LST_points.setAdapter(adapter_points);

    }

    private void updatePointsFromServer() {
        oApi.getAllObjectsUsingPagination(CurrentUser.getInstance().getTheUser().getUserId().getSuperapp(),
                CurrentUser.getInstance().getTheUser().getUserId().getEmail(),
                20,0).enqueue(new Callback<List<ObjectBoundary>>() {
            @Override
            public void onResponse(Call<List<ObjectBoundary>> call, Response<List<ObjectBoundary>> response) {
                if (response.isSuccessful()) {
                    getAllPoints(response.body());
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
    private void getAllPoints(List<ObjectBoundary> points) {
        this.points.addAll(points);
        updateList(this.points);
    }


    private void updateList(ArrayList<ObjectBoundary> points) {
        adapter_points.updateList(points);
    }

}