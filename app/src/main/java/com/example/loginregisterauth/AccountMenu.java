package com.example.loginregisterauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AccountMenu extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;

    private String userID;

    FirebaseAuth auth;
    BottomNavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_menu);

        //FirebaseAuth auth;
        TextView Verifytxt;
        Button Verifybtn;

        navigationView = findViewById(R.id.bottom_navigation);
        Verifybtn = findViewById(R.id.VerifyBtn);
        Verifytxt = findViewById(R.id.VerificationTxt);

        ListView listviewAccountMenu = findViewById(R.id.listviewAccountMenu);


        //initialize value
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();


        final TextView emailtxt = (TextView) findViewById(R.id.txtEmail);
        final TextView firstnametxt = (TextView) findViewById(R.id.txtFirstName);
        final TextView lastnametxt = (TextView) findViewById(R.id.txtLastName);
        final TextView phonenumbertxt = (TextView) findViewById(R.id.txtPhoneNo);



        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users userProfile = snapshot.getValue(Users.class);

                if (userProfile !=null){
                    String email = userProfile.email;
                    String firstname = userProfile.firstname;
                    String lastname = userProfile.lastname;
                    String PhoneNo = userProfile.phonenumber;

                    //retrieving data from firebase
                    emailtxt.setText(email);
                    firstnametxt.setText(firstname);
                    lastnametxt.setText(lastname);
                    phonenumbertxt.setText(PhoneNo);
                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AccountMenu.this, "Something went wrong!", Toast.LENGTH_SHORT).show();

            }
        });

       //checking if the user is not verified yet
        if(!auth.getCurrentUser().isEmailVerified()){
            Verifybtn.setVisibility(View.VISIBLE);
            Verifytxt.setText("Not Verified");
        }
        //checking if the user is verified
        else if (auth.getCurrentUser().isEmailVerified()){
            Verifytxt.setText("Verified User");

        }



        Verifybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // send verification email
                auth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AccountMenu.this, "Verification Email Sent", Toast.LENGTH_SHORT).show();
                        Verifybtn.setVisibility(View.GONE);

                    }
                });
            }
        });


        //add item to the listview
        List<String> list = new ArrayList<>();
        list.add("Settings");
        list.add("Feedback");
        list.add("Logout");


        //setting ArrayAdapter
        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1,list);
        listviewAccountMenu.setAdapter(arrayAdapter);

        //perform ItemClicklistener

        listviewAccountMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    //clicked Settings
                    startActivity(new Intent(getApplicationContext(),SettingsMenu.class));
                    finish();
                }else if(position==1){
                    //clicked Feedback
                    //validate the user needed to verified
                    if(!auth.getCurrentUser().isEmailVerified()){
                        Toast.makeText(AccountMenu.this, "You need to be verified to give feedback", Toast.LENGTH_SHORT).show();
                    }
                    //if the user is verified process to go feedback
                    if(auth.getCurrentUser().isEmailVerified()){
                        startActivity(new Intent(getApplicationContext(), Feedback.class));
                        finish();
                    }
                }
                else if(position==2){
                    //clicked logout
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(),Login.class));
                    finish();
                }else{

                }
            }
        });


        //set Home Selected
        navigationView.setSelectedItemId(R.id.nav_profile);

        //perform ItemSelectedListener
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_profile:
                        return true;
                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_announcement:
                        startActivity(new Intent(getApplicationContext(), Announcement.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.nav_map:
                        //validate the user needed to verfied
                        if(!auth.getCurrentUser().isEmailVerified()){
                            Toast.makeText(AccountMenu.this, "You need to be verified to access the map", Toast.LENGTH_SHORT).show();
                        }
                        //if the user is verified process to go the map
                        if(auth.getCurrentUser().isEmailVerified()){
                            startActivity(new Intent(getApplicationContext(), Map.class));
                            overridePendingTransition(0, 0);
                            return true;
                        }
                    case R.id.nav_building:
                        //validate the user needed to verified
                        if(!auth.getCurrentUser().isEmailVerified()){
                            Toast.makeText(AccountMenu.this, "You need to be verified to see new establishments", Toast.LENGTH_SHORT).show();
                        }
                        //if the user is verified process to go the map
                        if(auth.getCurrentUser().isEmailVerified()){
                            startActivity(new Intent(getApplicationContext(), NewBuildingUser.class));
                            overridePendingTransition(0, 0);
                            return true;
                        }

                }

                return false;
            }
        });

    }
}