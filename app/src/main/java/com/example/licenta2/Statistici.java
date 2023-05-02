package com.example.licenta2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Statistici extends AppCompatActivity {

    TextView raspunsuriCorecte, intrebariTotale, acurateteIntrebari;

    int corect = 0,total= 0;
    float acuratete;

    int numarCorect, numarTotal;

    ActivitateIntrebare activ = new ActivitateIntrebare();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistici);

        raspunsuriCorecte = findViewById(R.id.textViewCorect);
        intrebariTotale = findViewById(R.id.textViewIntrebareTotal);
        acurateteIntrebari = findViewById(R.id.textViewAcuratete);

        corect = activ.raspunsuriCorecte;
        total = activ.raspunsuriTotale;

        numarCorect = numarCorect+corect;
        numarTotal = numarTotal+total;

        acuratete = (numarCorect/numarTotal)*100;


        raspunsuriCorecte.setText("Raspunsuri corecte: "+ numarCorect);
        intrebariTotale.setText("Numarul de intrebari raspunse: "+numarTotal);
        acurateteIntrebari.setText("Acuratetea: "+acuratete+"%");

    }
}