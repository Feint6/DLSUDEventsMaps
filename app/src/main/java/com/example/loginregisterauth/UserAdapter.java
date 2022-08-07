package com.example.loginregisterauth;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends FirebaseRecyclerAdapter<Users,UserAdapter.myViewHolder>{

    FirebaseAuth auth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public UserAdapter(@NonNull FirebaseRecyclerOptions<Users> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Users model) {
        holder.firstname.setText(model.getFirstname());
        holder.lastname.setText(model.getLastname());
        holder.email.setText(model.getEmail());

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.firstname.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_popup))
                        .setExpanded(true,850)
                        .create();

                View view = dialogPlus.getHolderView();

                EditText firstname = view.findViewById(R.id.txtFname);
                EditText lastname = view.findViewById(R.id.txtLname);
                EditText email = view.findViewById(R.id.txtEmail1);
                EditText phonenumber = view.findViewById(R.id.txtPhoneNo1);

                Button btnUpdate = view.findViewById(R.id.UpdateBtn);

                firstname.setText(model.getFirstname());
                lastname.setText(model.getLastname());
                email.setText(model.getEmail());
                phonenumber.setText(model.getPhonenumber());


                //email and phone cant be change
                email.setEnabled(false);
                phonenumber.setEnabled(false);
                dialogPlus.show();

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap<String, Object> data = new HashMap<>();
                        data.put("firstname",firstname.getText().toString());
                        data.put("lastname",lastname.getText().toString());
                        data.put("email",email.getText().toString());
                        data.put("phonenumber",phonenumber.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("Users")
                                .child(getRef(position).getKey()).updateChildren(data)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.firstname.getContext(), "Data has been Updated Successfully", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(holder.firstname.getContext(), "Error while Updating", Toast.LENGTH_SHORT).show();
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
                //setting up the alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.firstname.getContext());
                builder.setTitle("Delete User Information?");
                builder.setMessage("Deleting data is irreversible.");

                //Delete the user if the user clicks/taps the continue button
                builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //deleting the current user realtime database
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                        databaseReference.child(getRef(position).getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(holder.firstname.getContext(), "Account Deleted.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(holder.firstname.getContext(), "Account Deletion Failed.", Toast.LENGTH_SHORT).show();
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item,parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{

        TextView firstname,lastname,email;
        Button btnEdit,btnDelete;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            firstname = (TextView) itemView.findViewById(R.id.FirstNameText);
            lastname = (TextView) itemView.findViewById(R.id.LastNameText);
            email = (TextView) itemView.findViewById(R.id.EmailText);

            btnEdit = (Button) itemView.findViewById(R.id.EditBtn);
            btnDelete = (Button) itemView.findViewById(R.id.DeleteBtn);
        }
    }
}
