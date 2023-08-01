package com.example.mfood;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mfood.adapter.RecyclerViewFoodAdapter;
import com.example.mfood.model.Food;
import com.example.mfood.viewholder.FoodViewHolder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    FloatingActionButton fabutton;

    DatabaseReference myRef;
    RecyclerView recyclerView;
    public List<Food> mList;
    private RecyclerViewFoodAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_main,
                container, false);
        initView(view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        myRef = FirebaseDatabase.getInstance().getReference().child("food");

        fabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),
                        CartActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void initView(View view) {
        fabutton=view.findViewById(R.id.fabutton);
        recyclerView=view.findViewById(R.id.recyclerView);
    }


    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mList=new ArrayList<>();

                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    Food food = dataSnapshot1.getValue(Food.class);
                    mList.add(food);
                }

                adapter = new RecyclerViewFoodAdapter(getActivity(),mList);
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // set code to show an error
                Toast.makeText(getActivity(), "No Data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}