package com.example.loginregisterauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
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


public class MainActivity extends AppCompatActivity {

    TextView verifyMsg;
    Button verifyEmailBtn;
    FirebaseAuth auth;

    private RecyclerView recyclerView;
    private ArrayList<Upload> uploadArrayList;
    private ImageAdapter imageAdapter;

    BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        verifyMsg = findViewById(R.id.verifyEmailmsg);
        verifyEmailBtn = findViewById(R.id.verifyEmailbtn);

        navigationView = findViewById(R.id.bottom_navigation);

        //for recyclerView
        recyclerView = findViewById(R.id.recyclerView);
        //spancount is number of columns
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 1));
        recyclerView.setHasFixedSize(true);

        uploadArrayList = new ArrayList<>();

        clearAll();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("PosterUploads");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clearAll();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Upload uploadModel = new Upload();
                    uploadModel.setName(snapshot.child("name").getValue().toString());
                    uploadModel.setImageUrl(snapshot.child("imageUrl").getValue().toString());

                    uploadArrayList.add(uploadModel);
                }

                imageAdapter = new ImageAdapter(getApplicationContext(),uploadArrayList);
                recyclerView.setAdapter(imageAdapter);
                imageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Error: " + error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        //set Home Selected
        navigationView.setSelectedItemId(R.id.MainHome);

        //perform ItemSelectedListener
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        return true;
                    case R.id.nav_announcement:
                        //validate the user needed to verfied
                        if(!auth.getCurrentUser().isEmailVerified()){
                            Toast.makeText(MainActivity.this, "You need to be verified to see announcements", Toast.LENGTH_SHORT).show();
                        }
                        //if the user is verified process to go the map
                        if(auth.getCurrentUser().isEmailVerified()){
                            startActivity(new Intent(getApplicationContext(), Announcement.class));
                            overridePendingTransition(0, 0);
                            return true;
                        }
                    case R.id.nav_profile:
                        startActivity(new Intent(getApplicationContext(),AccountMenu.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_map:
                        //validate the user needed to verfied
                        if(!auth.getCurrentUser().isEmailVerified()){
                            Toast.makeText(MainActivity.this, "You need to be verified to access the map", Toast.LENGTH_SHORT).show();
                        }
                        //if the user is verified process to go the map
                        if(auth.getCurrentUser().isEmailVerified()){
                            startActivity(new Intent(getApplicationContext(), Map.class));
                            overridePendingTransition(0, 0);
                            return true;
                        }
                    case R.id.nav_building:
                        //validate the user needed to verfied
                        if(!auth.getCurrentUser().isEmailVerified()){
                            Toast.makeText(MainActivity.this, "You need to be verified to see the new establishments", Toast.LENGTH_SHORT).show();
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

        if(!auth.getCurrentUser().isEmailVerified()){
            verifyEmailBtn.setVisibility(View.VISIBLE);
            verifyMsg.setVisibility(View.VISIBLE);
        }



        verifyEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // send verification email
                auth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(MainActivity.this, "Verification Email Sent", Toast.LENGTH_SHORT).show();
                        verifyEmailBtn.setVisibility(View.GONE);
                        verifyMsg.setVisibility(View.GONE);
                    }
                });
            }
        });

    }

    private void clearAll() {
        if (uploadArrayList != null){
            uploadArrayList.clear();

            if (imageAdapter != null){
                imageAdapter.notifyDataSetChanged();
            }
        }

        uploadArrayList = new ArrayList<>();
    }

}