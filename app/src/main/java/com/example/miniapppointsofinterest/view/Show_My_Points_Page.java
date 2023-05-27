package com.example.miniapppointsofinterest.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.example.miniapppointsofinterest.model.miniappCommand.TargetObject;
import com.example.miniapppointsofinterest.model.object.ObjectBoundary;
import com.example.miniapppointsofinterest.model.user.UserBoundary;
import com.example.miniapppointsofinterest.recycleView.Adapter_Points;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Show_My_Points_Page extends AppCompatActivity {

    private RecyclerView ShowMyPoints_LST_points;
    private AppCompatImageView main_IMG_background;
    private ArrayList<ObjectBoundary> points;
    private Adapter_Points adapter_points;
    private MiniAppCommandApi mApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_my_points_page);
        My_Screen_Utils.hideSystemUI(this);
        mApi = RetrofitClient.getInstance().create(MiniAppCommandApi.class);

        ShowMyPoints_LST_points = findViewById(R.id.ShowMyPoints_LST_points);
        main_IMG_background = findViewById(R.id.main_IMG_background);
//        Imager.me().imageByDrawableCrop(main_IMG_background, R.drawable.img_background);

        initList();
        updatePointsFromServer();

    }

    private void initList() {
        adapter_points = new Adapter_Points(points);
//            adapter_points.setOnPointClickListener(new Adapter_Points().OnPointClickListener() {
//            @Override
//            public void onClick(View view, ObjectBoundary point, int position) {
//                Toast.makeText(Show_My_Points_Page.this, game.getTitle() + " clicked", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onLikeClicked(View view, Game game, int position) {
//                likeClicked(position);
//            }
//        });

        // Grid view
        ShowMyPoints_LST_points.setLayoutManager(new GridLayoutManager(this, 2));

        ShowMyPoints_LST_points.setHasFixedSize(true);
        ShowMyPoints_LST_points.setAdapter(adapter_points);

    }

    private void updatePointsFromServer() {
        MiniAppCommandBoundary command = new MiniAppCommandBoundary();
        command.setCommandAttributes(new HashMap<String, Object>());
        command.setCommand("getPointsByCreatedUser");
        InvocationUser invokedBy = new InvocationUser(CurrentUser.getInstance().getTheUser().getUserId());
        command.setInvokedBy(invokedBy);
        //TODO: COMPLETE THE TARGET OBJECT
//        TargetObject targetObject = new TargetObject();
        mApi.invokeCommand("timeToTravel",false,command).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    getAllPointsByUser(response.body());
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

    private ArrayList<ObjectBoundary> getAllPointsByUser(Object body) {
        points = (ArrayList<ObjectBoundary>) body;
//        adapter_points.setPoints(points);
//        adapter_points.notifyDataSetChanged();
        return points;
    }
}