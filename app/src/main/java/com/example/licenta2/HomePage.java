package com.example.licenta2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class HomePage extends AppCompatActivity {

    private Button buttonJoaca, buttonProvocare, buttonInstructiuni, buttonStatistici, buttonAlteJocuri;
    TextView textViewLogout;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        buttonJoaca = findViewById(R.id.buttonJoaca);
        buttonProvocare = findViewById(R.id.buttonAlteJocuri);

        buttonInstructiuni = findViewById(R.id.buttonInstructiuni);
        buttonStatistici = findViewById(R.id.buttonStatistici);

        buttonAlteJocuri = findViewById(R.id.buttonAlteJocuri);
        textViewLogout = findViewById(R.id.textViewLogout);

        gso  = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);


        BazaDate bazaDeDate = new BazaDate(HomePage.this);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
            buttonJoaca.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(HomePage.this, ActivitateIntrebare.class);
                    startActivity(intent);
                }
            });

        textViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        buttonInstructiuni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(HomePage.this, Instructiuni.class);
                startActivity(intent2);
            }
        });

        buttonStatistici.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(HomePage.this, Statistici.class);
                startActivity(intent3);
            }
        });
        buttonProvocare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4 = new Intent(HomePage.this, Provocare.class);
                startActivity(intent4);
            }
        });

        buttonAlteJocuri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }

    void signOut(){
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                finish();
                startActivity(new Intent(HomePage.this, MainActivity.class));
            }
        });
    }
}