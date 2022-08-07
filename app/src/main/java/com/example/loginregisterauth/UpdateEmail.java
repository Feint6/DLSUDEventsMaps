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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class UpdateEmail extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email);

        Button updateEmail, gotosettings;

        updateEmail = findViewById(R.id.updateEmailBtn);
        gotosettings = findViewById(R.id.returnUpdateBtn);

        final EditText newEmail = findViewById(R.id.newEmail);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        updateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validate the email & phonenumber
                String email = newEmail.getText().toString().trim();

                if(newEmail.getText().toString().isEmpty()){
                    newEmail.setError("Required Field");
                    return;
                }
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    newEmail.setError("Enter a valid email address");
                    return;
                }
                //update the email
                        user.updateEmail(newEmail.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                //successfully updated your email
                                //update email on realtime database
                                //setting the key to get the current user
                                String key = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                HashMap<String,Object> data = new HashMap<>();
                                data.put("email",email);

                                databaseReference.child(key).updateChildren(data).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {

                                        if (task.isSuccessful()){
                                            Toast.makeText(UpdateEmail.this,"Email has been updated please login with the new email",Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            Toast.makeText(UpdateEmail.this,"Failed to Update",Toast.LENGTH_SHORT).show();

                                        }

                                    }
                                });


                                //working on authentication but not on realtime database

                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(UpdateEmail.this,Login.class));
                                finish();
                                //updateData(phoneNo,email);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UpdateEmail.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        //return to SettingsMenu
        gotosettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SettingsMenu.class));
                finish();
            }
        });
    }
}