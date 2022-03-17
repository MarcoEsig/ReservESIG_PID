package com.example.reservesig_pid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.reservesig_pid.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //initialistion firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

        //setup progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Attendez svp");
        progressDialog.setCanceledOnTouchOutside(false);


        //back
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //click register
        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });

    }
    private String name = "", email = "", password = "";
    private void validateData(){

        //je prendre donnée
        name = binding.nameEt.getText().toString().trim();
        email = binding.emailEt.getText().toString().trim();
        password = binding.passwordEt.getText().toString().trim();
        String cPassword = binding.cPasswordEt.getText().toString().trim();

        //validation
        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "Entrez votre nom..", Toast.LENGTH_SHORT).show();
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Email invalide...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Entrez le mot de passe..", Toast.LENGTH_SHORT).show();
        }
        else if (!password.equals(cPassword)){
            Toast.makeText(this, "Les mots de passe ne sont pas les mêmes..", Toast.LENGTH_SHORT).show();
        }
        else {
            createAccount();
        }
    }
    private void createAccount(){
        progressDialog.setMessage("Creation du compte");
        progressDialog.show();

        //creation de l'utilisateur firebase
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                //creation du compte réussi, ajout dans firebase
                updateUserInfo();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserInfo(){
        progressDialog.setMessage("Sauvegarde des infos..");

        long timestamp = System.currentTimeMillis();

        //Prend l'id de l'utilisateur
        String uid = firebaseAuth.getUid();

        //info pour ajout bdd
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("uid",uid);
        hashMap.put("email",email);
        hashMap.put("name",name);
        hashMap.put("profileImage","");
        hashMap.put("userType","user");//valeur possible admin et user je vais le faire manuellement dans la bdd les changements
        hashMap.put("timestamp",timestamp);

        //ajout donnée à la bdd
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(uid).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                //donnée ajouté dans bdd
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, "Compte crée", Toast.LENGTH_SHORT).show();

                //vu que le user à été créer on l'envois dans le dashboard utilisateur
                startActivity(new Intent(RegisterActivity.this,DashBoardUserActivity.class));
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //fail d'ajout bdd
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, ""+ e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }
}