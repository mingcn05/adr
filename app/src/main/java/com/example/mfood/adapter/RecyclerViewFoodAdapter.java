package com.example.mfood.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mfood.EditDeleteActivity;
import com.example.mfood.FoodDetailActivity;
import com.example.mfood.R;
import com.example.mfood.model.Food;
import com.example.mfood.viewholder.FoodViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewFoodAdapter extends RecyclerView.Adapter<FoodViewHolder> {
    private Context mContext;
    private List<Food> mList;

    public RecyclerViewFoodAdapter(Context mContext,List<Food> mList) {
        this.mContext = mContext;
        this.mList=mList;
    }

    public void setmList(List<Food> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item,parent,false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food model=mList.get(position);


        holder.txtName.setText(model.getName());
        holder.txtPrice.setText(model.getPrice());

        Picasso.get().load(model.getImage()).into(holder.img);
        //items set onclick
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(mContext, FoodDetailActivity.class);
                intent.putExtra("key",model.getKey());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
