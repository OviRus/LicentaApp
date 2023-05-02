package com.example.licenta2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

public class ParolaUitata extends AppCompatActivity {

    private TextInputEditText editTextForget;
    private Button buttonForget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parola_uitata);

        editTextForget = findViewById(R.id.editTextForget);
        buttonForget = findViewById(R.id.buttonForget);

        buttonForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextForget.getText().toString();
                if (!email.equals(""))
                {
                    resetareParola(email);
                }
            }
        });
    }
    public void resetareParola(String email)
    {

    }
}