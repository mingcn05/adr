package com.example.mfood.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mfood.FoodDetailActivity;
import com.example.mfood.R;
import com.example.mfood.model.Cart;
import com.example.mfood.model.Comment;
import com.example.mfood.model.Food;
import com.example.mfood.viewholder.CartViewHolder;
import com.example.mfood.viewholder.CommentViewHolder;
import com.example.mfood.viewholder.FoodViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder>{

    private Context mContext;
    private List<Comment> mList;

    public CommentAdapter(Context mContext,List<Comment> mList) {
        this.mContext = mContext;
        this.mList=mList;
    }

    public void setmList(List<Comment> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_comment,parent,false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment model=mList.get(position);
        holder.txtName.setText(model.getName());
        holder.txtComment.setText(model.getComment());


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}