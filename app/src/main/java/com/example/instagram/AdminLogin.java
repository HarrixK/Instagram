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
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AdminLogin extends AppCompatActivity {
    private TextView Signup;
    private EditText emailET, paswwordET;

    private Button signin, googlesignin;
    private GoogleSignInClient mGoogleSignInClient;

    private FirebaseAuth mAuth;
    private FirebaseAuth objectFirebaseAuth;
    private LottieAnimationView lottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        ConnectWithXML();
    }

    private void ConnectWithXML() {
        try {
            Signup = findViewById(R.id.SignupTextView);
            emailET = findViewById(R.id.Username);

            signin = findViewById(R.id.Login);
            paswwordET = findViewById(R.id.Password);

            googlesignin = findViewById(R.id.LoginGoogle);

            Signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Change();
                }
            });
            mAuth = FirebaseAuth.getInstance();

            objectFirebaseAuth = FirebaseAuth.getInstance();
            signin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lottie.playAnimation();
                    lottie.setVisibility(View.VISIBLE);
                    signinuser();
                }
            });

            lottie = findViewById(R.id.Lottie);


        } catch (Exception ex) {
            Toast.makeText(this, "ConnectWithXML: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void Change() {
        startActivity(new Intent(AdminLogin.this, AdminSignup.class));
        finish();
    }

    public void signinuser() {
        try {
            if (!emailET.getText().toString().isEmpty() && !paswwordET.getText().toString().isEmpty()) {
                if (objectFirebaseAuth.getCurrentUser() != null) {
                    objectFirebaseAuth.signOut();
                    signin.setEnabled(false);

                    Toast.makeText(this, "User Logged Out Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    objectFirebaseAuth.signInWithEmailAndPassword(emailET.getText().toString(),
                            paswwordET.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    signin.setEnabled(true);

                                    Toast.makeText(AdminLogin.this, "User Logged In", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(AdminLogin.this, AdminMainPage.class));
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            signin.setEnabled(true);
                            emailET.requestFocus();

                            lottie.setVisibility(View.INVISIBLE);
                            Toast.makeText(AdminLogin.this, "Fails To Sig-in User: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else if (emailET.getText().toString().isEmpty()) {
                signin.setEnabled(true);
                emailET.requestFocus();

                lottie.setVisibility(View.INVISIBLE);
                Toast.makeText(this, "Please Enter The Email", Toast.LENGTH_SHORT).show();
            } else if (paswwordET.getText().toString().isEmpty()) {
                signin.setEnabled(true);
                paswwordET.requestFocus();

                lottie.setVisibility(View.INVISIBLE);
                Toast.makeText(this, "Please Enter The Password", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {

            signin.setEnabled(true);
            emailET.requestFocus();

            lottie.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Logging In Error" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
