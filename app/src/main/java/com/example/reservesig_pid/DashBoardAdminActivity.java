package com.example.reservesig_pid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.view.View;

import com.example.reservesig_pid.databinding.ActivityDashBoardAdminBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DashBoardAdminActivity extends AppCompatActivity {

    private ActivityDashBoardAdminBinding binding;
    private FirebaseAuth firebaseAuth;

    //arraylist catégo
    private ArrayList<ModelCategory> categoryArrayList;

    private AdapterCategory adapterCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashBoardAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //initialistion firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();
        loadCategories();

        //click logout
        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                checkUser();
            }
        });

        //click ajout categorie
        binding.addCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashBoardAdminActivity.this,CategoryAddActivity.class));
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
    private void loadCategories() {
        //init array
        categoryArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //on efface ce qui a dans arraylist avant de mettre des données
                categoryArrayList.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    //prend les données
                    ModelCategory model = ds.getValue(ModelCategory.class);

                    //ajout a l'arraylist
                    categoryArrayList.add(model);

                    //setup l'adapter
                    adapterCategory = new AdapterCategory(DashBoardAdminActivity.this,categoryArrayList);

                    //on set le adapter au Rv
                    binding.categoriesRv.setAdapter(adapterCategory);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}