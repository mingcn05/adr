package com.example.mfood;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.mfood.adapter.CartAdapter;
import com.example.mfood.adapter.CommentAdapter;
import com.example.mfood.adapter.RecyclerViewFoodAdapter;
import com.example.mfood.model.Cart;
import com.example.mfood.model.Comment;
import com.example.mfood.model.Food;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class FoodDetailActivity extends AppCompatActivity {
    TextView food_name,food_des,food_price;
    ImageView food_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    EditText number;
    private String foodId="";
    FirebaseDatabase database;
    DatabaseReference foods;
    private String downloadImgUrl,randomKey;
    private DatabaseReference baseCart = FirebaseDatabase.getInstance().getReference("cart");
    private DatabaseReference baseComment = FirebaseDatabase.getInstance().getReference("comment");

    private String saveCurDate,saveCurTime;
    private StorageReference imgRef;
    private DatabaseReference myRef;
    private Uri imageUri;
    Food currentFood;
    //comment
    RecyclerView reComment;
    EditText comment;
    TextView Name;
    ImageView send;
    public List<Comment> mList;
    DatabaseReference comments;
    private CommentAdapter adapter;

    private FirebaseUser auth = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        //database
        database =FirebaseDatabase.getInstance();
        foods=database.getReference("food");


        imgRef= FirebaseStorage.getInstance().getReference().child("cart image");
        myRef= FirebaseDatabase.getInstance().getReference().child("cart");
        //init

        btnCart=findViewById(R.id.btnCart);
        number=findViewById(R.id.number_button);
        food_des=findViewById(R.id.food_des);
        food_name=findViewById(R.id.food_name);
        food_image=findViewById(R.id.img_food);
        food_price=findViewById(R.id.food_price);
        reComment=findViewById(R.id.reComment);

        comment=findViewById(R.id.Comment);
        send=findViewById(R.id.send);
        Name=findViewById(R.id.Name);
        //ten nguoi comment
        Name.setText(auth.getEmail());


        collapsingToolbarLayout=findViewById(R.id.collap);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);
        if (getIntent()!=null) {
            foodId=getIntent().getStringExtra("key");
        }
        if (!foodId.isEmpty()){
            getDetailFood(foodId);
        }
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendComment();
            }
        });
        comments = database.getReference("comment").child(foodId);
        viewComment();


    }
    private void sendComment(){
        Calendar calendar = Calendar.getInstance();
        String gio = calendar.get(Calendar.HOUR) + "";
        String phut = calendar.get(Calendar.MINUTE) + "";
        String giay = calendar.get(Calendar.SECOND) + "";
        String email = auth.getEmail();
        baseComment.child(foodId).child("To"+gio+":"+phut+":"+giay).setValue(new Comment(email.replace(".","-"),comment.getText().toString())).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(FoodDetailActivity.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(FoodDetailActivity.this, "Thêm thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void viewComment(){
        mList = new ArrayList<>();

        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        reComment.setLayoutManager(manager);
        adapter = new CommentAdapter(this, mList);
        reComment.setAdapter(adapter);
        comments.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Comment f = dataSnapshot.getValue(Comment.class);

                        mList.add(f);

                }
                adapter.setmList(mList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getDetailFood(String foodId){
        foods.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentFood=snapshot.getValue(Food.class);
                //set Image

                Picasso.get().load(currentFood.getImage()).into(food_image);
                collapsingToolbarLayout.setTitle(currentFood.getName());
                food_price.setText(currentFood.getPrice());
                food_name.setText(currentFood.getName());
                food_des.setText(currentFood.getDes());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void validateData(){
        String slg=number.getText().toString();

        if(slg==null){
            Toast.makeText(this, "Vui lòng nhập số lượng!", Toast.LENGTH_SHORT).show();

        }else{
//            stoFood();
            saveCart();

        }
    }

    private void saveCart(){
        Calendar calendar = Calendar.getInstance();
        String gio = calendar.get(Calendar.HOUR) + "";
        String phut = calendar.get(Calendar.MINUTE) + "";
        String giay = calendar.get(Calendar.SECOND) + "";

        String email = auth.getEmail();

        String tenMon = food_name.getText().toString();
        String gia = food_price.getText().toString();
        String imgUrl=currentFood.getImage();
        int soLuong = Integer.valueOf(number.getText().toString());
        baseCart.child(email.replace(".", "-")).child(gio+":"+phut+":"+giay).setValue(new Cart(tenMon, gia ,imgUrl,soLuong)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(FoodDetailActivity.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(FoodDetailActivity.this, "Thêm thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }


}