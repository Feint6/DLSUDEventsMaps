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

public class AdminResetPassword extends AppCompatActivity {
    EditText AdminuserPassword,AdminuserConfPassword;
    Button savePasswordBtn,returnBtn;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_reset_password);

        //new password
        AdminuserPassword = findViewById(R.id.newAdminUserPassword);
        AdminuserConfPassword = findViewById(R.id.newAdminConfirmPass);

        user = FirebaseAuth.getInstance().getCurrentUser();

        returnBtn = findViewById(R.id.Adminreturnbtn);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AdminSettingsMenu.class));
                finish();
            }
        });

        savePasswordBtn = findViewById(R.id.AdminSavebtn);
        savePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newpassword = AdminuserPassword.getText().toString();
                String confirmpass = AdminuserConfPassword.getText().toString();

                // extract / validate

                if (newpassword.isEmpty()){
                    AdminuserPassword.setError("Required Field");
                    return;
                }
                if (confirmpass.isEmpty()){
                    AdminuserConfPassword.setError("Required Field");
                    return;
                }



                if(!newpassword.equals(confirmpass)){
                    AdminuserConfPassword.setError("Password does not match");
                    return;
                }
                if(newpassword.length()<6){
                    AdminuserPassword.setError("Password length must be at least 6 characters");
                    return;
                }

                user.updatePassword(AdminuserPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //successfully updated your password
                        Toast.makeText(AdminResetPassword.this, "Password Updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),AdminSettingsMenu.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Change password failed, show reason
                        Toast.makeText(AdminResetPassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });




            }

        });
    }
}