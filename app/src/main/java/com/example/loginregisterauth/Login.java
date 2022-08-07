package com.example.loginregisterauth;

import static android.view.View.VISIBLE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;


public class Login extends AppCompatActivity {

    Button createAccountBtn,loginBtn,forgetPasswordBtn,gotoAdminLogin;
    EditText userEmail,password;
    AlertDialog.Builder reset_alert;
    LayoutInflater inflater;


    private FirebaseUser user;
    private DatabaseReference reference;

    private String userID;
    FirebaseAuth auth;

    boolean valid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        reset_alert = new AlertDialog.Builder(this);
        inflater = this.getLayoutInflater();



        //Create User Account
        createAccountBtn = findViewById(R.id.createAccountBtn);
        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Register.class));
            }
        });

        userEmail = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
        loginBtn = findViewById(R.id.loginBtn);
        forgetPasswordBtn = findViewById(R.id.forgetPasswordbtn);

        forgetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start alertdialog
                View view = inflater.inflate(R.layout.reset_popup,null);

                reset_alert.setTitle("Reset Password")
                        .setMessage("Enter Your Email to get your Password Reset Link")
                        .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //validate the email
                                EditText email = view.findViewById(R.id.reset_email_popup);
                                if(email.getText().toString().isEmpty()){
                                    email.setError("Required Field");
                                    return;
                                }
                                //send the reset link
                                auth.sendPasswordResetEmail(email.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(Login.this, "Password Reset Link Sent", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).setNegativeButton("Cancel",null)
                        .setView(view)
                        .create().show();

            }
        });

        //user login
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // extract / validate
                String email = userEmail.getText().toString().trim();
                String pass = password.getText().toString();

                //checking validity of user Email
                if (userEmail.getText().toString().isEmpty()){
                    userEmail.setError("Email is Missing");
                    return;
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    userEmail.setError("Enter a valid email address");
                    return;
                }
                //checking validity of user password

                if (password.getText().toString().isEmpty()){
                    password.setError("Password is Missing");
                    return;
                }
                if (password.length() < 6) {
                    password.setError("Password Length Must be 6 Characters");
                    return;
                }

                auth.signInWithEmailAndPassword(userEmail.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            //user id
                            //User Access Admin/User Level
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            database.getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("usertype").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    //getting the usertype
                                    int usertype = snapshot.getValue(Integer.class);
                                    //user type 0 for user dashboard
                                    if(usertype==0){
                                        //login to the user dashboard
                                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                        finish();
                                    }
                                    //user type 1 for the admin dashboard
                                    if(usertype==1){
                                        startActivity(new Intent(getApplicationContext(),AdminDashboardMenu.class));
                                        finish();
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(Login.this, "Something when wrong", Toast.LENGTH_SHORT).show();
                                }
                            });


                        }
                        else {
                            //for error checking
                            try{
                                throw task.getException();
                            }catch (Exception e){
                                Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                    }
                });


            }
        });
    }
}