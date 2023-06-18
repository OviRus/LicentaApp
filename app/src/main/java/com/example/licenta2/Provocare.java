package com.example.licenta2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Provocare extends AppCompatActivity {

    TextView time, question, punctaj;
    Button button1, button2, button3, button4;
    ImageView imageView;
    public int raspunsuriTotale = 0;
    public int raspunsuriCorecte = 0;
    CountDownTimer timer;
    private static final long START_TIMER_IN_MILS = 20000;
    Boolean timmer_runnig;
    long timp_ramas_in_milis = START_TIMER_IN_MILS;
    int score=0;
    int intrebareCompleta = IntrebariRaspunsuri.intrebare.length;
    int indexIntrebareCurenta;
    String raspunsSelectat = "";
    List<Integer> askedQuestionIndices = new ArrayList<>();
    Random random = new Random();
    int randomIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provocare);

        time = findViewById(R.id.textViewTime);
        question = findViewById(R.id.textViewQuestion);
        punctaj= findViewById(R.id.textViewPunctaj);
        button1 = findViewById(R.id.buttonRaspuns1);
        button2 = findViewById(R.id.buttonRaspuns2);
        button3 = findViewById(R.id.buttonRaspuns3);
        button4 = findViewById(R.id.buttonRaspuns4);
        imageView = findViewById(R.id.imageViewHint);
        String userEmail = getIntent().getStringExtra("userEmail");
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

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

    }

    @Override
    protected void onPause() {
        pauseTimer();
        super.onPause();
    }

    void intrebareNoua() {

        do {
            randomIndex = random.nextInt(IntrebariRaspunsuri.intrebareProvocare.length);
        } while (askedQuestionIndices.contains(randomIndex));

        askedQuestionIndices.add(randomIndex);

        if (askedQuestionIndices.size() == IntrebariRaspunsuri.intrebareProvocare.length) {
            askedQuestionIndices.clear();
        }

        question.setText(IntrebariRaspunsuri.intrebareProvocare[randomIndex]);
        button1.setText(String.valueOf(IntrebariRaspunsuri.varianteProvocare[randomIndex][0]));
        button2.setText(String.valueOf(IntrebariRaspunsuri.varianteProvocare[randomIndex][1]));
        button3.setText(String.valueOf(IntrebariRaspunsuri.varianteProvocare[randomIndex][2]));
        button4.setText(String.valueOf(IntrebariRaspunsuri.varianteProvocare[randomIndex][3]));
    }

    void verificareRaspuns(Button button)
    {
        String userEmail = getIntent().getStringExtra("userEmail");
        String textButton = button.getText().toString();
        raspunsSelectat = IntrebariRaspunsuri.raspunsuriCorecteProvocare[randomIndex];
        if (textButton.equals(raspunsSelectat))
        {

            raspunsuriCorecte++;
            button.setBackgroundColor(Color.GREEN);
            score++;
            pauseTimer();
            AlertDialog.Builder builder3 = new AlertDialog.Builder(Provocare.this);
            builder3.setTitle("FELICITARI! :)))");
            builder3.setCancelable(false);
            builder3.setMessage("Doresti sa raspunzi la o alta intrebare?");
            builder3.setPositiveButton("DA", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    indexIntrebareCurenta = randomIndex;
                    pauseTimer();
                    resetTimer();
                    intrebareNoua();
                    startTimer();

                }
            });
            builder3.setNegativeButton("NU", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(Provocare.this, HomePage.class);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(Provocare.this);
            builder.setTitle("GRESIT :(((");
            builder.setCancelable(false);
            builder.setMessage("Raspunsul tau este gresit."+"\n\n" +"Mai jos vei primi raspunsul corect!");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    pauseTimer();
                    String raspunsComplet = IntrebariRaspunsuri.raspunsCompletProvocare[randomIndex];

                        AlertDialog.Builder builder4 = new AlertDialog.Builder(Provocare.this);
                        builder4.setTitle("NU SPUNE LA NIMENI")
                                .setMessage("Raspunsul corect este: " + raspunsComplet);
                        builder4.setCancelable(false);
                        builder4.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Provocare.this, HomePage.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                        builder4.create().show();
                }
            });

            builder.setNegativeButton("NU", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(Provocare.this, HomePage.class);
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
                AlertDialog.Builder builder5 = new AlertDialog.Builder(Provocare.this);
                builder5.setTitle("GAME OVER :(((");
                builder5.setCancelable(false);
                builder5.setMessage("Ne pare rau, dar nu ai raspuns in timpul alocat!");
                builder5.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent5 = new Intent(Provocare.this, HomePage.class);
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
        String time_left = String.format(Locale.getDefault(), "%02d", second);
        time.setText(time_left);
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