package com.example.mfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mfood.adapter.CartAdapter;
import com.example.mfood.model.Bill;
import com.example.mfood.model.Cart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class OrderActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference request;
    DatabaseReference myRef;
    TextView txtTong,txtTotal;
    Button btnCancel;

    public List<Cart> mList;
    private CartAdapter adapter;
    DatabaseReference carts;
    String email;
    private FirebaseUser auth = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference Bill = FirebaseDatabase.getInstance().getReference("bill");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Order");
        setContentView(R.layout.activity_order);


        //init
        recyclerView = findViewById(R.id.listCart);

        txtTong = findViewById(R.id.txtTong);

        btnCancel = findViewById(R.id.btnCancel);

        email = auth.getEmail();

        String key=getIntent().getStringExtra("key1");
        carts = FirebaseDatabase.getInstance().getReference("bill").child(email.replace(".", "-")).child(key);
        ViewCart();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }

    private void ViewCart() {
        mList = new ArrayList<>();

        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        carts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    Bill bill = snapshot.getValue(Bill.class);



                    mList.addAll(bill.getFoods());



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter = new CartAdapter(this, mList);
        recyclerView.setAdapter(adapter);
    }



}