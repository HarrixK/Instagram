package com.example.instagram;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import ModelClassDM.StatusAdapterDMClass;

public class DM_Chat extends AppCompatActivity {

    private Button Add;
    private EditText Name;
    String name;

    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;

    private ArrayList<String> list_of_rooms = new ArrayList<>();
    private StatusAdapterDMClass objectStatusAdapter;

    private FirebaseFirestore objectFirebaseAuth;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("Users").getRoot();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d_m__chat);
        try {
            Name = findViewById(R.id.Search);

            listView = findViewById(R.id.ListViewChat);
            arrayAdapter = new ArrayAdapter<String>(this, R.layout.single_row_dm, list_of_rooms);

            listView.setAdapter(arrayAdapter);

            root.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Set<String> set = new HashSet<String>();
                    Iterator i = dataSnapshot.getChildren().iterator();
                    while (i.hasNext()) {
                        set.add(((DataSnapshot) i.next()).getKey());
                    }
                    list_of_rooms.clear();
                    list_of_rooms.addAll(set);

                    arrayAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
