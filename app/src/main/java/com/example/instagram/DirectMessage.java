package com.example.instagram;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ModelClassSearch.StatusAdapterSearchClass;
import ModelClassSearch.StatusModelSearchClass;

public class DirectMessage extends AppCompatActivity {
    private RecyclerView rcv;
    private FirebaseDatabase objectFirebaseDatabase;

    private LottieAnimationView lottie;
    private Button back;

    private androidx.appcompat.widget.SearchView searchView;
    private DatabaseReference ref;

    private ArrayList<StatusModelSearchClass> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direct_message);

        ConnectXML();
    }

    private void ConnectXML() {
        try {
            objectFirebaseDatabase = FirebaseDatabase.getInstance();
            rcv = findViewById(R.id.RCV);

            rcv.setLayoutManager(new LinearLayoutManager(this));

            list = new ArrayList<StatusModelSearchClass>();

            searchView = findViewById(R.id.Search);
            lottie = findViewById(R.id.Lottie);

            back = findViewById(R.id.back);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DirectMessage.this, MainPage.class);
                    startActivity(intent);
                }
            });
            ref = objectFirebaseDatabase.getReference("Users");
        } catch (Exception ex) {
            Toast.makeText(this, "onCreate: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (ref != null) {
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        list = new ArrayList<StatusModelSearchClass>();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            list.add(ds.getValue(StatusModelSearchClass.class));
                        }
                        StatusAdapterSearchClass statusAdapterSearchClass = new StatusAdapterSearchClass(list);
                        rcv.setAdapter(statusAdapterSearchClass);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(DirectMessage.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (searchView != null) {
            searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return false;
                }
            });
        }
    }

    private void search(String str) {
        ArrayList<StatusModelSearchClass> myList = new ArrayList<>();
        for (StatusModelSearchClass statusModelSearchClass : list) {
            if (statusModelSearchClass.getUsername().toLowerCase().contains(str.toLowerCase())) {
                myList.add(statusModelSearchClass);
            }
        }
        StatusAdapterSearchClass statusAdapterSearchClass = new StatusAdapterSearchClass(myList);
        rcv.setAdapter(statusAdapterSearchClass);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
