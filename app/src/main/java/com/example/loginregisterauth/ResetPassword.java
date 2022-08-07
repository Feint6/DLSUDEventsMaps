package com.example.loginregisterauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ResetPassword extends AppCompatActivity {
    EditText userPassword,userConfPassword,currentUserPassword;
    Button savePasswordBtn,returnBtn;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        //new
        userPassword = findViewById(R.id.newUserPassword);
        userConfPassword = findViewById(R.id.newConfirmPass);



        user = FirebaseAuth.getInstance().getCurrentUser();

        returnBtn = findViewById(R.id.returnbtn);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SettingsMenu.class));
                finish();
            }
        });

        savePasswordBtn = findViewById(R.id.Savebtn);
        savePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newpassword = userPassword.getText().toString();
                String confirmpass = userConfPassword.getText().toString();

                // extract / validate

                if (newpassword.isEmpty()){
                    userPassword.setError("Required Field");
                    return;
                }
                if (confirmpass.isEmpty()){
                    userConfPassword.setError("Required Field");
                    return;
                }



                if(!newpassword.equals(confirmpass)){
                    userConfPassword.setError("Password does not match");
                    return;
                }
                if(newpassword.length()<6){
                    userPassword.setError("Password length must be at least 6 characters");
                    return;
                }

                        user.updatePassword(userPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                //successfully updated your password
                                Toast.makeText(ResetPassword.this, "Password Updated", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),SettingsMenu.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                    //Change password failed, show reason
                                Toast.makeText(ResetPassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                });




            }

        });

    }
}