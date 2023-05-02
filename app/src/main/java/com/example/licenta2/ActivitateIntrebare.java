package com.example.licenta2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class ActivitateIntrebare extends AppCompatActivity  {

    TextView time, question, punctaj;
    Button button1, button2, button3, button4, button;

    public int raspunsuriTotale = 0;


    public int raspunsuriCorecte = 0;

    CountDownTimer timer;
    private static final long START_TIMER_IN_MILS = 20000;
    Boolean timmer_runnig;
    long timp_ramas_in_milis = START_TIMER_IN_MILS;

    int score;
    int intrebareCompleta = IntrebariRaspunsuri.intrebare.length;

//    int intrebareCompleta = intrebariRaspunsuri.intrebareAleatoare();
    int indexIntrebareCurenta=0;
    String raspunsSelectat = "";

    SharedPreferences sharedPreferences;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activitate_intrebare);

        time = findViewById(R.id.textViewTime);
        question = findViewById(R.id.textViewQuestion);
        punctaj= findViewById(R.id.textViewPunctaj);
        button1 = findViewById(R.id.buttonRaspuns1);
        button2 = findViewById(R.id.buttonRaspuns2);
        button3 = findViewById(R.id.buttonRaspuns3);
        button4 = findViewById(R.id.buttonRaspuns4);




        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                button1.setBackgroundColor(Color.WHITE);
                verificareRaspuns(button1);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                verificareRaspuns(button2);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                verificareRaspuns(button3);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verificareRaspuns(button4);
            }
        });

        startTimer();

        question.setText("Intrebare: "+intrebareCompleta);
        intrebareNoua();

        retrageDatele();

    }

    @Override
    protected void onPause() {
        salveazaDatele();
        pauseTimer();
        super.onPause();
    }

    public void  salveazaDatele()
    {
        sharedPreferences = getSharedPreferences("salveazaDatete", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("key score", score);
        editor.commit();
    }

    public void retrageDatele()
    {
        sharedPreferences = getSharedPreferences("salveazaDatele", MODE_PRIVATE);
        score = sharedPreferences.getInt("key score", 0);
        punctaj.setText(""+score);
    }


    void intrebareNoua()
    {
        question.setText(IntrebariRaspunsuri.intrebare[indexIntrebareCurenta]);
        button1.setText(IntrebariRaspunsuri.variante[indexIntrebareCurenta][0]);
        button2.setText(IntrebariRaspunsuri.variante[indexIntrebareCurenta][1]);
        button3.setText(IntrebariRaspunsuri.variante[indexIntrebareCurenta][2]);
        button4.setText(IntrebariRaspunsuri.variante[indexIntrebareCurenta][3]);
    }

    void verificareRaspuns(Button button)
    {
        String textButton = button.getText().toString();
        raspunsSelectat = IntrebariRaspunsuri.raspunsuriCorecte[indexIntrebareCurenta];
        if (textButton.equals(raspunsSelectat))
        {
            raspunsuriCorecte++;
            button.setBackgroundColor(Color.GREEN);
            score++;
            pauseTimer();
            AlertDialog.Builder builder3 = new AlertDialog.Builder(ActivitateIntrebare.this);
            builder3.setTitle("FELICITARI! :)))");
            builder3.setCancelable(false);
            builder3.setMessage("Doresti sa raspunzi la o alta intrebare?");
            builder3.setPositiveButton("DA", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    indexIntrebareCurenta++;
                    pauseTimer();
                    resetTimer();
                    intrebareNoua();
                    startTimer();

                }
            });
            builder3.setNegativeButton("NU", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(ActivitateIntrebare.this, HomePage.class);
                    startActivity(intent);
                    finish();
                }
            });
            builder3.create().show();
        }
        else
        {
            pauseTimer();
            button.setBackgroundColor(Color.RED);
            AlertDialog.Builder builder = new AlertDialog.Builder(ActivitateIntrebare.this);
            builder.setTitle("GRESIT :(((");
            builder.setCancelable(false);
            builder.setMessage("Raspunsul tau este gresit."+"\n\n" +"Daca vrei sa-l afli pe cel corect te va costa 2 puncte!"+"\nVrei sa-l afli?");
            builder.setPositiveButton("DA", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    pauseTimer();
                    String raspunsComplet = IntrebariRaspunsuri.raspunsComplet[indexIntrebareCurenta];
                    if(score>=2) {
                        AlertDialog.Builder builder4 = new AlertDialog.Builder(ActivitateIntrebare.this);
                        builder4.setTitle("NU SPUNE LA NIMENI")
                                .setMessage("Raspunsul corect este: " + raspunsComplet);
                        builder4.setCancelable(false);
                        builder4.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(ActivitateIntrebare.this, HomePage.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                        builder4.create().show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Pentru a vedea raspunsul a nevoie de minim 2 puncte!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ActivitateIntrebare.this, HomePage.class);
                        startActivity(intent);
                        finish();

                    }

                }
            });

            builder.setNegativeButton("NU", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(ActivitateIntrebare.this, HomePage.class);
                    startActivity(intent);
                    finish();
                }
            });
            builder.create().show();
        }
        punctaj.setText("Punctaj "+score);
        raspunsuriTotale++;
    }

    public void startTimer()
    {
        timer = new CountDownTimer(timp_ramas_in_milis, 1000) {
            @Override
            public void onTick(long l) {
                timp_ramas_in_milis = l;
                updateText();
            }

            @Override
            public void onFinish() {

//                timmer_runnig =false;
//                pauseTimer();
//                resetTimer();
//                updateText();
                AlertDialog.Builder builder5 = new AlertDialog.Builder(ActivitateIntrebare.this);
            builder5.setTitle("GAME OVER :(((");
            builder5.setCancelable(false);
            builder5.setMessage("Ne pare rau, dar nu ai raspuns in timpul alocat!");
            builder5.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent5 = new Intent(ActivitateIntrebare.this, HomePage.class);
                    startActivity(intent5);
                    finish();
                }
            });
            builder5.create().show();
            }
        }.start();
        timmer_runnig = true;
    }
    public void updateText()
    {
        int second = (int)(timp_ramas_in_milis/1000)%60;
        String time_left = String.format(Locale.getDefault(), "%02d", second);//format in two digits
        time.setText(time_left);//write the on to the time text
    }
    public  void pauseTimer()
    {
        timer.cancel();
        timmer_runnig=false;
    }

    public  void resetTimer()
    {
        timp_ramas_in_milis = START_TIMER_IN_MILS;
        updateText();
    }
}