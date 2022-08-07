package com.example.loginregisterauth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecyclerBuildingAdapter extends RecyclerView.Adapter<RecyclerBuildingAdapter.ImageViewHolder>{

    private Context mContext;
    private List<NewBuildingModel> mBuilding;
    private RecyclerBuildingAdapter.OnItemClickListener mListener;

    public RecyclerBuildingAdapter(Context context, List<NewBuildingModel> buildings) {
        mContext = context;
        mBuilding = buildings;
    }

    @Override
    public RecyclerBuildingAdapter.ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.newbldg_layout, parent, false);
        return new RecyclerBuildingAdapter.ImageViewHolder(v);
    }


    @Override
    public void onBindViewHolder(RecyclerBuildingAdapter.ImageViewHolder holder, int position) {
        NewBuildingModel uploadCurrent = mBuilding.get(position);
        holder.textViewBName.setText(uploadCurrent.getbName());
        holder.textViewBDesc.setText(uploadCurrent.getaImageUrl());

        Picasso.get()
                .load(uploadCurrent.getcDesc())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .into(holder.imageBView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, NewBuildingActivity.class);
                intent.putExtra("object@#",mBuilding.get(position).getcDesc());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mBuilding.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        public TextView textViewBName;
        public TextView textViewBDesc;
        public ImageView imageBView;

        public ImageViewHolder(View itemView) {
            super(itemView);

            textViewBName = itemView.findViewById(R.id.bldgname);
            textViewBDesc = itemView.findViewById(R.id.bldgdesc);
            imageBView = itemView.findViewById(R.id.imgviewbldg);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");
            MenuItem delete = menu.add(Menu.NONE, 1, 1, "Delete");

            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {

                    switch (item.getItemId()) {
                        case 1:
                            mListener.onDeleteClick(position);
                            return true;

                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(RecyclerBuildingAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

}
