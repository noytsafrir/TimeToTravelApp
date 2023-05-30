package com.example.miniapppointsofinterest.view;


import android.content.Intent;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.bumptech.glide.Glide;
import com.example.miniapppointsofinterest.My_Screen_Utils;
import com.example.miniapppointsofinterest.R;
import com.example.miniapppointsofinterest.api.MiniAppCommandApi;
import com.example.miniapppointsofinterest.api.RetrofitClient;
import com.example.miniapppointsofinterest.model.CurrentUser;
import com.example.miniapppointsofinterest.model.miniappCommand.InvocationUser;
import com.example.miniapppointsofinterest.model.miniappCommand.MiniAppCommandBoundary;
import com.example.miniapppointsofinterest.model.miniappCommand.SuperAppObjectIdBoundary;
import com.example.miniapppointsofinterest.model.miniappCommand.TargetObject;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.HashMap;

import retrofit2.Callback;

public class Specific_Point_Page extends AppCompatActivity {

    public static final String KEY_NAME ="KEY_NAME";
    public static final String KEY_TYPE ="KEY_TYPE";
    public static final String KEY_DESCRIPTION ="KEY_DESCRIPTION";
    public static final String KEY_USER ="KEY_USER";
    public static final String KEY_LAT ="KEY_LAT";
    public static final String KEY_LNG ="KEY_LNG";
    public static final String KEY_IMAGE ="KEY_IMAGE";
    public static final String KEY_SUPERAPP ="KEY_SUPERAPP";
    public static final String KEY_ID ="KEY_ID";
    private MaterialTextView    specificPoint_LBL_PointName;
    private MaterialTextView    specificPoint_LBL_PointType;
    private MaterialTextView    specificPoint_LBL_CreatedUser;
    private MaterialTextView    specificPoint_LBL_Location;
    private MaterialTextView    specificPoint_LBL_PointDescription;
    private ImageView           specificPoint_IMV_imageView;
    private RatingBar           point_RTG_rating;
    private MaterialButton      specificPoint_BTN_return;
    private float userRating=0;

    private MiniAppCommandApi mApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_object);
        My_Screen_Utils.hideSystemUI(this);
        startView();
        mApi = RetrofitClient.getInstance().create(MiniAppCommandApi.class);
        Intent previousIntent = getIntent();
        UploadDetails(previousIntent);

        point_RTG_rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rate, boolean isPressed) {
                if(isPressed)
                    userRating = ratingBar.getRating();
            }
        });
        specificPoint_BTN_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userRating>0)
                    sendRate(previousIntent);
                returnToFormerPage();
            }
        });
    }

    private void sendRate(Intent previousIntent) {

        String superapp =previousIntent.getExtras().getString(KEY_SUPERAPP);
        String id =previousIntent.getExtras().getString(KEY_ID);

        MiniAppCommandBoundary miniappCommand = new MiniAppCommandBoundary();
        miniappCommand.setCommandAttributes(new HashMap<>());
        miniappCommand.setCommand("timeToTravel_ratePoint");
        InvocationUser invokedBy = new InvocationUser(CurrentUser.getInstance().getTheUser().getUserId());
        miniappCommand.setInvokedBy(invokedBy);
        TargetObject target = new TargetObject(new SuperAppObjectIdBoundary(superapp,id));
        miniappCommand.setTargetObject(target);
        miniappCommand.getCommandAttributes().put("rate",userRating);

        mApi.invokeCommand("TimeToTravel",false,miniappCommand).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(retrofit2.Call<Object> call, retrofit2.Response<Object> response) {
            }

            @Override
            public void onFailure(retrofit2.Call<Object> call, Throwable t) {
            }
        });

    }

    private void UploadDetails(Intent previousIntent) {
        String name =previousIntent.getExtras().getString(KEY_NAME);
        String type =previousIntent.getExtras().getString(KEY_TYPE);
        String user =previousIntent.getExtras().getString(KEY_USER);
        Double lat  =previousIntent.getExtras().getDouble(KEY_LAT);
        Double lng  =previousIntent.getExtras().getDouble(KEY_LNG);
        String description =previousIntent.getExtras().getString(KEY_DESCRIPTION);
        String image =previousIntent.getExtras().getString(KEY_IMAGE);

        specificPoint_LBL_PointName.setText(name);
        specificPoint_LBL_PointType.setText("Type: "+ type);
        specificPoint_LBL_CreatedUser.setText("Created by: "+user);
        specificPoint_LBL_Location.setText("Location: ("+lat+ " , " + lng+")");
        specificPoint_LBL_PointDescription.setText("Description: "+ description);
        Glide
                .with(Specific_Point_Page.this)
                .load(image)
                .into(specificPoint_IMV_imageView);
    }

    private void returnToFormerPage() {
        Intent intent = new Intent(this, Show_Recommended_Points_Page.class);
        startActivity(intent);
        finish();
    }

    private void startView() {
        specificPoint_LBL_PointName = findViewById(R.id.specificPoint_LBL_PointName);
        specificPoint_LBL_PointType = findViewById(R.id.specificPoint_LBL_PointType);
        specificPoint_LBL_CreatedUser = findViewById(R.id.specificPoint_LBL_CreatedUser);
        specificPoint_LBL_Location = findViewById(R.id.specificPoint_LBL_Location);
        specificPoint_LBL_PointDescription = findViewById(R.id.specificPoint_LBL_PointDescription);
        specificPoint_IMV_imageView = findViewById(R.id.specificPoint_IMV_imageView);
        point_RTG_rating = findViewById(R.id.point_RTG_rating);
        specificPoint_BTN_return = findViewById(R.id.specificPoint_BTN_return);
    }
}