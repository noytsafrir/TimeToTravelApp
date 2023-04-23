package com.example.miniapppointsofinterest.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.os.Bundle;

import com.example.miniapppointsofinterest.My_Screen_Utils;
import com.example.miniapppointsofinterest.R;
import com.google.android.material.textview.MaterialTextView;

public class Show_Recommended_Points_Page extends AppCompatActivity {

    private MaterialTextView ShowRecPoints_LBL_recPoints;
    private  MaterialTextView ShowRecPoints_LBL_point1;
    private AppCompatEditText ShowRecPoints_EDT_comment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_recommended_points_page);
        My_Screen_Utils.hideSystemUI(this);
        startView();
    }

    private void startView() {
        ShowRecPoints_LBL_recPoints = findViewById(R.id.ShowRecPoints_LBL_recPoints);
         ShowRecPoints_LBL_point1 = findViewById(R.id.ShowRecPoints_LBL_point1);
         ShowRecPoints_EDT_comment = findViewById(R.id.ShowRecPoints_EDT_comment);
    }

}