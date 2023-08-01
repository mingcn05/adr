package com.example.mfood;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.mfood.adapter.RecyclerViewFoodAdapter;
import com.example.mfood.model.Bill;
import com.example.mfood.model.Cart;
import com.example.mfood.model.Food;
import com.google.android.gms.common.internal.service.Common;
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

public class CartActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference request;
    DatabaseReference myRef;
    TextView txtTong,txtTotal;
    Button btnOrder;

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
        actionBar.setTitle("Cart");
        setContentView(R.layout.activity_cart);


        //init
        recyclerView = findViewById(R.id.listCart);

        txtTong = findViewById(R.id.txtTong);

        btnOrder = findViewById(R.id.btnBuy);

        email = auth.getEmail();

//
        carts = FirebaseDatabase.getInstance().getReference("cart").child(email.replace(".", "-"));
        ViewCart();

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlerDialog();
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
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Cart food = dataSnapshot.getValue(Cart.class);

                    mList.add(food);

                }
                int sum=0;
                for(Cart c:mList){

                    sum+=(c.getSlg()*Integer.parseInt(c.getPrice()));
                }
                txtTong.setText(String.valueOf(sum));
                adapter.setmList(mList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter = new CartAdapter(this, mList);
        recyclerView.setAdapter(adapter);
    }

    private void showAlerDialog(){

        AlertDialog.Builder builder=new AlertDialog.Builder(CartActivity.this);


        builder.setTitle("One more step");
        builder.setMessage("enter your address");
        final EditText editText=new EditText(CartActivity.this);
        LinearLayout.LayoutParams lp= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        editText.setLayoutParams(lp);
        builder.setView(editText);
        builder.setIcon(R.drawable.cart);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String e=auth.getEmail();
                String phone=auth.getPhoneNumber();
                String name=auth.getDisplayName();
                Calendar calendar = Calendar.getInstance();
                String gio = calendar.get(Calendar.HOUR) + "";
                String phut = calendar.get(Calendar.MINUTE) + "";
                String giay = calendar.get(Calendar.SECOND) + "";
                String key="To"+gio+""+phut+""+giay;
                Bill.child(e.replace(".", "-")).child(key).setValue(new Bill(key,name, phone ,editText.getText().toString(),txtTong.getText().toString(),mList)).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(CartActivity.this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
                        //xoá node
                        FirebaseDatabase.getInstance().getReference().child("cart").child(email.replace(".", "-")).removeValue();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CartActivity.this, "Đặt hàng thất bại!", Toast.LENGTH_SHORT).show();
                    }
                });
                finish();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
            }
        });
        builder.show();
    }

}