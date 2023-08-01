package com.example.mfood.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mfood.FoodDetailActivity;
import com.example.mfood.OrderActivity;
import com.example.mfood.R;
import com.example.mfood.model.Bill;
import com.example.mfood.viewholder.OrderViewHolder;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderViewHolder>{

    private Context mContext;
    private List<Bill> mList;

    public OrderAdapter(Context mContext,List<Bill> mList) {
        this.mContext = mContext;
        this.mList=mList;
    }

    public void setmList(List<Bill> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_order_status,parent,false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Bill model=mList.get(position);
        holder.txtKey.setText(model.getKey());
        holder.txtStatus.setText(convertStatus(model.getStatus()));
        holder.txtAddress.setText(model.getAddress());
        holder.txtTotal.setText(model.getTotal());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(mContext, OrderActivity.class);
                intent.putExtra("key1",model.getKey());
                mContext.startActivity(intent);
            }
        });
    }
    private String convertStatus(String s){
        if (s.equals("0")) return "Placed";
        if (s.equals("1")) return "Shipping";
        return "Shipped";
    }
    @Override
    public int getItemCount() {
        return mList.size();
    }
}