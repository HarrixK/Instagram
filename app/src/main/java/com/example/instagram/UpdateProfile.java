package com.example.instagram;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class UpdateProfile extends AppCompatActivity {
    private ImageView ProfileIV;
    private EditText Username, DisplayName, Bio;

    private Button Update, back;
    private static final int REQUEST_CODE = 124;

    private Uri imageDataInUriForm;
    private StorageReference objectStorageReference;

    private FirebaseFirestore objectFirebaseFirestore;
    private boolean isImageSelected = false;
    private GoogleSignInClient mGoogleSignInClient;

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseAuth;

    public String User;
    private LottieAnimationView lottie;

    private String currentDate;
    private Object ServerTimestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        try {
            ProfileIV = findViewById(R.id.avatarIV);
            Username = findViewById(R.id.Uname);

            DisplayName = findViewById(R.id.Username);
            Bio = findViewById(R.id.Bio);

            Update = findViewById(R.id.Update);
            lottie = findViewById(R.id.Lottie);

            firebaseAuth = FirebaseAuth.getInstance().getCurrentUser();
            objectFirebaseFirestore = FirebaseFirestore.getInstance();

            if (firebaseAuth != null) {
                User = firebaseAuth.getDisplayName().toString();
            }
            objectStorageReference = FirebaseStorage.getInstance().getReference(User);

            back = findViewById(R.id.back);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(UpdateProfile.this, MainPage.class);
                    startActivity(intent);
                }
            });

            ProfileIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openGallery();
                }
            });
            Update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lottie.playAnimation();
                    lottie.setVisibility(View.VISIBLE);
                    uploadOurImage();
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void openGallery() {
        try {
            Intent objectIntent = new Intent(); //Step 1:create the object of intent
            objectIntent.setAction(Intent.ACTION_GET_CONTENT); //Step 2: You want to get some data

            objectIntent.setType("image/*");//Step 3: Images of all type
            startActivityForResult(objectIntent, REQUEST_CODE);

        } catch (Exception e) {
            Toast.makeText(this, "openGallery:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
                imageDataInUriForm = data.getData();
                Bitmap objectBitmap;

                objectBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageDataInUriForm);
                ProfileIV.setImageBitmap(objectBitmap);

                isImageSelected = true;
            } else if (requestCode != REQUEST_CODE) {
                Toast.makeText(this, "Request code doesn't match", Toast.LENGTH_SHORT).show();
            } else if (resultCode != RESULT_OK) {
                Toast.makeText(this, "Fails to get image", Toast.LENGTH_SHORT).show();
            } else if (data == null) {
                Toast.makeText(this, "No Image Was Selected", Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e) {
            Toast.makeText(this, "onActivityResult:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadOurImage() {
        try {
            if (imageDataInUriForm != null && !Username.getText().toString().isEmpty()
                    && isImageSelected) {

                String imageName = Username.getText().toString() + "." + getExtension(imageDataInUriForm);
                final StorageReference actualImageRef = objectStorageReference.child(imageName);

                UploadTask uploadTask = actualImageRef.putFile(imageDataInUriForm);
                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {

                            throw task.getException();
                        }
                        return actualImageRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            String url = task.getResult().toString();
                            try {
                                mAuth = FirebaseAuth.getInstance();
                                FirebaseDatabase database = FirebaseDatabase.getInstance();

                                DatabaseReference myRef = database.getReference("Users").child(User);
                                HashMap<String, Object> hashMap = new HashMap<>();

                                hashMap.put("Username", Username.getText().toString());
                                hashMap.put("DisplayName", DisplayName.getText().toString());

                                hashMap.put("Bio", Bio.getText().toString());
                                hashMap.put("URL", url);
                                myRef.setValue(hashMap);
                            } catch (Exception e) {
                                Toast.makeText(UpdateProfile.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            Map<String, Object> objectMap = new HashMap<>();
                            objectMap.put("URL", url);
                            objectMap.put("Username", Username.getText().toString());
                            objectMap.put("Comments", "Profile Picture");
                            objectMap.put("DisplayName", DisplayName.getText().toString());
                            objectMap.put("Bio", Bio.getText().toString());
                            objectMap.put("Server Time Stamp", FieldValue.serverTimestamp());
                            objectMap.put("By", User);
                            objectFirebaseFirestore.collection(User)
                                    .document("ProfileInfo")
                                    .set(objectMap)
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(UpdateProfile.this, "Fails To Upload Image URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(UpdateProfile.this, "Uploading", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            objectFirebaseFirestore.collection("Users")
                                    .document(User)
                                    .set(objectMap)
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            lottie.setVisibility(View.INVISIBLE);
                                            Toast.makeText(UpdateProfile.this, "Fails To Upload Image URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            lottie.setVisibility(View.INVISIBLE);
                                            Toast.makeText(UpdateProfile.this, "Image Successfully Uploaded: ", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        lottie.setVisibility(View.INVISIBLE);
                        Toast.makeText(UpdateProfile.this, "Fails To Upload Image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            } else if (imageDataInUriForm == null) {
                Toast.makeText(this, "No Image Is Selected", Toast.LENGTH_SHORT).show();
            } else if (Username.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please First You Need To Enter An Image Name", Toast.LENGTH_SHORT).show();
                Username.requestFocus();
            } else if (!isImageSelected) {
                Toast.makeText(this, "Please Select An Image", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "uploadOurImage:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String getExtension(Uri imageDataInUriForm) {
        try {
            ContentResolver objectContentResolver = getContentResolver();
            MimeTypeMap objectMimeTypeMap = MimeTypeMap.getSingleton();

            String extension = objectMimeTypeMap.getExtensionFromMimeType(objectContentResolver.getType(imageDataInUriForm));
            return extension;
        } catch (Exception e) {
            Toast.makeText(this, "getExtension:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return "";
    }


}
