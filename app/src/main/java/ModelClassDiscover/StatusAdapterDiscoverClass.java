package ModelClassDiscover;

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


public class StatusAdapterDiscoverClass extends FirestoreRecyclerAdapter<StatusModelDiscoverClass, StatusAdapterDiscoverClass.ViewHolder> {
    public StatusAdapterDiscoverClass(@NonNull FirestoreRecyclerOptions<StatusModelDiscoverClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull StatusModelDiscoverClass statusModelDiscoverClass) {
        viewHolder.Username.setText(statusModelDiscoverClass.getUsername());
        Glide.with(viewHolder.URL.getContext())
                .load(statusModelDiscoverClass.getURL())
                .into(viewHolder.URL);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View singlerow = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.single_row_users, parent, false
        );
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
