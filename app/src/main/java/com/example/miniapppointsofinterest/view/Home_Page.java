package com.example.miniapppointsofinterest.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.miniapppointsofinterest.My_Screen_Utils;
import com.example.miniapppointsofinterest.My_Signal;
import com.example.miniapppointsofinterest.R;
import com.example.miniapppointsofinterest.api.ObjectApi;
import com.example.miniapppointsofinterest.api.UserApi;
import com.example.miniapppointsofinterest.model.CurrentUser;
import com.example.miniapppointsofinterest.model.object.ObjectBoundary;
import com.example.miniapppointsofinterest.model.user.NewUserBoundary;
import com.example.miniapppointsofinterest.model.user.UserBoundary;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home_Page extends AppCompatActivity {

    private MaterialButton home_BTN_addPoint;
    private MaterialButton home_BTN_showMyPoints;
    private MaterialButton home_BTN_showRecommendedPoints;
    private MaterialTextView home_LBL_hello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        My_Screen_Utils.hideSystemUI(this);
        startView();

        home_BTN_addPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home_Page.this, Add_Poing_Page.class);
                startActivity(intent);
            }
        });

        home_BTN_showMyPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home_Page.this, Show_My_Points_Page.class);
                startActivity(intent);
            }
        });

        home_BTN_showRecommendedPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home_Page.this, Show_Recommended_Points_Page.class);
                startActivity(intent);
            }
        });

        home_LBL_hello.setText("Hello "+ CurrentUser.getInstance().getTheUser().getUsername());
    }
    private void startView() {
        home_BTN_addPoint = findViewById(R.id.home_BTN_addPoint);
        home_BTN_showMyPoints = findViewById(R.id.home_BTN_showMyPoints);
        home_BTN_showRecommendedPoints = findViewById(R.id.home_BTN_showRecommendedPoints);
        home_LBL_hello = findViewById(R.id.home_LBL_hello);
    }

}

