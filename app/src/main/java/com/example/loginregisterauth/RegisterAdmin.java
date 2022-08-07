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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterAdmin extends AppCompatActivity {

    Button adminUserBtn,gotoLoginAdmin;

    FirebaseAuth Fauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_admin);

        //Edit texts forms
        final EditText adminFname = findViewById(R.id.AdminFname);
        final EditText adminLname = findViewById(R.id.AdminLname);
        final EditText adminEmail = findViewById(R.id.AdminEmail);
        final EditText adminPass = findViewById(R.id.AdminPass);
        final EditText adminConfPass = findViewById(R.id.AdminConfPass);
        final EditText adminPhoneNo = findViewById(R.id.AdminPhoneNo);

        // Buttons
        adminUserBtn = findViewById(R.id.AdminUserBtn);
        gotoLoginAdmin = findViewById(R.id.gotoLoginAdmin);

        //Authentication
        Fauth = FirebaseAuth.getInstance();




        gotoLoginAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AdminDashboardMenu.class));
                finish();
            }
        });

        adminUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // extract data from the forms
                String firstname = adminFname.getText().toString();
                String lastname = adminLname.getText().toString();
                String email = adminEmail.getText().toString();
                String password = adminPass.getText().toString();
                String confpass = adminConfPass.getText().toString();
                String phonenumber = adminPhoneNo.getText().toString();

                //validator for phone number
                String mobileRegex = "^(09|\\+639)\\d{9}$";
                Matcher mobileMatcher;
                Pattern mobilePattern = Pattern.compile(mobileRegex);
                mobileMatcher = mobilePattern.matcher(phonenumber);



                //check if user filled all the fields before sending data to firebase

                if(firstname.isEmpty() ){
                    adminFname.setError("First name is Required");
                    return;
                }
                if(lastname.isEmpty()){
                    adminLname.setError("Last name is Required");
                    return;
                }
                if(email.isEmpty()){
                    adminEmail.setError("Email Address is Required");
                    return;
                }
                if(password.isEmpty()){
                    adminPass.setError("Password is Required");
                    return;
                }
                if(confpass.isEmpty()){
                    adminConfPass.setError("Confirmation Password is Required");
                    return;
                }
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    adminEmail.setError("Enter a valid email address");
                    return;
                }
                if(!password.equals(confpass)){
                    adminConfPass.setError("Password do not Match");
                    return;
                }
                if(password.length()<6){
                    adminPass.setError("Password length must atleast 6 characters");
                    return;
                }
                if(phonenumber.isEmpty()){
                    adminPhoneNo.setError("PhoneNumber is Required");
                }
                if (!mobileMatcher.find()){
                    adminPhoneNo.setError("Phone No. is not valid");
                    return;
                }

                // if data is validated
                Fauth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            //same but Users
                            Users users = new Users(firstname,lastname,email,phonenumber,1);
                            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    // send admin user to logout to prevent bugs
                                    // registered data of the user using firebase
                                    Toast.makeText(RegisterAdmin.this, "Registered admin has been successfully created login again to try the credentials", Toast.LENGTH_SHORT).show();
                                    FirebaseAuth.getInstance().signOut();
                                    startActivity(new Intent(getApplicationContext(),Login.class));
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegisterAdmin.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterAdmin.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });




            }
        });

    }
}