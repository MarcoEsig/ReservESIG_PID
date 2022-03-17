package com.example.reservesig_pid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class IntroActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        //initialistion firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

        //Affiche l'écan principal après 2 seconde
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                    checkUser();
            }
        },2000); //2000 ms = 2 seconde
    }

    private void checkUser() {

        //prend le user actuelle, si loggué
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser ==null){
            //user pas loggué
            //start écran principal
            startActivity(new Intent(IntroActivity.this,MainActivity.class));
            finish(); //"fini" cette activité
        }
        else {
            //user vient de se loggué on check son type
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
            ref.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //prend le usertype
                    String userType = "" + dataSnapshot.child("userType").getValue();
                    //verifie
                    if (userType.equals("user")){
                        //open dashboard user
                        startActivity(new Intent(IntroActivity.this,DashBoardUserActivity.class));
                        finish();
                    }
                    else if (userType.equals("admin")){
                        //open dashboard admin
                        startActivity(new Intent(IntroActivity.this,DashBoardAdminActivity.class));
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }
}