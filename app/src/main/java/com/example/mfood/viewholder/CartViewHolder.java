package com.example.mfood.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mfood.R;
import com.example.mfood.model.ItemClickListener;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView txtPrice,txtName,txtQuantity,txtTotal;
    public ImageView img;

    private ItemClickListener listener;


    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        txtName=itemView.findViewById(R.id.txtName);
        txtPrice=itemView.findViewById(R.id.txtPrice);
        txtQuantity=itemView.findViewById(R.id.txtQuantity);
        txtTotal=itemView.findViewById(R.id.txtTotal);
        img=itemView.findViewById(R.id.img);
    }
    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }
    @Override
    public void onClick(View view) {

    }
}