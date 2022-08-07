package com.example.loginregisterauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Announcement extends AppCompatActivity {

    RecyclerView recyclerView;
    AnnouncementUserAdapter announcementUserAdapter;
    FirebaseAuth auth;
    BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);

        recyclerView = findViewById(R.id.rvannouncement);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<AnnouncementUserModel> options = new FirebaseRecyclerOptions.Builder<AnnouncementUserModel>().setQuery(FirebaseDatabase.getInstance()
                .getReference().child("Announcements"),AnnouncementUserModel.class).build();

        announcementUserAdapter = new AnnouncementUserAdapter(options);
        recyclerView.setAdapter(announcementUserAdapter);

        //navigationView = findViewById(R.id.bottom_navigation);
        /*
        //perform ItemSelectedListener
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_profile:
                        startActivity(new Intent(getApplicationContext(),AccountMenu.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_announcement:
                        startActivity(new Intent(getApplicationContext(), Announcement.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.nav_map:
                        //validate the user needed to verfied
                        if(!auth.getCurrentUser().isEmailVerified()){
                            Toast.makeText(Announcement.this, "You need to be verified to access the map", Toast.LENGTH_SHORT).show();
                        }
                        //if the user is verified process to go the map
                        if(auth.getCurrentUser().isEmailVerified()){
                            startActivity(new Intent(getApplicationContext(), Map.class));
                            overridePendingTransition(0, 0);
                            return true;
                        }
                    case R.id.nav_feedback:
                        //validate the user needed to verified
                        if(!auth.getCurrentUser().isEmailVerified()){
                            Toast.makeText(Announcement.this, "You need to be verified to give feedback", Toast.LENGTH_SHORT).show();
                        }
                        //if the user is verified process to go the map
                        if(auth.getCurrentUser().isEmailVerified()){
                            startActivity(new Intent(getApplicationContext(), Feedback.class));
                            overridePendingTransition(0, 0);
                            return true;
                        }

                }

                return false;
            }
        });*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        announcementUserAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        announcementUserAdapter.stopListening();
    }
}