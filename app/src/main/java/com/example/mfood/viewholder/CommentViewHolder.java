package com.example.mfood.viewholder;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mfood.R;
import com.example.mfood.model.ItemClickListener;

public class CommentViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener{
    public TextView txtName;
    public TextView txtComment;
    public ItemClickListener listener;
    public CommentViewHolder(@NonNull View view) {
        super(view);
        txtName=view.findViewById(R.id.txtName);
        txtComment=view.findViewById(R.id.txtComment);

    }

    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view,getAdapterPosition(),false);
    }
}
