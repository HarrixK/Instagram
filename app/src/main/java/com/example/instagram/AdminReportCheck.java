package com.example.instagram;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import ModelClassAdmin.StatusAdapterAdminClass;
import ModelClassAdmin.StatusModelAdminClass;

public class AdminReportCheck extends AppCompatActivity {

    private Button back;
    private RecyclerView rcv;

    private StatusAdapterAdminClass objectStatusAdapter;
    private FirebaseFirestore objectFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_report_check);

        ConnectWithXML();
    }

    private void ConnectWithXML() {
        try {
            rcv = findViewById(R.id.RCV);
            back = findViewById(R.id.back);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(AdminReportCheck.this, AdminMainPage.class));
                    finish();
                }
            });

            objectFirebaseAuth = FirebaseFirestore.getInstance();
            addValuestoRV();
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void addValuestoRV() {
        try {
            Query objectQuery = objectFirebaseAuth.collection("Report");
            FirestoreRecyclerOptions<StatusModelAdminClass> options =
                    new FirestoreRecyclerOptions.Builder<StatusModelAdminClass>().setQuery(objectQuery, StatusModelAdminClass.class).build();
            objectStatusAdapter = new StatusAdapterAdminClass(options, AdminReportCheck.this);

            rcv.setLayoutManager(new LinearLayoutManager(AdminReportCheck.this));
            rcv.setAdapter(objectStatusAdapter);
        } catch (Exception ex) {
            Toast.makeText(this, "AddValuesToRV: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        objectStatusAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        objectStatusAdapter.stopListening();
    }
}
