package com.example.reservesig_pid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.example.reservesig_pid.databinding.ActivityDashBoardAdminBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

        //edit text change pour la search bar
        binding.searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                //que je tape une lettre sa change
                try {
                    adapterCategory.getFilter().filter(s);
                }
                catch (Exception e)
                {

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

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
        //pdf click
        binding.addPdfFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashBoardAdminActivity.this, PdfAddActivity.class));
            }
        });

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
                }

                    //setup l'adapter
                    adapterCategory = new AdapterCategory(DashBoardAdminActivity.this,categoryArrayList);

                    //on set le adapter au Rv
                    binding.categoriesRv.setAdapter(adapterCategory);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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