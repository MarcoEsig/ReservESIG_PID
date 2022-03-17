package com.example.reservesig_pid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.reservesig_pid.databinding.ActivityCategoryAddBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CategoryAddActivity extends AppCompatActivity {

    private ActivityCategoryAddBinding binding;
    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //initialistion firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Attendez");
        progressDialog.setCanceledOnTouchOutside(false);

        //back
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });

    }

    private String category ="";
    private void validateData() {

        //prendre les données
        category = binding.categoryEt.getText().toString().trim();

        if (TextUtils.isEmpty(category))
        {
            Toast.makeText(this, "Entrer une catégorie..", Toast.LENGTH_SHORT).show();
        }
        else  {
            addCategoryFirebase();
        }
    }

    private void addCategoryFirebase() {

        progressDialog.setMessage("Ajout catégorie..");
        progressDialog.show();

        long timestamp = System.currentTimeMillis();

        //Prend l'id de l'utilisateur
        String uid = firebaseAuth.getUid();

        //info pour ajout bdd
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("id",+timestamp);
        hashMap.put("category",""+category);
        hashMap.put("timestamp",timestamp);
        hashMap.put("uid",""+firebaseAuth.getUid());

        //ajout donnée à la bdd des catégo
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.child(""+timestamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                //donnée ajouté dans bdd
                progressDialog.dismiss();
                Toast.makeText(CategoryAddActivity.this, "Catégorie ajoutée avec succès...", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //catégo fail
                progressDialog.dismiss();
                Toast.makeText(CategoryAddActivity.this, ""+ e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
}