package com.example.miniapppointsofinterest.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;


import com.example.miniapppointsofinterest.My_Screen_Utils;
import com.example.miniapppointsofinterest.My_Signal;
import com.example.miniapppointsofinterest.R;
import com.example.miniapppointsofinterest.api.RetrofitClient;
import com.example.miniapppointsofinterest.api.UserApi;
import com.example.miniapppointsofinterest.model.NewUserBoundary;
import com.example.miniapppointsofinterest.model.UserBoundary;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Signup_Page extends AppCompatActivity {

    private AppCompatEditText signup_EDT_avatar;
    private AppCompatEditText signup_EDT_email;
    private AppCompatEditText signup_EDT_username;
    private MaterialButton signup_BTN_signup;
    private Spinner signup_SPN_roles;

    private UserApi mApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);
        My_Screen_Utils.hideSystemUI(this);
        mApi = RetrofitClient.getInstance().create(UserApi.class);
        startView();

        signup_SPN_roles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });


        signup_BTN_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser(signup_EDT_email.getText().toString(), signup_EDT_username.getText().toString(), signup_SPN_roles.getSelectedItem().toString(), signup_EDT_avatar.getText().toString());
//                signup_EDT_email.getText().clear();
//                signup_EDT_username.getText().clear();
//                //TODO: clear spinner
//                signup_EDT_avatar.getText().clear();
            }
        });
    }
    private void startView() {
        signup_BTN_signup = findViewById(R.id.signup_BTN_signup);
        signup_EDT_avatar = findViewById(R.id.signup_EDT_avatar);
        signup_EDT_email = findViewById(R.id.signup_EDT_email);
        signup_EDT_username =findViewById(R.id.signup_EDT_username);
        signup_SPN_roles = findViewById(R.id.signup_SPN_roles);
        String[] rolesArray = getResources().getStringArray(R.array.types);
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item,rolesArray);
        signup_SPN_roles.setAdapter(typeAdapter);

    }


    private void createUser(String email, String username, String role, String avatar) {
        NewUserBoundary newUser = new NewUserBoundary(role, username, avatar, email);
        mApi.createUser(newUser).enqueue(new Callback<UserBoundary>() {
            @Override
            public void onResponse(Call<UserBoundary> call, Response<UserBoundary> response) {
                if (response.isSuccessful()) {
                    confirmNewUserUI(response.body());
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

    private void confirmNewUserUI(UserBoundary user) {
        //TODO: check to add a delay before remove to the home page to show the success message
        Intent intent = new Intent(Signup_Page.this, Home_Page.class);
        startActivity(intent);
    }


}