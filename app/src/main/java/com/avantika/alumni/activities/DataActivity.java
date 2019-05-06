package com.avantika.alumni.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.avantika.alumni.R;

public class DataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        Toast.makeText(this, getIntent().getStringExtra("dataRequest"), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, getIntent().getStringExtra("id"), Toast.LENGTH_SHORT).show();

    }
}
