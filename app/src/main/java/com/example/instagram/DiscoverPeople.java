package com.example.instagram;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import ModelClassDM.StatusAdapterDMClass;
import ModelClassDiscover.StatusAdapterDiscoverClass;
import ModelClassDiscover.StatusModelDiscoverClass;

public class DiscoverPeople extends AppCompatActivity {

    private TextView Username;
    private RecyclerView rcv;

    private GoogleSignInClient mGoogleSignInClient;
    private ImageView Image;

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseAuth;

    private FirebaseFirestore objectFirebaseAuth;
    private DatabaseReference reference;

    private StatusAdapterDiscoverClass objectStatusAdapter;
    private String User;

    private FirebaseFirestore objectFirebaseFirestore;
    private LottieAnimationView lottie;

    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover_people);

        lottie = findViewById(R.id.Lottie);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DiscoverPeople.this, MainPage.class);
                startActivity(intent);
            }
        });
        ConnectXML();
    }

    private void ConnectXML() {
        try {
            objectFirebaseAuth = FirebaseFirestore.getInstance();
            rcv = findViewById(R.id.RCV);

            addValuestoRV();
        } catch (Exception ex) {
            Toast.makeText(this, "onCreate: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void addValuestoRV() {
        try {
            Query objectQuery = objectFirebaseAuth.collection("Users");
            FirestoreRecyclerOptions<StatusModelDiscoverClass> options =
                    new FirestoreRecyclerOptions.Builder<StatusModelDiscoverClass>().setQuery(objectQuery, StatusModelDiscoverClass.class).build();
            objectStatusAdapter = new StatusAdapterDiscoverClass(options);

            rcv.setLayoutManager(new LinearLayoutManager(this));
            rcv.setAdapter(objectStatusAdapter);
            lottie.setVisibility(View.INVISIBLE);
        } catch (Exception ex) {
            lottie.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "AddValuesToRV: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        lottie.playAnimation();
        lottie.setVisibility(View.VISIBLE);
        objectStatusAdapter.startListening();
        lottie.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        objectStatusAdapter.stopListening();
    }
}
