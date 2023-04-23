package com.example.miniapppointsofinterest.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatRadioButton;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.example.miniapppointsofinterest.My_Screen_Utils;
import com.example.miniapppointsofinterest.R;
import com.google.android.material.button.MaterialButton;
public class Add_Poing_Page extends AppCompatActivity {

    private RadioGroup addPoint_RAG_radiogroup;
    private AppCompatRadioButton addPoint_RAB_private;
    private AppCompatRadioButton addPoint_RAB_Public;
    private AppCompatEditText addPoint_EDT_pointName;
    private AppCompatEditText addPoint_EDT_description;
    private AppCompatEditText addPoint_EDT_picture;
    private Spinner addPoint_SPN_level;
    private Spinner addPoint_SPN_type;
    private MaterialButton addPoint_BTN_addPoint;
    private ImageView addPoint_IMV_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_poing_page);
        My_Screen_Utils.hideSystemUI(this);
        startView();
    }

    private void startView() {
        addPoint_RAG_radiogroup = findViewById(R.id.addPoint_RAG_radiogroup);
        addPoint_RAB_private= findViewById(R.id.addPoint_RAB_private);
        addPoint_RAB_Public= findViewById(R.id.addPoint_RAB_Public);
        addPoint_EDT_pointName = findViewById(R.id.addPoint_EDT_pointName);
        addPoint_EDT_description = findViewById(R.id.addPoint_EDT_description);
        addPoint_EDT_picture = findViewById(R.id.addPoint_EDT_picture);
        addPoint_SPN_level = findViewById(R.id.addPoint_SPN_level);
        addPoint_SPN_type = findViewById(R.id.addPoint_SPN_type);
        addPoint_BTN_addPoint = findViewById(R.id.addPoint_BTN_addPoint);
        String[] levelsArray = getResources().getStringArray(R.array.levels);
        ArrayAdapter<String> levelAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item,levelsArray);
        addPoint_SPN_level.setAdapter(levelAdapter);
        String[] typesArray = getResources().getStringArray(R.array.types);
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item,typesArray);
        addPoint_SPN_type.setAdapter(typeAdapter);
    }
}