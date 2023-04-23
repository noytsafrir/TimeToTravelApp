package com.example.miniapppointsofinterest.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.os.Bundle;

import com.example.miniapppointsofinterest.My_Screen_Utils;
import com.example.miniapppointsofinterest.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class Signin_Page extends AppCompatActivity {
    private MaterialTextView    signin_LBL_signin;
    private AppCompatEditText   signin_EDT_email;
    private MaterialButton      signin_BTN_signin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_page);
        My_Screen_Utils.hideSystemUI(this);
        startView();
    }

    private void startView() {
        signin_LBL_signin = findViewById(R.id.signin_LBL_signin);
        signin_EDT_email = findViewById(R.id.signin_EDT_email);
        signin_BTN_signin = findViewById(R.id.signin_BTN_signin);
    }
}