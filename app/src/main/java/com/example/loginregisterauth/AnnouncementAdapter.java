package com.example.loginregisterauth;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;

public class AnnouncementAdapter extends FirebaseRecyclerAdapter<AnnouncementModel,AnnouncementAdapter.myViewHolder> {

    FirebaseAuth auth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AnnouncementAdapter(@NonNull FirebaseRecyclerOptions<AnnouncementModel> options) { super(options); }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull AnnouncementModel model) {

        holder.topic.setText(model.getTopic());
        holder.sender.setText(model.getSender());
        holder.message.setText(model.getMessage());

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.topic.getContext())
                        .setContentHolder(new ViewHolder(R.layout.feedback_popup))
                        .setExpanded(true,820)
                        .create();

                View view = dialogPlus.getHolderView();

                EditText topic = view.findViewById(R.id.txtTopic);
                EditText sender = view.findViewById(R.id.txtSender);
                EditText message = view.findViewById(R.id.txtMessage);

                Button btnUpdate = view.findViewById(R.id.UpdateBtn);

                topic.setText(model.getTopic());
                sender.setText(model.getSender());
                message.setText(model.getMessage());

                dialogPlus.show();

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap<String, Object> data = new HashMap<>();

                        data.put("topic",topic.getText().toString());
                        data.put("sender",sender.getText().toString());
                        data.put("message",message.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("Announcements")
                                .child(getRef(position).getKey()).updateChildren(data)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(holder.topic.getContext(), "Data has been updated successfully", Toast.LENGTH_SHORT).show();
                                dialogPlus.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(holder.topic.getContext(), "Error while Updating", Toast.LENGTH_SHORT).show();
                                dialogPlus.dismiss();
                            }
                        });
                    }
                });
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.topic.getContext());
                builder.setTitle("Delete Announcement?");
                builder.setMessage("Deleting data is irreversible");

                builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Announcements");
                        databaseReference.child(getRef(position).getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(holder.topic.getContext(), "Announcement Deleted", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(holder.topic.getContext(), "Announcement Deletion Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                //Return the user if user click cancel button
                builder.setNegativeButton("Cancel",null).create().show();

            }
        });

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.announcement_item,parent,false);
        return new myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder {

        TextView topic,sender,message;
        Button btnEdit,btnDelete;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            topic = (TextView) itemView.findViewById(R.id.aTopic);
            sender = (TextView) itemView.findViewById(R.id.aSender);
            message = (TextView) itemView.findViewById(R.id.aMessage);

            btnEdit = (Button) itemView.findViewById(R.id.EditBtn);
            btnDelete = (Button) itemView.findViewById(R.id.DeleteBtn);
        }
    }
}
