package com.example.loginregisterauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class AnnouncementInput extends AppCompatActivity {
    Button uploadBtn,cancelBtn,viewannBtn;

    FirebaseAuth Fauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_input);

        //Edit texts forms
        final EditText aTopic = findViewById(R.id.aTopic);
        final EditText aSender = findViewById(R.id.aSender);
        final EditText aMessage = findViewById(R.id.aMessage);

        // Buttons
        uploadBtn = findViewById(R.id.uploadbutton);
        viewannBtn = findViewById(R.id.viewAbtn);
        cancelBtn = findViewById(R.id.cancelbtn);

        //Authentication
        Fauth = FirebaseAuth.getInstance();

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // extract data from the forms
                String topic = aTopic.getText().toString();
                String sender = aSender.getText().toString();
                String message = aMessage.getText().toString();

                //check if user filled all the fields before sending data to firebase

                if(topic.isEmpty() ){
                    aTopic.setError("Field is Required");
                    return;
                }
                if(sender.isEmpty()){
                    aSender.setError("Field is Required");
                    return;
                }
                if(message.isEmpty()){
                    aMessage.setError("Field is Required");
                    return;
                }

                AnnouncementModel announcementModel = new AnnouncementModel(topic,sender,message);

                FirebaseDatabase.getInstance().getReference("Announcements").push().setValue(announcementModel)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                Toast.makeText(AnnouncementInput.this, "Announcement Uploaded", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),AdminDashboardMenu.class));
                                finish();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AnnouncementInput.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

        viewannBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AnnouncementView.class));
                finish();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AdminDashboardMenu.class));
                finish();
            }
        });
    }
}