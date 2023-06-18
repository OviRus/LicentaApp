package com.example.licenta2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Statistici extends AppCompatActivity {

    TextView raspunsuriCorecte, intrebariTotale, acurateteIntrebari;

    BazaDateDAO dao = new BazaDateDAO(this);
    String emailUser = EmailClass.getUserEmail();


    ActivitateIntrebare activ = new ActivitateIntrebare();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistici);

        raspunsuriCorecte = findViewById(R.id.textViewCorect);
        intrebariTotale = findViewById(R.id.textViewIntrebareTotal);
        acurateteIntrebari = findViewById(R.id.textViewAcuratete);

        dao.open();
        int   corectNumber = dao.fetchUserTotalCorrectAnswers(emailUser);
        int totalNumber = dao.fetchUserTotalAnswers(emailUser);
        dao.close();
        float accuracy = (corectNumber / (float) totalNumber) * 100;
        String formattedAccuracy = String.format("%.2f", accuracy);
        raspunsuriCorecte.setText("Raspunsuri corecte: "+ corectNumber);
        intrebariTotale.setText("Numarul de intrebari raspunse: "+totalNumber);
        acurateteIntrebari.setText("Acuratetea: "+formattedAccuracy+"%");

    }
}