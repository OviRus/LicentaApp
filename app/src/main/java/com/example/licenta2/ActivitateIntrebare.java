package com.example.licenta2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class ActivitateIntrebare extends AppCompatActivity  {

    TextView time, question, punctaj;
    Button button1, button2, button3, button4, button;
    ImageView imageView;
    public int totalAnswers = 0;
    public int correctResponses = 0;
    CountDownTimer timer;
    private static final long START_TIMER_IN_MILS = 20000;
    Boolean timmer_runnig;
    long time_left_in_milis = START_TIMER_IN_MILS;
    int consecutiveCorrectAnswers = 0;

    int intrebareCompleta = IntrebariRaspunsuri.intrebare.length;
    int indexIntrebareCurenta;
    String raspunsSelectat = "";
    List<Integer> askedQuestionIndices = new ArrayList<>();
    Random random = new Random();
    int randomIndex;
    int imageViewClickCount;
    Button invisibleButton = null;

    BazaDateDAO dao = new BazaDateDAO(this);

    String userEmail = EmailClass.getUserEmail();

    int score = 0;

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
        imageView = findViewById(R.id.imageViewHint);


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

    private void handleImageViewClick(ImageView imageView) {
        imageViewClickCount++;

        if (imageViewClickCount == 1) {
            score -= 1;
            int randomButtonIndex = new Random().nextInt(4);
            Button selectedButton = null;
            switch (randomButtonIndex) {
                case 0:
                    selectedButton = button1;
                    break;
                case 1:
                    selectedButton = button2;
                    break;
                case 2:
                    selectedButton = button3;
                    break;
                case 3:
                    selectedButton = button4;
                    break;
            }

            if (selectedButton != null) {
                if (selectedButton.getText().equals(IntrebariRaspunsuri.raspunsuriCorecte[randomIndex])) {
                    handleImageViewClick(imageView);
                    return;
                } else {
                    selectedButton.setVisibility(View.INVISIBLE);
                    invisibleButton = selectedButton; // Store the currently invisible button
                }
            }
        } else if (imageViewClickCount == 2) {
            score -= 3;
            if (invisibleButton != null) {
                invisibleButton.setVisibility(View.VISIBLE); // Make the previously invisible button visible again
                invisibleButton = null; // Reset the invisible button reference
            }
        }

        punctaj.setText("Punctaj: " + score);
        imageView.setEnabled(false);
    }

    void intrebareNoua() {

        do {
            randomIndex = random.nextInt(IntrebariRaspunsuri.intrebare.length);
        } while (askedQuestionIndices.contains(randomIndex));

        askedQuestionIndices.add(randomIndex);

        if (askedQuestionIndices.size() == IntrebariRaspunsuri.intrebare.length) {
            askedQuestionIndices.clear();
        }

        question.setText(IntrebariRaspunsuri.intrebare[randomIndex]);
        button1.setText(String.valueOf(IntrebariRaspunsuri.variante[randomIndex][0]));
        button2.setText(String.valueOf(IntrebariRaspunsuri.variante[randomIndex][1]));
        button3.setText(String.valueOf(IntrebariRaspunsuri.variante[randomIndex][2]));
        button4.setText(String.valueOf(IntrebariRaspunsuri.variante[randomIndex][3]));
    }

    void verificareRaspuns(Button button)
    {
        score=showPoints();
        String textButton = button.getText().toString();
       raspunsSelectat = IntrebariRaspunsuri.raspunsuriCorecte[randomIndex];
        if (textButton.equals(raspunsSelectat))
        {
            consecutiveCorrectAnswers++;
            score++;
            dao.updateUserPoints(userEmail,score);
            correctResponses++;
            dao.incrementUserCorrectAnswers(userEmail,correctResponses);
            if (consecutiveCorrectAnswers == 3){
                score += 6;
                consecutiveCorrectAnswers = 0;
                Toast.makeText(this, "Felicitari! Ai primit 6 puncte bonus, deoarece ai raspuns corect la 3 intrebari consecutiv!", Toast.LENGTH_SHORT).show();
            }
            button.setBackgroundColor(Color.GREEN);
            pauseTimer();
            AlertDialog.Builder builder3 = new AlertDialog.Builder(ActivitateIntrebare.this);
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
                    dao.updateUserPoints(userEmail,score);
                    Intent intent = new Intent(ActivitateIntrebare.this, HomePage.class);
                    startActivity(intent);
                    finish();
                }
            });
            builder3.create().show();

        }
        else
        {
            score = showPoints();
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
                    String raspunsComplet = IntrebariRaspunsuri.raspunsComplet[randomIndex];

                    if(score>=2) {
                        score=score-2;
                        dao.updateUserPoints(userEmail,score);
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
        totalAnswers++;
        dao.incrementUserTotalAnswers(userEmail,totalAnswers);
        punctaj.setText("Punctaj: "+score);
       // displayPoints(score);
    }

    public void startTimer()
    {
        timer = new CountDownTimer(time_left_in_milis, 1000) {
            @Override
            public void onTick(long l) {
                time_left_in_milis = l;
                updateText();
            }

            @Override
            public void onFinish() {
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
        int second = (int)(time_left_in_milis/1000)%60;
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
        time_left_in_milis = START_TIMER_IN_MILS;
        updateText();
    }

    public int showPoints(){
        dao.open();
        score= dao.fetchUserPoints(userEmail);
        return score;
    }

    public void displayPoints(int scoreResult){
        dao.open();
        int score1 = dao.fetchUserPoints(userEmail);
        dao.close();
        scoreResult =+score1;
        punctaj.setText("Punctaj: " + String.valueOf(scoreResult));
    }

}