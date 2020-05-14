package ModelClassProfile;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagram.DisplayUser;
import com.example.instagram.ProfileDisplay;
import com.example.instagram.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.net.URL;
import java.util.ArrayList;

import ModelClass.StatusAdapterClass;
import ModelClass.StatusModelClass;
import ModelClassUser.StatusModelUserClass;


public class StatusAdapterProfileClass extends FirestoreRecyclerAdapter<StatusModelClass, StatusAdapterProfileClass.ViewHolder> {
    private StatusAdapterClass.onItemClickListner listner;

    Context ctx;

    public StatusAdapterProfileClass(@NonNull FirestoreRecyclerOptions<StatusModelClass> options, Context ctx) {
        super(options);
        this.ctx = ctx;
    }

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseUser firebaseAuth;
    String currentuser;

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i, @NonNull final StatusModelClass statusModelClass) {
        firebaseAuth = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseAuth != null) {
            currentuser = firebaseAuth.getDisplayName();
        }
        try {
            viewHolder.Comments.setText(statusModelClass.getComments());
            viewHolder.UserProfile.setText(statusModelClass.getBy());
            Glide.with(viewHolder.URL.getContext())
                    .load(statusModelClass.getURL())
                    .into(viewHolder.URL);

            viewHolder.RL_Profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(ctx, ProfileDisplay.class);
                        intent.putExtra("URL", statusModelClass.getURL());
                        intent.putExtra("Comment", statusModelClass.getComments());
                        intent.putExtra("DisplayName", statusModelClass.getBy());
                        ctx.startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(ctx, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View singlerow = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.single_row_profile, parent, false
        );
        firebaseAuth = FirebaseAuth.getInstance().getCurrentUser();


        return new ViewHolder(singlerow);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView URL;
        TextView Comments, UserProfile;
        RelativeLayout RL_Profile;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            URL = itemView.findViewById(R.id.IV);
            Comments = itemView.findViewById(R.id.TextHere);
            UserProfile = itemView.findViewById(R.id.UserProfile);
            RL_Profile = itemView.findViewById(R.id.RL_Profile);
        }
    }

    public interface onItemClickListner {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListner(StatusAdapterClass.onItemClickListner listner) {
        this.listner = listner;
    }
}
