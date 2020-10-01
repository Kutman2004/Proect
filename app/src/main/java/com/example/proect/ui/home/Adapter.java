package com.example.proect.ui.home;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proect.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyHolder> {
    Context context;
    List<Upload> uploadList;


    public Adapter(Context context, List<Upload> uploadList) {
        this.context = context;
        this.uploadList = uploadList;
    }


    @Override
    public MyHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_item, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {
        final Upload upload = uploadList.get(position);
        if (upload.getUrl() != null) {
            Picasso.with(context)
                    .load(upload.getUrl())
                    .placeholder(R.mipmap.ic_launcher)
                    .fit()
                    .centerCrop()
                    .into(holder.imageView);
        }


    }
    @Override
    public int getItemCount() {
        return uploadList.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.download_image_view);
        }
    }
}
