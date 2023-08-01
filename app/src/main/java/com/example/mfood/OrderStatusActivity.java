package com.example.mfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.mfood.adapter.OrderAdapter;
import com.example.mfood.adapter.RecyclerViewFoodAdapter;
import com.example.mfood.model.Bill;
import com.example.mfood.model.Food;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderStatusActivity extends AppCompatActivity {
    private RecyclerView reOrderStatus;
    public List<Bill> mList;
    private OrderAdapter adapter;
    DatabaseReference foods;
    private FirebaseUser auth = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);
        //init
        reOrderStatus=findViewById(R.id.reOderStatus);
        //view
        mList = new ArrayList<>();
        String email=auth.getEmail();
        foods= FirebaseDatabase.getInstance().getReference("bill").child(email.replace(".","-"));
        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        reOrderStatus.setLayoutManager(manager);
        adapter = new OrderAdapter(this, mList);
        reOrderStatus.setAdapter(adapter);
        foods.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Bill food = dataSnapshot.getValue(Bill.class);

                        mList.add(food);

                }
                adapter.setmList(mList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}