package com.example.loginregisterauth;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AnnouncementUserAdapter extends FirebaseRecyclerAdapter<AnnouncementUserModel,AnnouncementUserAdapter.myViewHolder> {

    FirebaseAuth auth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AnnouncementUserAdapter(@NonNull FirebaseRecyclerOptions<AnnouncementUserModel> options) { super(options); }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull AnnouncementUserModel model) {
        holder.topic.setText(model.getTopic());
        holder.sender.setText(model.getSender());
        holder.message.setText(model.getMessage());
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.announcement_user_item,parent,false);
        return new AnnouncementUserAdapter.myViewHolder(view);
    }


    public class myViewHolder extends RecyclerView.ViewHolder {

        TextView topic,sender,message;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            topic = (TextView) itemView.findViewById(R.id.uTopic);
            sender = (TextView) itemView.findViewById(R.id.uSender);
            message = (TextView) itemView.findViewById(R.id.uAnnouncement);

        }
    }
}
