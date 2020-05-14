package Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.example.instagram.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.os.Environment.getExternalStoragePublicDirectory;

public class CameraFragment extends Fragment {
    private ImageView imageToUploadIV;
    private EditText imageNameET;

    private TextView gallery;
    private ProgressBar bar;

    private String currentPhotoPath;
    private LottieAnimationView lottie;

    private Button imageUploadingBtn, quit, UploadImg;
    private static final int REQUEST_CODE = 124;

    private static final int CAMERA_CODE = 124;
    private Uri imageDataInUriForm;
    private StorageReference objectStorageReference;

    private FirebaseFirestore objectFirebaseFirestore;
    private boolean isImageSelected = false;
    private String CurrentPhotoPath;

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseAuth;
    private String User;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        imageToUploadIV = view.findViewById(R.id.imageToUploadIV);
        imageUploadingBtn = view.findViewById(R.id.Upload);

        imageNameET = view.findViewById(R.id.Caption);
        gallery = view.findViewById(R.id.textgallery);

        bar = view.findViewById(R.id.ProgressBar);
        objectFirebaseFirestore = FirebaseFirestore.getInstance();
        lottie = view.findViewById(R.id.Lottie);

        objectStorageReference = FirebaseStorage.getInstance().getReference("Gallery");
        imageUploadingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lottie.playAnimation();
                lottie.setVisibility(View.VISIBLE);
                askCameraPermission();
                hide();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        firebaseAuth = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseAuth != null) {
            User = firebaseAuth.getDisplayName().toString();
        }


        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (signInAccount != null) {
            User = signInAccount.getDisplayName().toString();
        }
        return view;
    }

    private void hide() {
        gallery.setVisibility(View.INVISIBLE);
    }

    private void askCameraPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_CODE);
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_CODE);
        } else {
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_CODE) {
            if (grantResults.length < 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(getActivity(), "Camera Persmission required to use Camera", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == getActivity().RESULT_OK) {
                File f = new File(currentPhotoPath);
                imageToUploadIV.setImageURI(Uri.fromFile(f));

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(f);

                mediaScanIntent.setData(contentUri);
                getActivity().sendBroadcast(mediaScanIntent);

                UploadImageToFireBase(f.getName(), contentUri);

            } else if (requestCode != REQUEST_CODE) {
                Toast.makeText(getActivity(), "Request code doesn't match", Toast.LENGTH_SHORT).show();
            } else if (resultCode != RESULT_OK) {
                Toast.makeText(getActivity(), "Fails to get image", Toast.LENGTH_SHORT).show();
            } else if (data == null) {
                Toast.makeText(getActivity(), "No Image Was Selected", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "onActivityResult:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void UploadImageToFireBase(String name, final Uri contentUri) {
        final StorageReference image = objectStorageReference.child(name);
        image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                final String Caption = imageNameET.getText().toString();
                image.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            String url = contentUri.toString();
                            Map<String, Object> objectMap = new HashMap<>();
                            objectMap.put("URL", url);
                            objectMap.put("Comments", Caption);
                            objectMap.put("Server Time Stamp", FieldValue.serverTimestamp());
                            objectMap.put("By", User);
                            objectFirebaseFirestore.collection("Gallery")
                                    .document(imageNameET.getText().toString())
                                    .set(objectMap)
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getActivity(), "Fails To Upload Image URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
//                                            lottie.setVisibility(View.INVISIBLE);
//                                            Toast.makeText(getActivity(), "Image Successfully Uploaded: ", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            objectFirebaseFirestore.collection(User)
                                    .document(imageNameET.getText().toString())
                                    .set(objectMap)
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getActivity(), "Fails To Upload Image URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            lottie.setVisibility(View.INVISIBLE);
                                            Toast.makeText(getActivity(), "Image Successfully Uploaded: ", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                });
                Toast.makeText(getActivity(), "Please wait Uploading", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                lottie.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity(), "Failed To Uplaod", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = imageNameET.getText().toString();
//        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (Exception ex) {
                Toast.makeText(getActivity(), "dispatchTakePictureIntent: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                try {
                    Uri photoURI = FileProvider.getUriForFile(getActivity(),
                            "com.example.android.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, CAMERA_CODE);
                } catch (Exception ex) {
                    Toast.makeText(getActivity(), "dispatchTakePictureIntent: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
