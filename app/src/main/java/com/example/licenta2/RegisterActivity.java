package com.example.licenta2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    private ImageView imageViewAccount;
    private TextInputEditText editTextRegisterEmail, editTextPasswordRegister, editTextRepassword;
    private Button buttonRegister;

    boolean controlImagine = false;
    BazaDate DB;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        imageViewAccount = findViewById(R.id.imageViewAccount);
        editTextRegisterEmail = findViewById(R.id.editTextRegisterEmail);
        editTextPasswordRegister = findViewById(R.id.editTextPasswordRegister);
        editTextRepassword = findViewById(R.id.editTextRepassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        DB = new BazaDate(this);
        SQLiteDatabase dbsql = DB.getReadableDatabase();

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email_user = editTextRegisterEmail.getText().toString();
                String parola = editTextPasswordRegister.getText().toString();
                String repassword = editTextRepassword.getText().toString();

                if (!email_user.equals("")  && !parola.equals("") && !repassword.equals(""))
                {
                    Toast.makeText(RegisterActivity.this, "Te rugam completeaza toate campurile", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (parola.equals(repassword)) {
                        Boolean checkuser = DB.verificareEmailUser(email_user);
                        if (checkuser == false) {
                            Boolean insert = DB.insertData(email_user, parola);
                            if (insert == true) {
                                Toast.makeText(RegisterActivity.this, "Inregistrare cu succes", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(RegisterActivity.this, "Inregistrare esuata", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(RegisterActivity.this, "Email-ul deja exista. Te rugam introdu alt emil", Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        Toast.makeText(RegisterActivity.this, "Parolile nu se potrivesc!", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
}