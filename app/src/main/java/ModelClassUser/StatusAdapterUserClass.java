package ModelClassUser;

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
import com.example.instagram.R;

import java.util.ArrayList;


public class StatusAdapterUserClass extends RecyclerView.Adapter<StatusAdapterUserClass.ViewHolder> {
    ArrayList<StatusModelUserClass> list;
    Context ctx;

    public StatusAdapterUserClass(ArrayList<StatusModelUserClass> list, Context ctx) {
        this.list = list;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View singlerow = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.single_row_search, parent, false
        );

        return new ViewHolder(singlerow);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        try {
            viewHolder.UserProfile.setText(list.get(position).getUsername());
            viewHolder.Name.setText(list.get(position).getDisplayName());
            Glide.with(viewHolder.URL.getContext())
                    .load(list.get(position).getURL())
                    .into(viewHolder.URL);
            viewHolder.parentlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(ctx, DisplayUser.class);
                        intent.putExtra("URL", list.get(position).getURL());
                        intent.putExtra("DisplayName", list.get(position).getDisplayName());
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

    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView URL;
        TextView UserProfile, Name;
        RelativeLayout parentlayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            URL = itemView.findViewById(R.id.avatar);

            UserProfile = itemView.findViewById(R.id.UserProfile);
            Name = itemView.findViewById(R.id.Name);

            parentlayout = itemView.findViewById(R.id.single_row_RL);
        }
    }
}
