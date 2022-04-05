package com.example.reservesig_pid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.reservesig_pid.databinding.ActivityPdfDetailBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PdfDetailActivity extends AppCompatActivity {

    private ActivityPdfDetailBinding binding;

    //id
    String bookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPdfDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();

        bookId = intent.getStringExtra("bookId");

        loadBookDetails();

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void loadBookDetails() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Books");
        ref.child(bookId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //prend les données
                String title = ""+ snapshot.child("title").getValue();
                String description = ""+snapshot.child("description").getValue();
                String categoryId = ""+snapshot.child("categoryId").getValue();
                String viewsCount = ""+snapshot.child("viewsCount").getValue();
                String url = ""+snapshot.child("url").getValue();
                //String timestamp = ""+ snapshot.child("timestramp").getValue();

                //String date = MyApplication.formatTimeStamp(Long.parseLong(timestamp));

                MyApplication.loadCategory(""+categoryId,binding.categoryTv);
                MyApplication.loadPdfFromUrlSinglePage(""+url,""+title,binding.pdfView,binding.progressBar);
                MyApplication.loadPdfSize(""+url,""+title,binding.sizeTv);

                //met donnée
                binding.titleTv.setText(title);
                binding.descriptionTv.setText(description);
                binding.viewsTv.setText(viewsCount.replace("null","N/A"));
                //binding.dateTv.setText(date);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}