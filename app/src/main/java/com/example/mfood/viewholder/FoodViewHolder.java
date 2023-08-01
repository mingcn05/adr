package com.example.mfood.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mfood.R;
import com.example.mfood.model.ItemClickListener;

public class FoodViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener{
    public TextView txtName,txtPrice;
    public ImageView img;
    public ItemClickListener listener;
    public FoodViewHolder(@NonNull View view) {
        super(view);
        txtName=view.findViewById(R.id.txtName);
        txtPrice=view.findViewById(R.id.txtPrice);
        img=view.findViewById(R.id.img);
    }

    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view,getAdapterPosition(),false);
    }
}