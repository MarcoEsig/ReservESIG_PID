package com.example.reservesig_pid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.reservesig_pid.databinding.ActivityDashBoardAdminBinding;
import com.example.reservesig_pid.databinding.ActivityDashBoardUserBinding;
import com.example.reservesig_pid.models.ModelCategory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DashBoardUserActivity extends AppCompatActivity {


    public ArrayList<ModelCategory> categoryArrayList;
    public ViewPagerAdapter viewPagerAdapter;

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

        setupViewPagerAdapter(binding.viewPager);
        binding.tabLayout.setupWithViewPager(binding.viewPager);

        //click logout
        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                checkUser();
            }
        });


    }

    private void setupViewPagerAdapter(ViewPager viewPager){

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,this );

        categoryArrayList = new ArrayList<>();

        //load categorie
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot) {
                categoryArrayList.clear();

                //ajout data
                ModelCategory modelAll = new ModelCategory("01","All","","1");

                categoryArrayList.add(modelAll);

                viewPagerAdapter.addFragment(BooksUserFragment.newInstance(""+modelAll.getId(),""+modelAll.getCategory(),""+modelAll.getUid()), modelAll.getCategory());

                viewPagerAdapter.notifyDataSetChanged();

                //load de firebase les catego
                for (DataSnapshot ds: snapshot.getChildren()){
                    ModelCategory model = ds.getValue(ModelCategory.class);
                    categoryArrayList.add(model);

                    viewPagerAdapter.addFragment(BooksUserFragment.newInstance(""+model.getId(),""+model.getCategory(),""+model.getUid()), model.getCategory());

                    //refresh
                    viewPagerAdapter.notifyDataSetChanged();
                }
                viewPager.setAdapter(viewPagerAdapter);
            }

            @Override
            public void onCancelled( DatabaseError error) {
            }
        });
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter{

        private ArrayList<BooksUserFragment> fragmentList = new ArrayList<>();
        private ArrayList<String> fragmentTitleList = new ArrayList<>();
        private Context context;

        public ViewPagerAdapter(FragmentManager fm, int behavior, Context context) {
            super(fm, behavior);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        private void addFragment(BooksUserFragment fragment, String title){
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
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