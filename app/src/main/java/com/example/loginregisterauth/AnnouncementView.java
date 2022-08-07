package com.example.loginregisterauth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class AnnouncementView extends AppCompatActivity {

    RecyclerView recyclerView;
    AnnouncementAdapter announcementAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_view);

        recyclerView = findViewById(R.id.rvA);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<AnnouncementModel> options = new FirebaseRecyclerOptions.Builder<AnnouncementModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Announcements"),AnnouncementModel.class).build();

        announcementAdapter = new AnnouncementAdapter(options);
        recyclerView.setAdapter(announcementAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        announcementAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        announcementAdapter.stopListening();
    }
}