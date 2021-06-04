package com.ac.id.umn.uasmobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    ArrayList<Post> mList;
    Context context;

    public PostAdapter(Context context , ArrayList<Post> mList){

        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item , parent , false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Post model = mList.get(position);
        holder.description.setText(model.getDescription());
        Glide.with(context).load(mList.get(position).getImageUrl()).into(holder.ImageUrl);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView description;
        ImageView ImageUrl;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            description = itemView.findViewById(R.id.m_description);
            ImageUrl = itemView.findViewById(R.id.m_image);
        }
    }
}
