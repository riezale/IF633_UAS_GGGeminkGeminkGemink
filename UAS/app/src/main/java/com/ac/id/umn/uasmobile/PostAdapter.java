package com.ac.id.umn.uasmobile;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    public static final String TAG = "PostAdapter";
    ArrayList<Post> mList;
    Context context;
    Activity activity;

    public PostAdapter(Context context , ArrayList<Post> mList, Activity activity){

        this.mList = mList;
        this.context = context;
        this.activity = activity;

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
        holder.btndownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveimage(holder);
            }
        });
    }

    private void saveimage(MyViewHolder holder) {

        Log.d(TAG, "saveimage: downloading selected image");

        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

        FileOutputStream fileOutputStream = null;
        File file = getDisc();

        if (!file.exists() && !file.mkdirs()) {

            file.mkdirs();
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyymmsshhmmss");
        String date = simpleDateFormat.format(new Date());
        String name = "IMG" + date + "jpg";
        String file_name = file.getAbsolutePath()+"/"+name;
        File new_file = new File(file_name);

        try {

            BitmapDrawable draw = (BitmapDrawable) holder.ImageUrl.getDrawable();
            Bitmap bitmap = draw.getBitmap();
            fileOutputStream = new FileOutputStream(new_file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
            Toast.makeText(context, "Save Image Success", Toast.LENGTH_SHORT).show();
            fileOutputStream.flush();
            fileOutputStream.close();

        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }

        refreshGallery(new_file);
    }

    private void refreshGallery(File file) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        context.sendBroadcast(intent);
    }

    private File getDisc() {
         File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
         return new File(file, "Saved Picture");
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView description;
        ImageView ImageUrl,btndownload;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            description = itemView.findViewById(R.id.m_description);
            ImageUrl = itemView.findViewById(R.id.m_image);
            btndownload = itemView.findViewById(R.id.btndownload);
        }
    }
}
