package com.example.reservesig_pid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Adapter;

import com.example.reservesig_pid.adapters.AdapterPdfAdmin;
import com.example.reservesig_pid.databinding.ActivityPdfListAdminBinding;
import com.example.reservesig_pid.models.ModelPdf;

import java.util.ArrayList;

public class PdfListAdminActivity extends AppCompatActivity {

    private ActivityPdfListAdminBinding binding;
    
    private ArrayList<ModelPdf> pdfArrayList;
    
    private AdapterPdfAdmin adapterPdfAdmin;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPdfListAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        loadPdfList();
        
        
        
        
    }

    private void loadPdfList() {
    }
}