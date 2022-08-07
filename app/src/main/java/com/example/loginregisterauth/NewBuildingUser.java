package com.example.loginregisterauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NewBuildingUser extends AppCompatActivity {

    private RecyclerView recyclerViewb;
    private ArrayList<NewBuildingModel> buildingModelArrayList;
    private RecyclerBuildingAdapter buildingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_building_user);

        recyclerViewb = findViewById(R.id.brecyclerView);
        recyclerViewb.setLayoutManager(new GridLayoutManager(NewBuildingUser.this, 1));
        recyclerViewb.setHasFixedSize(true);

        buildingModelArrayList = new ArrayList<>();

        clearAll();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("NewBuildings");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clearAll();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    NewBuildingModel buildingModel = new NewBuildingModel();
                    buildingModel.setbName(snapshot.child("bName").getValue().toString());
                    buildingModel.setcDesc(snapshot.child("cDesc").getValue().toString());
                    buildingModel.setaImageUrl(snapshot.child("aImageUrl").getValue().toString());

                    buildingModelArrayList.add(buildingModel);
                }

                buildingAdapter = new RecyclerBuildingAdapter(getApplicationContext(),buildingModelArrayList);
                recyclerViewb.setAdapter(buildingAdapter);
                buildingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(NewBuildingUser.this, "Error: " + error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearAll() {
        if (buildingModelArrayList != null){
            buildingModelArrayList.clear();

            if (buildingAdapter != null){
                buildingAdapter.notifyDataSetChanged();
            }
        }

        buildingModelArrayList = new ArrayList<>();
    }
}