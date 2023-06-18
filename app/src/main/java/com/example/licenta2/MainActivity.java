package com.example.licenta2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

    private ImageView imageViewGoogleIcon;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        imageViewGoogleIcon = findViewById(R.id.imageViewGoogle);



        gso  = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        imageViewGoogleIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    void signIn()
    {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    String googleAccountId = account.getId();
                    userEmail = account.getEmail();
                    EmailClass.setUserEmail(userEmail);
                    int userPoints = 0;

                    // Check if the user already exists in the database
                    BazaDateDAO dao = new BazaDateDAO(getApplicationContext());
                    dao.open();
                    Cursor cursor = dao.getAllUsers();
                    boolean userExists = false;
                    while (cursor.moveToNext()) {
                        @SuppressLint("Range") String existingUserEmail = cursor.getString(cursor.getColumnIndex("userEmail"));
                        if (existingUserEmail.equals(userEmail)) {
                            userExists = true;
                            break;
                        }
                    }
                    cursor.close();
                    dao.close();

                    if (!userExists) {
                        // User doesn't exist, insert into the database
                        dao.open();
                        dao.addUser(userEmail, userPoints, 0, 0); // Assuming totalQuestions and wrong are initially set to 0
                        dao.close();

                        Toast.makeText(getApplicationContext(), "User added to the database", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, HomePage.class);
                        startActivity(intent);
                        finish();
                    }

                    navigateToHomePage();
                }
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "Ceva nu merge bine", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, HomePage.class);
                startActivity(intent);
                finish();
            }
        }
    }

    void navigateToHomePage()
        {
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
            if(account != null) {

                String googleAccountId = account.getId();
                int userPoints = 0;
                finish();
                Intent intent = new Intent(MainActivity.this, HomePage.class);
                startActivity(intent);
            }
        }
}