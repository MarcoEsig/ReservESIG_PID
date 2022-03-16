package com.example.reservesig_pid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        //Affiche l'écan principal après 2 seconde
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                //start écran principal
                startActivity(new Intent(IntroActivity.this,MainActivity.class));
                finish(); //"fini" cette activité
            }
        },2000); //2000 ms = 2 seconde
    }
}