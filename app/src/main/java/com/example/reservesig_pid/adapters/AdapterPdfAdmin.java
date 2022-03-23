package com.example.reservesig_pid.adapters;

import static com.example.reservesig_pid.Constants.MAX_BYTES_PDF;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reservesig_pid.MyApplication;
import com.example.reservesig_pid.databinding.RowPdfAdminBinding;
import com.example.reservesig_pid.models.ModelPdf;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AdapterPdfAdmin extends RecyclerView.Adapter<AdapterPdfAdmin.HolderPdfAdmin> {

    private Context context;

    private ArrayList<ModelPdf> pdfArrayList;

    private RowPdfAdminBinding binding;

    private static final String TAG = "PDF_ADAPTER_TAG";

    public AdapterPdfAdmin(Context context, ArrayList<ModelPdf> pdfArrayList) {
        this.context = context;
        this.pdfArrayList = pdfArrayList;
    }

    @NonNull
    @Override
    public HolderPdfAdmin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowPdfAdminBinding.inflate(LayoutInflater.from(context),parent,false);
        return new HolderPdfAdmin(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderPdfAdmin holder, int position) {

        ModelPdf model = pdfArrayList.get(position);
        String title = model.getTitle();
        String description = model.getDescription();
        long timestamp = model.getTimestamp();

        //convertion timestamp
        String formattedDate = MyApplication.formatTimeStamp(timestamp);


        holder.titleTv.setText(title);
        holder.descriptionTv.setText(description);
        holder.dateTv.setText(formattedDate);

        //chargement
        loadCategory(model,holder);
        loadPdfFromUrl(model,holder);
        loadPdfSize(model,holder);

    }

    private void loadPdfSize(ModelPdf model, HolderPdfAdmin holder) {

        //en utilisant l'ûrl on peut prendre le fichier et ces metadata de firebase

        String pdfUrl = model.getUrl();

        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(pdfUrl);
        ref.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata storageMetadata) {

                double bytes = storageMetadata.getSizeBytes();

                double kb = bytes/1024;
                double mb = kb/1024;

                if (mb >=1){
                    holder.sizeTv.setText(String.format("%.2f",mb)+" MB");
                }
                else if (kb >=1){
                    holder.sizeTv.setText(String.format("%.2f",kb)+" KB");
                }
                else {
                    holder.sizeTv.setText(String.format("%.2f",bytes)+" bytes");
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadPdfFromUrl(ModelPdf model, HolderPdfAdmin holder) {

        String pdfUrl = model.getUrl();
        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(pdfUrl);
        ref.getBytes(MAX_BYTES_PDF).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Log.d(TAG,"onSuccess: "+model.getTitle()+ " succesfully got the file");

                //pdf view
                holder.pdfView.fromBytes(bytes)
                        .pages(0)
                        .spacing(0)
                        .swiperHorizontal(false)
                        .enableSwipe(false)
                        .onError(new OnErrorListener(){
                            public void onError(Throwable t){
                                Log.d(TAG,"onError: "+t.getMessage());
                            }
                        }).onPageError(new OnPageErrorListener(){
                            public void onPageError(int page,Throwable t){
                                Log.d(TAG,"onPageError: "+t.getMessage());
                            }
                }).load();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,"onFailure: failed getting file from url due to" + e.getMessage());

            }
        });

    }

    private void loadCategory(ModelPdf model, HolderPdfAdmin holder) {
        //get catego en utilisant l'id

        String categoryId = model.getCategoryId();

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Categories");
        ref.child(categoryId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String category = ""+snapshot.child("category").getValue();

                holder.categoryTv.setText(category);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return pdfArrayList.size();
    }

    class HolderPdfAdmin extends RecyclerView.ViewHolder{

        PDFView pdfView;
        ProgressBar progressBar;
        TextView titleTv,descriptionTv,categoryTv,sizeTv,dateTv;
        ImageButton moreBtn;

      public HolderPdfAdmin(@NonNull View itemView) {
          super(itemView);

          pdfView = binding.pdfView;
          progressBar = binding.progressBar;
          titleTv = binding.titleTv;
          descriptionTv = binding.descriptionTv;
          categoryTv = binding.categoryTv;
          sizeTv = binding.sizeTv;
          dateTv = binding.dateTv;
          moreBtn = binding.moreBtn;
      }
  }

}
