package com.example.licenta2;

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
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText editTextEmail, editTextPassword;
    private Button buttonSignup, buttonLogin;
    private TextView textViewForgotPass;
    BazaDate DB;


    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        editTextEmail =findViewById(R.id.EmailText);
        editTextPassword = findViewById(R.id.PasswordText);
        buttonSignup = findViewById(R.id.buttonSignup);
        buttonLogin = findViewById(R.id.buttonLoginin);
        textViewForgotPass = findViewById(R.id.textForgotPass);

        gso  = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account!=null)
            navigateToHomePage();

        DB = new BazaDate(this);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
//                String email = editTextEmail.getText().toString();
//                String password = editTextPassword.getText().toString();

//                if (email.equals("") && password.equals(""))
//                {
//                    Toast.makeText(MainActivity.this, "Va rugam completati toate.", Toast.LENGTH_SHORT).show();
//                }
//                else
//                {
//                    Boolean checkuserpass = DB.verificaUserEmailParola(email,password);
//                    if (checkuserpass == true)
//                    {
//                        Toast.makeText(MainActivity.this, "Logare cu succes.", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(getApplicationContext(), HomePage.class);
//                        startActivity(intent);
//                    }
//                    else
//                    {
//                        Toast.makeText(MainActivity.this, "Email sau parola gresite", Toast.LENGTH_SHORT).show();
//                    }
//                }
            }
        });

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        textViewForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), ParolaUitata.class);
                startActivity(intent);
            }
        });
    }

    void signIn()
    {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                navigateToHomePage();
                task.getResult(ApiException.class);
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "Ceva nu merge bine", Toast.LENGTH_SHORT);
            }

        }
    }

    void navigateToHomePage()
        {
            finish();
            Intent intent = new Intent(MainActivity.this, HomePage.class);
            startActivity(intent);  //pun aici functia pt ca sa o pot muta mai usor in clasa de login
        }
}