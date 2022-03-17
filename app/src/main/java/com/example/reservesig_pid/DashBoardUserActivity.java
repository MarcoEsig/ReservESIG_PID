package com.example.reservesig_pid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.reservesig_pid.databinding.ActivityDashBoardAdminBinding;
import com.example.reservesig_pid.databinding.ActivityDashBoardUserBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashBoardUserActivity extends AppCompatActivity {

    private ActivityDashBoardUserBinding binding;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashBoardUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //initialistion firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();

        //click logout
        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                checkUser();
            }
        });


    }

    private void checkUser() {

        //prend le user actuelle, si loggué
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser ==null){
            //user pas loggué
            //start écran principal
            startActivity(new Intent(this,MainActivity.class));
            finish(); //"fini" cette activité
        }
        else {
            String email = firebaseUser.getEmail();
            //met dans la textview
            binding.subTitleTv.setText(email);
        }

    }
}