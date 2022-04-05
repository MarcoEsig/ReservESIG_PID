package com.example.reservesig_pid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.reservesig_pid.databinding.ActivityPdfEditBinding;

public class PdfEditActivity extends AppCompatActivity {

    private ActivityPdfEditBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPdfEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}