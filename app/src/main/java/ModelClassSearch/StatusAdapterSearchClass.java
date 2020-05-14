package ModelClassSearch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagram.R;

import java.util.ArrayList;


public class StatusAdapterSearchClass extends RecyclerView.Adapter<StatusAdapterSearchClass.ViewHolder> {
    public StatusAdapterSearchClass(ArrayList<StatusModelSearchClass> list) {
        this.list = list;
    }

    ArrayList<StatusModelSearchClass> list;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View singlerow = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.single_row_dm, parent, false
        );

        return new ViewHolder(singlerow);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        try {
            viewHolder.UserProfile.setText(list.get(position).getUsername());
            Glide.with(viewHolder.URL.getContext())
                    .load(list.get(position).getURL())
                    .into(viewHolder.URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView URL;
        TextView UserProfile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            URL = itemView.findViewById(R.id.avatar);
            UserProfile = itemView.findViewById(R.id.UserProfile);
        }
    }
}
