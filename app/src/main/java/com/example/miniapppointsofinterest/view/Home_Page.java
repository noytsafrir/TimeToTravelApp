package com.example.miniapppointsofinterest.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.miniapppointsofinterest.My_Screen_Utils;
import com.example.miniapppointsofinterest.R;
import com.google.android.material.button.MaterialButton;

public class Home_Page extends AppCompatActivity {

    private MaterialButton home_BTN_addPoint;
    private MaterialButton home_BTN_showMyPoints;
    private MaterialButton home_BTN_showRecommendedPoints;


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
    }
    private void startView() {
        home_BTN_addPoint = findViewById(R.id.home_BTN_addPoint);
        home_BTN_showMyPoints = findViewById(R.id.home_BTN_showMyPoints);
        home_BTN_showRecommendedPoints = findViewById(R.id.home_BTN_showRecommendedPoints);
    }

}

