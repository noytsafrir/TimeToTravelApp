package com.example.miniapppointsofinterest.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.miniapppointsofinterest.My_Screen_Utils;
import com.example.miniapppointsofinterest.R;

public class Show_My_Points_Page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_my_points_page);
        My_Screen_Utils.hideSystemUI(this);
    }
}