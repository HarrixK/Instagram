package ModelClass;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.instagram.R;
import com.example.instagram.ReportPost;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class StatusAdapterClass extends FirestoreRecyclerAdapter<StatusModelClass, StatusAdapterClass.ViewHolder> {

    private onItemClickListner listner;
    Context ctx;

    public StatusAdapterClass(@NonNull FirestoreRecyclerOptions<StatusModelClass> options, Context ctx) {
        super(options);
        this.ctx = ctx;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull final StatusModelClass statusModelClass) {
        viewHolder.Comments.setText(statusModelClass.getComments());
        viewHolder.UserProfile.setText(statusModelClass.getBy());
        Glide.with(viewHolder.URL.getContext())
                .load(statusModelClass.getURL())
                .into(viewHolder.URL);

        viewHolder.URL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(ctx, ReportPost.class);
                    intent.putExtra("URL", statusModelClass.getURL());
                    intent.putExtra("Comment", statusModelClass.getComments());
                    intent.putExtra("DisplayName", statusModelClass.getBy());
                    ctx.startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(ctx, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View singlerow = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.single_row, parent, false
        );
        return new ViewHolder(singlerow);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView URL, Like;
        TextView Comments, UserProfile;
        LottieAnimationView lottie;
        View mView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            URL = itemView.findViewById(R.id.IV);

            Comments = itemView.findViewById(R.id.TextHere);
            UserProfile = itemView.findViewById(R.id.UserProfile);

            lottie = itemView.findViewById(R.id.Lottie);

            Like = itemView.findViewById(R.id.Like);
            Like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listner != null) {
                        listner.onItemClick(getSnapshots().getSnapshot(position), position);
                        lottie.playAnimation();
                        lottie.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    public interface onItemClickListner {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListner(onItemClickListner listner) {
        this.listner = listner;
    }
}
