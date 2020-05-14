package ModelClassDM;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagram.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class StatusAdapterDMClass extends FirestoreRecyclerAdapter<StatusModelDMClass, StatusAdapterDMClass.ViewHolder> {

    public StatusAdapterDMClass(@NonNull FirestoreRecyclerOptions<StatusModelDMClass> options) {
        super(options);
    }

    private FirebaseUser firebaseAuth;
    String currentuser;

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull StatusModelDMClass statusModelClass) {
        if (firebaseAuth != null) {
            currentuser = firebaseAuth.getDisplayName();
        }
        try {
            firebaseAuth = FirebaseAuth.getInstance().getCurrentUser();
            if (currentuser.toString() != statusModelClass.getUsername().toString()) {
                viewHolder.Username.setText(statusModelClass.getUsername());
                Glide.with(viewHolder.URL.getContext())
                        .load(statusModelClass.getURL())
                        .into(viewHolder.URL);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View singlerow = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.single_row_search, parent, false
        );
        firebaseAuth = FirebaseAuth.getInstance().getCurrentUser();
        return new ViewHolder(singlerow);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView URL;
        TextView Username;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            URL = itemView.findViewById(R.id.avatar);
            Username = itemView.findViewById(R.id.UserProfile);
        }
    }
}
