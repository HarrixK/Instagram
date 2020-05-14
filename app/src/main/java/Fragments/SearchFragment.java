package Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.instagram.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import ModelClassUser.StatusAdapterUserClass;
import ModelClassUser.StatusModelUserClass;

public class SearchFragment extends Fragment {
    private Button back, imageDownloadBtn;
    private EditText imagenameET;

    private ImageView imageToDownloadIV;
    private TextView gallery;

    private FirebaseDatabase objectFirebaseDatabase;
    ;
    private LottieAnimationView lottie;

    private RecyclerView rcv;
    private FirebaseFirestore objectFirebaseAuth;

    private StatusAdapterUserClass objectStatusAdapter;

    private androidx.appcompat.widget.SearchView searchView;
    private DatabaseReference ref;

    private ArrayList<StatusModelUserClass> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        try {
            objectFirebaseDatabase = FirebaseDatabase.getInstance();
            rcv = view.findViewById(R.id.RCV);

            rcv.setLayoutManager(new LinearLayoutManager(getActivity()));
            list = new ArrayList<StatusModelUserClass>();

            searchView = view.findViewById(R.id.Search);
            lottie = view.findViewById(R.id.Lottie);

            ref = objectFirebaseDatabase.getReference("Users");
            objectFirebaseAuth = FirebaseFirestore.getInstance();

            rcv = view.findViewById(R.id.RCV);
            lottie = view.findViewById(R.id.Lottie);
        } catch (Exception ex) {
            Toast.makeText(getActivity(), "onCreate: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
//        imageToDownloadIV = view.findViewById(R.id.imageToDownloadIV);
//
//        imageDownloadBtn = view.findViewById(R.id.Download);
//        imagenameET = view.findViewById(R.id.Caption);
//
//        gallery = view.findViewById(R.id.textgallery);
//        objectFirebaseFirestore = FirebaseFirestore.getInstance();
//        lottie = view.findViewById(R.id.Lottie);
//
//        bar = view.findViewById(R.id.ProgressBar);
//        imageDownloadBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                gallery.setVisibility(View.INVISIBLE);
//                Download();
//            }
//        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        lottie.playAnimation();
        lottie.setVisibility(View.VISIBLE);
        if (ref != null) {
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        list = new ArrayList<StatusModelUserClass>();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            list.add(ds.getValue(StatusModelUserClass.class));
                        }
                        StatusAdapterUserClass statusAdapterSearchClass = new StatusAdapterUserClass(list, getActivity());
                        rcv.setAdapter(statusAdapterSearchClass);
                        lottie.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                    lottie.setVisibility(View.INVISIBLE);
                }
            });
        }
        if (searchView != null) {
            searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    lottie.setVisibility(View.INVISIBLE);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    lottie.setVisibility(View.INVISIBLE);
                    return false;
                }
            });
        }
    }

    private void search(String str) {
        ArrayList<StatusModelUserClass> myList = new ArrayList<>();
        for (StatusModelUserClass statusModelUserClass : list) {
            if (statusModelUserClass.getUsername().toLowerCase().contains(str.toLowerCase())) {
                myList.add(statusModelUserClass);
            }
        }
        StatusAdapterUserClass statusAdapterUserClass = new StatusAdapterUserClass(myList, getActivity());
        rcv.setAdapter(statusAdapterUserClass);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

//    private void Download() {
//        try {
//            if (!imagenameET.getText().toString().isEmpty()) {
//                lottie.playAnimation();
//                lottie.setVisibility(View.VISIBLE);
//                imageToDownloadIV.setImageResource(R.drawable.loading);
//                objectFirebaseFirestore.collection("Gallery")
//                        .document(imagenameET.getText().toString())
//                        .get()
//                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                            @Override
//                            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                if (documentSnapshot.exists()) {
//                                    lottie.setVisibility(View.INVISIBLE);
//                                    String url = documentSnapshot.getString("URL");
//                                    Glide.with(getActivity())
//                                            .load(url)
//                                            .into(imageToDownloadIV);
//                                    imagenameET.setText("");
//                                    Toast.makeText(getActivity(), "Image Downloaded", Toast.LENGTH_SHORT).show();
//
//                                } else {
//                                    lottie.setVisibility(View.INVISIBLE);
//                                    Toast.makeText(getActivity(), "No Such File Exists", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        lottie.setVisibility(View.INVISIBLE);
//                        Toast.makeText(getActivity(), "Failed To Retrieve Image" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//            } else {
//                lottie.setVisibility(View.INVISIBLE);
//                Toast.makeText(getActivity(), "Please Enter Name of The Image To Download", Toast.LENGTH_SHORT).show();
//            }
//        } catch (Exception ex) {
//            lottie.setVisibility(View.INVISIBLE);
//            Toast.makeText(getActivity(), "DownloadError: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }

}
