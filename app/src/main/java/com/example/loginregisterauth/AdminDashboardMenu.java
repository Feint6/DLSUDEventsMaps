package com.example.loginregisterauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminDashboardMenu extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;

    private String userID;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard_menu);

        ListView listviewAccountMenu = findViewById(R.id.listviewAccountMenu);

        //FirebaseAuth auth;
        TextView Verifytxt;
        Button Verifybtn;
        Verifybtn = findViewById(R.id.VerifyBtn);
        Verifytxt = findViewById(R.id.VerificationTxt);

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
                Toast.makeText(AdminDashboardMenu.this, "Something went wrong!", Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(AdminDashboardMenu.this, "Verification Email Sent", Toast.LENGTH_SHORT).show();
                        Verifybtn.setVisibility(View.GONE);

                    }
                });
            }
        });

        //add item to the listview
        List<String> list = new ArrayList<>();
        list.add("Register New Admin");
        list.add("Account Settings");
        list.add("View Users");
        list.add("View Feedback");
        list.add("Upload Announcement");
        list.add("Upload Posters");
        list.add("Add New Building");
        list.add("Log Out");


        //setting ArrayAdapter
        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1,list);
        listviewAccountMenu.setAdapter(arrayAdapter);

        //perform ItemClicklistener

        listviewAccountMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    //clicked register more admin
                    //validate the user needed to verfied
                    if(!auth.getCurrentUser().isEmailVerified()){
                        Toast.makeText(AdminDashboardMenu.this, "You need to be verified to add admin", Toast.LENGTH_SHORT).show();
                    }
                    //if the user is verified process to add admin
                    if(auth.getCurrentUser().isEmailVerified()){
                        startActivity(new Intent(getApplicationContext(), RegisterAdmin.class));
                        overridePendingTransition(0, 0);
                    }

                }else if(position==1){
                    //clicked account setting
                    startActivity(new Intent(getApplicationContext(),AdminSettingsMenu.class));
                    finish();

                }else if(position==2){
                    //clicked View Users
                    //validate the user needed to verfied
                    if(!auth.getCurrentUser().isEmailVerified()){
                        Toast.makeText(AdminDashboardMenu.this, "You need to be verified to view users", Toast.LENGTH_SHORT).show();
                    }
                    //if the user is verified process to view users
                    if(auth.getCurrentUser().isEmailVerified()){
                        startActivity(new Intent(getApplicationContext(), UsersView.class));
                        overridePendingTransition(0, 0);
                    }
                }
                else if(position==3){
                    //clicked Upload Posters
                    if(!auth.getCurrentUser().isEmailVerified()){
                        Toast.makeText(AdminDashboardMenu.this, "You need to be verified to view feedbacks", Toast.LENGTH_SHORT).show();
                    }
                    //if the user is verified process to upload posters
                    if(auth.getCurrentUser().isEmailVerified()){
                        startActivity(new Intent(getApplicationContext(), FeedbackView.class));
                        overridePendingTransition(0, 0);
                    }
                }
                else if(position==4){
                    //clicked Upload Announcement
                    if(!auth.getCurrentUser().isEmailVerified()){
                        Toast.makeText(AdminDashboardMenu.this, "You need to be verified to upload announcements", Toast.LENGTH_SHORT).show();
                    }
                    //if the user is verified process to upload announcements
                    if(auth.getCurrentUser().isEmailVerified()){
                        startActivity(new Intent(getApplicationContext(), AnnouncementInput.class));
                        overridePendingTransition(0, 0);
                    }
                }
                else if(position==5){
                    //clicked Upload Posters
                    if(!auth.getCurrentUser().isEmailVerified()){
                        Toast.makeText(AdminDashboardMenu.this, "You need to be verified to view users", Toast.LENGTH_SHORT).show();
                    }
                    //if the user is verified process to upload posters
                    if(auth.getCurrentUser().isEmailVerified()){
                        startActivity(new Intent(getApplicationContext(), AdminUploadPosters.class));
                        overridePendingTransition(0, 0);
                    }
                }
                else if(position==6){
                    //clicked Upload Posters
                    if(!auth.getCurrentUser().isEmailVerified()){
                        Toast.makeText(AdminDashboardMenu.this, "You need to be verified to add buildings", Toast.LENGTH_SHORT).show();
                    }
                    //if the user is verified process to upload posters
                    if(auth.getCurrentUser().isEmailVerified()){
                        startActivity(new Intent(getApplicationContext(), NewBuildingAdmin.class));
                        overridePendingTransition(0, 0);
                    }
                }
                else if(position==7){
                    //clicked logout for admin
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(),Login.class));
                    finish();
                }



            }
        });

    }
}