package com.example.instagram;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdminSignup extends AppCompatActivity {
    private Button signup;
    private TextView Signin;

    private EditText emailET, paswwordET, username;

    private FirebaseAuth objectFirebaseAuth;
    private static final String CollectionName = "Admin";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    private FirebaseFirestore objectFirebaseFirestore;
    private DatabaseReference reference;

    private LottieAnimationView lottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_signup);

        ConnectWithXML();
    }

    private void ConnectWithXML() {
        try {
            signup = findViewById(R.id.Signup);
            Signin = findViewById(R.id.SignTV);

            emailET = findViewById(R.id.Username);
            paswwordET = findViewById(R.id.Password);

            username = findViewById(R.id.Uname);
            lottie = findViewById(R.id.Lottie);

            Signin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Change();
                }
            });
            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lottie.playAnimation();
                    lottie.setVisibility(View.VISIBLE);
                    checkuser();
                }
            });

            mAuth = FirebaseAuth.getInstance();
            objectFirebaseAuth = FirebaseAuth.getInstance();

            objectFirebaseFirestore = FirebaseFirestore.getInstance();

        } catch (Exception ex) {
            Toast.makeText(this, "ConnectWithXML: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void Change() {
        startActivity(new Intent(AdminSignup.this, AdminLogin.class));
        finish();
    }

    private void checkuser() {
        try {
            if (!emailET.getText().toString().isEmpty()) {
                if (objectFirebaseAuth != null) {
                    signup.setEnabled(false);
                    objectFirebaseAuth.fetchSignInMethodsForEmail(emailET.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                @Override
                                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                    boolean check = task.getResult().getSignInMethods().isEmpty();
                                    if (!check) {
                                        signup.setEnabled(true);
                                        lottie.setVisibility(View.INVISIBLE);
                                        Toast.makeText(AdminSignup.this, "User Already Exists", Toast.LENGTH_SHORT).show();

                                    } else if (check) {
                                        lottie.setVisibility(View.INVISIBLE);

                                        signup.setEnabled(true);
                                        Signup();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            lottie.setVisibility(View.INVISIBLE);

                            signup.setEnabled(true);
                            Toast.makeText(AdminSignup.this, "Fails To Check If User Exists" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                emailET.requestFocus();
                lottie.setVisibility(View.INVISIBLE);

                signup.setEnabled(true);
                Toast.makeText(this, "Email and Password is Empty", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception ex) {
            lottie.setVisibility(View.INVISIBLE);

            signup.setEnabled(true);
            Toast.makeText(this, "Check User Error" + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void Signup() {
        try {
            if (!emailET.getText().toString().isEmpty()
                    &&
                    !paswwordET.getText().toString().isEmpty()) {
                if (objectFirebaseAuth != null) {
                    signup.setEnabled(false);
                    mAuth.createUserWithEmailAndPassword(emailET.getText().toString(), paswwordET.getText().toString())
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Map<String, Object> objectMap = new HashMap<>();
                                        objectMap.put("Username", username.getText().toString());
                                        objectMap.put("Email", emailET.getText().toString());
                                        objectFirebaseFirestore.collection(CollectionName)
                                                .document(username.getText().toString())
                                                .set(objectMap)
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(AdminSignup.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        lottie.setVisibility(View.INVISIBLE);
                                                    }
                                                });
                                        lottie.setVisibility(View.INVISIBLE);
                                        Toast.makeText(AdminSignup.this, "Sucessfully created an Admin", Toast.LENGTH_SHORT).show();
                                    } else {
                                        lottie.setVisibility(View.INVISIBLE);
                                        Toast.makeText(AdminSignup.this, "Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            } else if (emailET.getText().toString().isEmpty()) {
                signup.setEnabled(true);
                lottie.setVisibility(View.INVISIBLE);

                emailET.requestFocus();
                Toast.makeText(this, "Please Enter The Email", Toast.LENGTH_SHORT).show();
            } else if (paswwordET.getText().toString().isEmpty()) {
                signup.setEnabled(true);
                lottie.setVisibility(View.INVISIBLE);

                paswwordET.requestFocus();
                Toast.makeText(this, "Please Enter The Password", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception ex) {
            lottie.setVisibility(View.INVISIBLE);
            signup.setEnabled(true);

            emailET.requestFocus();
            Toast.makeText(this, "Signup Error" + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
