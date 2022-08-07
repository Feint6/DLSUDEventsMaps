package com.example.loginregisterauth;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class FeedbackAdapter extends FirebaseRecyclerAdapter<FeedbackModel,FeedbackAdapter.myViewHolder> {

    FirebaseAuth auth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FeedbackAdapter(@NonNull FirebaseRecyclerOptions<FeedbackModel> options) { super(options); }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_item,parent,false);
        return new myViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull FeedbackModel model) {
        holder.useremail.setText(model.getUseremail());
        holder.usercomment.setText(model.getUsercomment());

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setting up the alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.useremail.getContext());
                builder.setTitle("Delete Feedback?");
                builder.setMessage("Deleting data is irreversible.");

                builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Feedbacks");
                        databaseReference.child(getRef(position).getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(holder.useremail.getContext(), "Feedback Deleted", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(holder.useremail.getContext(), "Failed to Delete Feedback", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                //Return the user if user click cancel button
                builder.setNegativeButton("Cancel",null).create().show();

            }
        });
    }


    public class myViewHolder extends RecyclerView.ViewHolder {

        TextView useremail,usercomment;
        Button btnDelete;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            useremail = (TextView) itemView.findViewById(R.id.userEmailText);
            usercomment = (TextView) itemView.findViewById(R.id.userCommentText);
            btnDelete = (Button) itemView.findViewById(R.id.DeleteBtn);
        }
    }
}
