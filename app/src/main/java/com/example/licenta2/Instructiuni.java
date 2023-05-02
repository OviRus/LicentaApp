package com.example.licenta2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Instructiuni extends AppCompatActivity {

    TextView textViewInstructiuni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructiuni);

        textViewInstructiuni = findViewById(R.id.textViewInstructiuni);

        textViewInstructiuni.setText(IntrebariRaspunsuri.instructiuniAplicatie[0]);
    }
}