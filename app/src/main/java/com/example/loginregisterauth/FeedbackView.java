package com.example.loginregisterauth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class FeedbackView extends AppCompatActivity {

    RecyclerView recyclerView;
    FeedbackAdapter feedbackAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_view);

        recyclerView = findViewById(R.id.rvfeedback);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<FeedbackModel> options = new FirebaseRecyclerOptions.Builder<FeedbackModel>().setQuery(FirebaseDatabase.getInstance()
                .getReference().child("Feedbacks"),FeedbackModel.class).build();

        feedbackAdapter = new FeedbackAdapter(options);
        recyclerView.setAdapter(feedbackAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        feedbackAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        feedbackAdapter.stopListening();
    }
}