package com.example.loginregisterauth;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecyclerPosterAdapter extends RecyclerView.Adapter<RecyclerPosterAdapter.ViewHolder> {

    private Context context;
    private ArrayList<PosterModel> posterModelArrayList;

    public RecyclerPosterAdapter(Context context, ArrayList<PosterModel> posterModelArrayList) {
        this.context = context;
        this.posterModelArrayList = posterModelArrayList;
    }

    @NonNull
    @Override
    public RecyclerPosterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.poster_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context)
                .load(posterModelArrayList.get(position).getImageurl())
                .into(holder.imageView);

        holder.textView.setText(posterModelArrayList.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PosterActivity.class);
                intent.putExtra("object@#",posterModelArrayList.get(position).getImageurl());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return posterModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.posterview);
            textView = itemView.findViewById(R.id.postername);
        }
    }
}
