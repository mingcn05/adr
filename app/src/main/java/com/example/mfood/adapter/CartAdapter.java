package com.example.mfood.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mfood.FoodDetailActivity;
import com.example.mfood.R;
import com.example.mfood.model.Cart;
import com.example.mfood.model.Food;
import com.example.mfood.model.ItemClickListener;
import com.example.mfood.viewholder.CartViewHolder;
import com.example.mfood.viewholder.FoodViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder>{

    private Context mContext;
    private List<Cart> mList;

    public CartAdapter(Context mContext,List<Cart> mList) {
        this.mContext = mContext;
        this.mList=mList;
    }

    public void setmList(List<Cart> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_cart,parent,false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cart model=mList.get(position);


        holder.txtName.setText(model.getName());
        holder.txtQuantity.setText(String.valueOf(model.getSlg()));
        holder.txtPrice.setText(model.getPrice());
        int total=model.getSlg()*Integer.parseInt(model.getPrice());
        holder.txtTotal.setText(String.valueOf(total));
        Picasso.get().load(model.getImg()).into(holder.img);
        //items set onclick


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
