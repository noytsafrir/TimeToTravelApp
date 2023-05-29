package com.example.miniapppointsofinterest.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.miniapppointsofinterest.My_Screen_Utils;
import com.example.miniapppointsofinterest.My_Signal;
import com.example.miniapppointsofinterest.R;
import com.example.miniapppointsofinterest.api.RetrofitClient;
import com.example.miniapppointsofinterest.api.UserApi;
import com.example.miniapppointsofinterest.model.CurrentUser;
import com.example.miniapppointsofinterest.model.user.UserBoundary;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Signin_Page extends AppCompatActivity {
    private MaterialTextView    signin_LBL_signin;
    private AppCompatEditText   signin_EDT_email;
    private MaterialButton      signin_BTN_signin;
    private UserApi mApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_page);
        My_Screen_Utils.hideSystemUI(this);
        mApi = RetrofitClient.getInstance().create(UserApi.class);

        startView();
        signin_BTN_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(signin_EDT_email.getText().toString());
            }
        });
    }
    private void startView() {
        signin_LBL_signin = findViewById(R.id.signin_LBL_signin);
        signin_EDT_email = findViewById(R.id.signin_EDT_email);
        signin_BTN_signin = findViewById(R.id.signin_BTN_signin);
    }

    private void login(String email){
        String supperapp = "2023b.noy.tsafrir";
        mApi.login(supperapp,email).enqueue(new Callback<UserBoundary>() {
            @Override
            public void onResponse(Call<UserBoundary> call, Response<UserBoundary> response) {
                if (response.isSuccessful()) {
                    openUserAccount(response.body());
                } else {
                    My_Signal.getInstance().toast("API call failed: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<UserBoundary> call, Throwable t) {
                My_Signal.getInstance().toast("API call failed: " + t.getMessage());
            }
        });
    }
    private void openUserAccount(UserBoundary body) {
        //Todo: load all the details
        CurrentUser.init(body);
        Intent intent = new Intent(this, Home_Page.class);
        startActivity(intent);
    }
}