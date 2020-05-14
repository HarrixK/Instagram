package Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.instagram.R;
import com.example.instagram.UpdateProfile;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import ModelClass.StatusModelClass;
import ModelClassProfile.StatusAdapterProfileClass;

public class ProfileFragment extends Fragment {
    private TextView name, mail, Bio;
    private RecyclerView rcv;

    private GoogleSignInClient mGoogleSignInClient;
    private ImageView insta, comment, Image;

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseAuth;

    private FirebaseFirestore objectFirebaseAuth;
    private DatabaseReference reference;

    private StatusAdapterProfileClass objectStatusAdapter;
    private String User;
    private String bio, Uname;

    private Button Profile;
    private FirebaseFirestore objectFirebaseFirestore;

    private LottieAnimationView lottie;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        name = view.findViewById(R.id.name);
        mail = view.findViewById(R.id.mail);

        Bio = view.findViewById(R.id.Bio);
        mAuth = FirebaseAuth.getInstance();

        objectFirebaseFirestore = FirebaseFirestore.getInstance();
        lottie = view.findViewById(R.id.Lottie);

        firebaseAuth = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseAuth != null) {
            User = firebaseAuth.getDisplayName().toString();
            name.setText(firebaseAuth.getDisplayName());
            mail.setText(firebaseAuth.getEmail());
        }

//        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(getActivity());
//        if (signInAccount != null) {
//            name.setText(signInAccount.getDisplayName());
//            mail.setText(signInAccount.getEmail());
//        }
        rcv = view.findViewById(R.id.RCV);
        comment = view.findViewById(R.id.Comment);

        Profile = view.findViewById(R.id.Update);
        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateProfile();
            }
        });

        Image = view.findViewById(R.id.ProfileIV);
        ConnectXML();
        return view;
    }

    private void ConnectXML() {
        try {
            objectFirebaseAuth = FirebaseFirestore.getInstance();
            addValuestoRV();
        } catch (Exception ex) {
            Toast.makeText(getActivity(), "onCreate: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void addValuestoRV() {
        try {
            Query objectQuery = objectFirebaseAuth.collection(User);
            FirestoreRecyclerOptions<StatusModelClass> options =
                    new FirestoreRecyclerOptions.Builder<StatusModelClass>().setQuery(objectQuery, StatusModelClass.class).build();
            objectStatusAdapter = new StatusAdapterProfileClass(options, getActivity());

            rcv.setLayoutManager(new LinearLayoutManager(getActivity()));
            rcv.setAdapter(objectStatusAdapter);
        } catch (Exception ex) {
            Toast.makeText(getActivity(), "AddValuesToRV: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        lottie.playAnimation();
        lottie.setVisibility(View.VISIBLE);
        Download();
        objectStatusAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        objectStatusAdapter.stopListening();
    }

    public void UpdateProfile() {
        try {
            startActivity(new Intent(getActivity(), UpdateProfile.class));
        } catch (Exception e) {
            Toast.makeText(getActivity(), "UpdateError: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            ;
        }
    }

    private void Download() {
        try {
            Image.setImageResource(R.drawable.loading);
            objectFirebaseFirestore.collection(User)
                    .document("ProfileInfo")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String url = documentSnapshot.getString("URL");
                                bio = documentSnapshot.getString("Bio");
                                Uname = documentSnapshot.getString("Username");
                                Glide.with(getActivity())
                                        .load(url)
                                        .into(Image);
                                Bio.setText(bio);
                                mail.setText(Uname);
                                lottie.setVisibility(View.INVISIBLE);
                            } else {
                                lottie.setVisibility(View.INVISIBLE);
                                Toast.makeText(getActivity(), "No Such File Exists", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    lottie.setVisibility(View.INVISIBLE);
                    Toast.makeText(getActivity(), "Failed To Retrieve Image" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (
                Exception ex) {
            lottie.setVisibility(View.INVISIBLE);
            Toast.makeText(getActivity(), "No Profile Image", Toast.LENGTH_SHORT).show();
        }
    }

}
