package com.example.mfood;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;

import com.example.mfood.adapter.RecyclerViewFoodAdapter;
import com.example.mfood.model.Food;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    SearchView txtSearch;
    Button btnSearch;
    EditText Max,Min;
    FirebaseDatabase database;
    Spinner sp;
    DatabaseReference foods;
    public List<Food> mList;
    private RecyclerViewFoodAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //init
        recyclerView=findViewById(R.id.recyclerViewSearch);
        txtSearch=findViewById(R.id.search);
        sp=findViewById(R.id.spCategory);

        sp.setAdapter(new ArrayAdapter<String>(this,R.layout.item_spinner,getResources().getStringArray(R.array.category)));
        btnSearch=findViewById(R.id.btnSearch);
        Max=findViewById(R.id.Max);
        Min=findViewById(R.id.Min);
        //firebase
        database=FirebaseDatabase.getInstance();
        foods=database.getReference("food");
        //click
        txtSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                SearchName(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                SearchName(s);
                return true;
            }
        });
        //tìm  kiếm theo category
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int p, long l) {
                String cate=sp.getItemAtPosition(p).toString();
                mList = new ArrayList<>();

                LinearLayoutManager manager = new LinearLayoutManager(SearchActivity.this, RecyclerView.VERTICAL, false);
                recyclerView.setLayoutManager(manager);
                adapter = new RecyclerViewFoodAdapter(SearchActivity.this, mList);
                recyclerView.setAdapter(adapter);
                foods.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Food food = dataSnapshot.getValue(Food.class);
                            if(food.getType().contains(cate)){
                                mList.add(food);
                            }
                        }
                        adapter.setmList(mList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mList = new ArrayList<>();

                LinearLayoutManager manager = new LinearLayoutManager(SearchActivity.this, RecyclerView.VERTICAL, false);
                recyclerView.setLayoutManager(manager);
                adapter = new RecyclerViewFoodAdapter(SearchActivity.this, mList);
                recyclerView.setAdapter(adapter);
                foods.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Food food = dataSnapshot.getValue(Food.class);
                            int max=Integer.parseInt(Max.getText().toString());
                            int min=Integer.parseInt(Min.getText().toString());
                            int f=Integer.parseInt(food.getPrice());
                            if(f>=min&&f<=max){
                                mList.add(food);
                            }
                        }
                        adapter.setmList(mList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


    }

    private void SearchName(String key){
        mList = new ArrayList<>();

        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new RecyclerViewFoodAdapter(this, mList);
        recyclerView.setAdapter(adapter);
        foods.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Food food = dataSnapshot.getValue(Food.class);
                    if(food.getName().contains(key)){
                        mList.add(food);
                    }
                }
                adapter.setmList(mList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}