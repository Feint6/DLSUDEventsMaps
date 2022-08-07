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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Register extends AppCompatActivity {
    Button registerUserBtn,gotoLogin;

    FirebaseAuth Fauth;


    //create object of databasereference class to access firebase realtime database

    //FirebaseDatabase database = FirebaseDatabase.getInstance();
    //DatabaseReference databaseReference = database.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Edit texts forms
        final EditText registerFirstname = findViewById(R.id.registerFirstname);
        final EditText registerLastname = findViewById(R.id.registerLastname);
        final EditText registerEmail = findViewById(R.id.registerEmail);
        final EditText registerPassword = findViewById(R.id.registerPassword);
        final EditText registerConfPass = findViewById(R.id.registerConfPass);
        final EditText registerPhoneNumber = findViewById(R.id.registerPhoneNumber);

        // Buttons
        registerUserBtn = findViewById(R.id.registerUserBtn);
        gotoLogin = findViewById(R.id.gotoLogin);

        //Authentication
        Fauth = FirebaseAuth.getInstance();




        gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();
            }
        });

        registerUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // extract data from the forms
                String firstname = registerFirstname.getText().toString();
                String lastname = registerLastname.getText().toString();
                String email = registerEmail.getText().toString();
                String password = registerPassword.getText().toString();
                String confpass = registerConfPass.getText().toString();
                String phonenumber = registerPhoneNumber.getText().toString();

                //validator for phone number
                String mobileRegex = "^(09|\\+639)\\d{9}$";
                Matcher mobileMatcher;
                Pattern mobilePattern = Pattern.compile(mobileRegex);
                mobileMatcher = mobilePattern.matcher(phonenumber);



                //check if user filled all the fields before sending data to firebase

                if(firstname.isEmpty() ){
                    registerFirstname.setError("First name is Required");
                    return;
                }
                if(lastname.isEmpty()){
                    registerLastname.setError("Last name is Required");
                    return;
                }
                if(email.isEmpty()){
                    registerEmail.setError("Email Address is Required");
                    return;
                }
                if(password.isEmpty()){
                    registerPassword.setError("Password is Required");
                    return;
                }
                if(confpass.isEmpty()){
                    registerConfPass.setError("Confirmation Password is Required");
                    return;
                }
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    registerEmail.setError("Enter a valid email address");
                    return;
                }
                if(!password.equals(confpass)){
                    registerConfPass.setError("Password do not Match");
                    return;
                }
                if(password.length()<6){
                    registerPassword.setError("Password length must atleast 6 characters");
                    return;
                }
                if(phonenumber.isEmpty()){
                    registerPhoneNumber.setError("PhoneNumber is Required");
                }
                if (!mobileMatcher.find()){
                    registerPhoneNumber.setError("Phone No. is not valid");
                    return;
                }

                // if data is validated
                Fauth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            //same but Users
                            Users users = new Users(firstname,lastname,email,phonenumber,0);
                            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    // send admin user to admin dashboard
                                    // registered data of the user using firebase
                                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Register.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });




            }
        });


    }
}