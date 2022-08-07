package com.example.loginregisterauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DeleteUserAccount extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    //btn
    Button deleteUser, gotosettings,confirmpass;
    //edittext
    EditText confirmpasswordtxt;
    //String user pass
    String userpass;
    //textview
    TextView txtAuthenticate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_user_account);

        //auth
        auth = FirebaseAuth.getInstance();

        //edittext
        confirmpasswordtxt = findViewById(R.id.conPassEdittxt);

        deleteUser = findViewById(R.id.deleteUserBtn);
        gotosettings = findViewById(R.id.returnDeleteBtn);
        confirmpass = findViewById(R.id.confirmPassBtn);
        txtAuthenticate = findViewById(R.id.txtviewAuthenticate);


        //Disabled delete user button until the user is authenticated
        deleteUser.setEnabled(false);

        //auth
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        //checking the account in the firebase database
        if (firebaseUser.equals("")){
            Toast.makeText(DeleteUserAccount.this, "Something went wrong!" + "User Details are not available at the moment", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(DeleteUserAccount.this,SettingsMenu.class));
            finish();
        }else {
            //confirmation to delete the user account
            reAuthenticateUser(firebaseUser);
        }

        //deleting the account
        /* working shits

        deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = auth.getCurrentUser();
                //deleting the current user auth
                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            //deleting the current user realtime database
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                            databaseReference.child(user.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(DeleteUserAccount.this, "Account Deleted.", Toast.LENGTH_SHORT).show();
                                    auth.signOut();
                                    startActivity(new Intent(DeleteUserAccount.this,Login.class));
                                }
                            });
                        }
                        else{
                            Toast.makeText(DeleteUserAccount.this, "Account Deleted Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        */

        //return to SettingsMenu
        gotosettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SettingsMenu.class));
                finish();
            }
        });

    }

    private void showAlertDialog(){
        //setting up the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(DeleteUserAccount.this);
        builder.setTitle("Delete User Information?");
        builder.setMessage("Do you really want to delete your account? This action is irreversible");

        //Delete the user if the user clicks/taps the continue button
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseUser user = auth.getCurrentUser();
                //deleting the current user auth
                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            //deleting the current user realtime database
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                            databaseReference.child(user.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(DeleteUserAccount.this, "Account Deleted.", Toast.LENGTH_SHORT).show();
                                    auth.signOut();
                                    startActivity(new Intent(DeleteUserAccount.this,Login.class));
                                }
                            });
                        }
                        else{
                            Toast.makeText(DeleteUserAccount.this, "Account Deleted Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        //Return the user if user click cancel button
        builder.setNegativeButton("Cancel",null).create().show();

    }

    //Reauthenticate the User
    private void reAuthenticateUser(FirebaseUser firebaseUser) {
        confirmpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userpass = confirmpasswordtxt.getText().toString();

                if (userpass.isEmpty()){
                    confirmpasswordtxt.setError("Password is required");
                    return;
                }else {
                    //Reuthenticate the user now
                    AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(),userpass);
                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {

                            if (task.isSuccessful()){
                                //disable edittext for password
                                confirmpasswordtxt.setEnabled(false);
                                //disable authenticate button
                                confirmpass.setEnabled(false);
                                //enable delete user button
                                deleteUser.setEnabled(true);
                                //set Textview to show user is Authenticated or verified
                                txtAuthenticate.setText("You may now delete your account. Take note that this action is irreversible");

                                //deleting the account
                                deleteUser.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        showAlertDialog();
                                    }
                                });

                            }
                            else {
                                //for error checking
                                try{
                                    throw task.getException();
                                }catch (Exception e){
                                    confirmpasswordtxt.setError(e.getMessage());
                                    return;
                                }
                            }
                        }
                    });
                }
            }
        });
    }
}