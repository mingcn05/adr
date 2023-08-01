package com.example.mfood.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mfood.R;
import com.example.mfood.model.ItemClickListener;

public class OrderViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener{
    public TextView txtKey,txtStatus,txtAddress,txtTotal;

    public ItemClickListener listener;
    public OrderViewHolder(@NonNull View view) {
        super(view);
        txtKey=view.findViewById(R.id.txtKey);
        txtStatus=view.findViewById(R.id.txtStatus);
        txtAddress=view.findViewById(R.id.txtAddress);
        txtTotal=view.findViewById(R.id.txtTotal);

    }

    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view,getAdapterPosition(),false);
    }
}

