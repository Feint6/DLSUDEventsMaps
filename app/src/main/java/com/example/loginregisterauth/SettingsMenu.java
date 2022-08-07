package com.example.loginregisterauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class SettingsMenu extends AppCompatActivity {

    FirebaseAuth auth;
    AlertDialog.Builder reset_alert;
    LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_menu);

        Button returnbtn = findViewById(R.id.settingsReturnBtn);

        ListView listviewSettingsMenu = findViewById(R.id.listviewSettingsMenu);

        //add item to the listview
        List<String> list = new ArrayList<>();
        list.add("Reset Password");
        list.add("Update Email");
        list.add("Delete Account");

        //setting ArrayAdapter
        ArrayAdapter arrayAdapter = new ArrayAdapter(SettingsMenu.this, android.R.layout.simple_list_item_1,list);
        listviewSettingsMenu.setAdapter(arrayAdapter);
        //perform ItemClicklistener
        listviewSettingsMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View convertView, int position, long id) {
                if(position==0){
                    //clicked Reset Password
                    startActivity(new Intent(getApplicationContext(),ResetPassword.class));
                    finish();
                }else if(position==1){
                    //clicked Update Email
                    startActivity(new Intent(getApplicationContext(),UpdateEmail.class));
                    finish();
                }else if(position==2){
                    //clicked delete account future uses
                    startActivity(new Intent(getApplicationContext(),DeleteUserAccount.class));
                    finish();
                  /*(  reset_alert.setTitle("Delete Account Permanently")
                            .setMessage("Are you sure?")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    FirebaseUser user = auth.getCurrentUser();
                                    user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(SettingsMenu.this, "Account Deleted.", Toast.LENGTH_SHORT).show();
                                            auth.signOut();
                                            startActivity(new Intent(getApplicationContext(),Login.class));
                                        }
                                    });

                                }
                            }).setNegativeButton("Cancel",null)
                            .create().show();
                            */
                }

                else{

                }
            }
        });


        returnbtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getApplicationContext(),AccountMenu.class));
            finish();
        }
        });
    }
}