package com.example.loginregisterauth;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class AdminSettingsMenu extends AppCompatActivity {


    FirebaseAuth auth;
    AlertDialog.Builder reset_alert;
    LayoutInflater inflater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_settings_menu);

        Button returnbtn = findViewById(R.id.settingsAdminReturnBtn);

        ListView listviewSettingsMenu = findViewById(R.id.listviewAdminSettingsMenu);

        //add item to the listview
        List<String> list = new ArrayList<>();
        list.add("Reset Password");
        list.add("Update Email");
        list.add("Delete Account");

        //setting ArrayAdapter
        ArrayAdapter arrayAdapter = new ArrayAdapter(AdminSettingsMenu.this, android.R.layout.simple_list_item_1,list);
        listviewSettingsMenu.setAdapter(arrayAdapter);
        //perform ItemClicklistener
        listviewSettingsMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View convertView, int position, long id) {
                if(position==0){
                    //clicked Reset Password
                    startActivity(new Intent(getApplicationContext(),AdminResetPassword.class));
                    finish();
                }else if(position==1){
                    //clicked Update Email
                    startActivity(new Intent(getApplicationContext(),AdminUpdateEmail.class));
                    finish();
                }else if(position==2){
                    //clicked delete account future uses
                    startActivity(new Intent(getApplicationContext(),DeleteAdminAccount.class));
                    finish();
                }
                else{
                }
            }
        });
        returnbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AdminDashboardMenu.class));
                finish();
            }
        });
    }
}