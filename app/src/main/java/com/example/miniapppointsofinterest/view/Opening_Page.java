package com.example.miniapppointsofinterest.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.miniapppointsofinterest.My_Screen_Utils;
import com.example.miniapppointsofinterest.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class Opening_Page extends AppCompatActivity {

    private MaterialTextView opening_LBL_welcome;
    private MaterialButton opening_BTN_signin;
    private MaterialButton opening_BTN_signup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_page);
        My_Screen_Utils.hideSystemUI(this);

        opening_BTN_signin = findViewById(R.id.opening_BTN_signin);
        opening_BTN_signup = findViewById(R.id.opening_BTN_signup);

        opening_BTN_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Opening_Page.this, Signin_Page.class);
                startActivity(intent);
            }
        });


        opening_BTN_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Opening_Page.this, Signup_Page.class);
                startActivity(intent);
            }
        });




    }
}