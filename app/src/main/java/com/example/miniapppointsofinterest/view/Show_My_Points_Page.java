package com.example.miniapppointsofinterest.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

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

import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
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

    private ImageView imageView;

    private MaterialTextView ShowMyPoints_LBL_jason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_my_points_page);
        My_Screen_Utils.hideSystemUI(this);
        mApi = RetrofitClient.getInstance().create(MiniAppCommandApi.class);
        oApi = RetrofitClient.getInstance().create(ObjectApi.class);

        ShowMyPoints_LST_points = findViewById(R.id.ShowMyPoints_LST_points);
        main_IMG_background = findViewById(R.id.main_IMG_background);
//        imageView = findViewById(R.id.imageView);
//        ShowMyPoints_LBL_jason = findViewById(R.id.ShowMyPoints_LBL_jason);
        points = new ArrayList<>();
        initList();
        updatePointsFromServer();

    }

    private void initList() {
        adapter_points = new Adapter_Points(points);
//        adapter_points.setOnPointClickListener(new Adapter_Points().OnPointClickListener() {
//            @Override
//            public void onClick (View view, ObjectBoundary point,int position){
//                Toast.makeText(Show_My_Points_Page.this, game.getTitle() + " clicked", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onLikeClicked (View view, Game game,int position){
//                likeClicked(position);
//            }
//        });

        // Grid view
        ShowMyPoints_LST_points.setLayoutManager(new GridLayoutManager(this, 2));

        ShowMyPoints_LST_points.setHasFixedSize(true);
        ShowMyPoints_LST_points.setAdapter(adapter_points);

    }

    private void updatePointsFromServer() {
        String superapp ="2023b.noy.tsafrir";
        String id = "238b32f4-c350-49b1-885f-95151e5683d1";
        MiniAppCommandBoundary miniappCommand = new MiniAppCommandBoundary();
        miniappCommand.setCommandAttributes(new HashMap<>());
        miniappCommand.setCommand("timeToTravel_findMyPoints");
        InvocationUser invokedBy = new InvocationUser(CurrentUser.getInstance().getTheUser().getUserId());
        miniappCommand.setInvokedBy(invokedBy);
        //TODO: COMPLETE THE TARGET OBJECT
        SuperAppObjectIdBoundary superAppObjectIdBoundary = new SuperAppObjectIdBoundary(superapp,id);
        TargetObject targetObject = new TargetObject(superAppObjectIdBoundary);
        miniappCommand.setTargetObject(targetObject);

//        mApi.invokeCommand("TimeToTravel",false,miniappCommand).enqueue(new Callback<Object>() {
        oApi.getAllObjectsUsingPagination(CurrentUser.getInstance().getTheUser().getUserId().getSuperapp(),
                CurrentUser.getInstance().getTheUser().getUserId().getEmail(),
                10,0).enqueue(new Callback<List<ObjectBoundary>>() {
            @Override
//            public void onResponse(Call<ObjectBoundary> call, Response<Object> response) {
            public void onResponse(Call<List<ObjectBoundary>> call, Response<List<ObjectBoundary>> response) {
                if (response.isSuccessful()) {
                   getAllPointsByUser(response.body());
//                    Object responseBody = response.body();
//                    if (responseBody instanceof List) {
//                        ArrayList<ObjectBoundary> points = (ArrayList<ObjectBoundary>) responseBody;
//                        getAllPointsByUser(points);
//                    } else {
//                        My_Signal.getInstance().toast("Invalid response body type");
//                    }
                } else {
                    My_Signal.getInstance().toast("API call failed: " + response.code());
                }
            }

            @Override
//            public void onFailure(Call<Object> call, Throwable t) {
            public void onFailure(Call<List<ObjectBoundary>> call, Throwable t) {
                My_Signal.getInstance().toast("API call failed: " + t.getMessage());
            }
        });
    }

    private void getAllPointsByUser(List<ObjectBoundary> points) {
        this.points.addAll(points);
        updateList(this.points);
//        ShowMyPoints_LBL_jason.setText(points.toString());
        Log.d("test", "********************$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        Log.d("test", "********************getAllPointsByUser: "+ points.get(0).getClass().getName());
//        if (!points.isEmpty()) {
//            ObjectBoundary firstPoint = points.get(0);
//            Gson gson = new Gson();
//            String objectDetailsJson = gson.toJson(firstPoint.getObjectDetails());
//            ObjectBoundary objectBoundary = gson.fromJson(objectDetailsJson, ObjectBoundary.class);
//
//            String imageBase64 = (String)objectBoundary.getObjectDetails().get("image");
//            byte[] imageBytes = Base64.decode(imageBase64, Base64.DEFAULT);
//
//            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
//            imageView.setImageBitmap(bitmap);
//        }
    }

    private void updateList(ArrayList<ObjectBoundary> points) {
        adapter_points.updateList(points);
    }
}