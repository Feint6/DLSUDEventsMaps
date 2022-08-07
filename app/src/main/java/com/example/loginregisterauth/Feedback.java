package com.example.loginregisterauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Feedback extends AppCompatActivity{

    Button Submitbtn, cancelbtn;

    FirebaseAuth Fauth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        Fauth = FirebaseAuth.getInstance();

        Submitbtn = findViewById(R.id.Submitbtn);
        cancelbtn = findViewById(R.id.cancelbtn);
        final EditText userEmail = findViewById(R.id.userEmail);
        final EditText userComment = findViewById(R.id.userComment);

        Submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // extract data from the forms
                String useremail = userEmail.getText().toString();
                String usercomment = userComment.getText().toString();

                // check to see user filled important field
                if(usercomment.isEmpty()){
                    userComment.setError("This field is required");
                    return;
                }

                FeedbackModel feedbackModel = new FeedbackModel(useremail,usercomment);

                FirebaseDatabase.getInstance().getReference("Feedbacks")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .setValue(feedbackModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Toast.makeText(Feedback.this, "Thank you for your feedback!", Toast.LENGTH_SHORT).show();
                        // send user to dashboard
                        // registered feedback data using firebase
                        startActivity(new Intent(getApplicationContext(),AccountMenu.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Feedback.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AccountMenu.class));
                finish();
            }
        });

    }

}