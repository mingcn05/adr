package com.example.mfood;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mfood.model.Food;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class EditDeleteActivity extends AppCompatActivity {
    TextView name,des,price;
    private String txtname,txtdes,txtprice,cate;
    private final static int galleryPick=1;
    ImageView food_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Button btnUpdate,btnDelete;
    EditText number;
    private String foodId="";
    private Uri imageUri;
    Spinner sp;
    FirebaseDatabase database;
    DatabaseReference foods;
    Integer NumWork = new Random().nextInt();
    String keytodo = Integer.toString(NumWork);
    Button btnSelectDate;
    private String saveCurDate,saveCurTime;
    private static final String TAG = "To";
    private StorageReference imgRef;
    private DatabaseReference myRef;
    private String downloadImgUrl,randomKey;
    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delete);
        //database
        database = FirebaseDatabase.getInstance();
        foods=database.getReference("food");

        //init
        btnDelete=findViewById(R.id.btnDelete);
        btnUpdate=findViewById(R.id.btnUpdate);

        sp=findViewById(R.id.spCategory);
        sp.setAdapter(new ArrayAdapter<String>(this,R.layout.item_spinner,getResources().getStringArray(R.array.category)));
        des=findViewById(R.id.txtDes);
        name=findViewById(R.id.txtName);
        food_image=findViewById(R.id.img);
        price=findViewById(R.id.txtPrice);
        imgRef= FirebaseStorage.getInstance().getReference().child("food image");
        myRef= FirebaseDatabase.getInstance().getReference().child("food");
        //set on click
        food_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFood();
            }
        });

        if (getIntent()!=null) {
            foodId=getIntent().getStringExtra("key");
        }
        if (!foodId.isEmpty()){
            getDetailFood(foodId);
        }
    }
    private void getDetailFood(String foodId){
        foods.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Food food=snapshot.getValue(Food.class);
                //set Image

                Picasso.get().load(food.getImage()).into(food_image);

                price.setText(food.getPrice());
                name.setText(food.getName());
                des.setText(food.getDes());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void deleteFood(){

    }
    private void validateData(){
        txtname=name.getText().toString();
        cate=sp.getSelectedItem().toString();
        txtdes=des.getText().toString();
        txtprice=price.getText().toString();
        if(imageUri==null){
            Toast.makeText(this, "Chon anh", Toast.LENGTH_SHORT).show();
        }else if(txtdes.isEmpty()){
            Toast.makeText(this, "chua mo ta viec", Toast.LENGTH_SHORT).show();
        }else if(txtname.isEmpty()){
            Toast.makeText(this, "nhap ten viec", Toast.LENGTH_SHORT).show();
        }else{
            storeWork();
        }
    }
    private void storeWork(){
        Calendar c=Calendar.getInstance();
        SimpleDateFormat curDate=new SimpleDateFormat("dd-MM-yyyy");
        saveCurDate=curDate.format(c.getTime());
        SimpleDateFormat curTime=new SimpleDateFormat("HH:mm:ss");
        saveCurTime=curTime.format(c.getTime());
        randomKey=saveCurDate+"-"+saveCurTime;
        StorageReference filePath=imgRef.child(
                imageUri.getLastPathSegment()+randomKey+".jpg");
        final UploadTask uploadTask=filePath.putFile(imageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message=e.toString();
                Toast.makeText(EditDeleteActivity.this,
                        "error: "+message, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(EditDeleteActivity.this,
                        "up anh thanh cong!", Toast.LENGTH_SHORT).show();
                Task<Uri> uriTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }
                        downloadImgUrl=filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        downloadImgUrl=task.getResult().toString();
                        Toast.makeText(EditDeleteActivity.this,
                                "Luu Url anh thanh cong!", Toast.LENGTH_SHORT).show();
                        saveFoodtoDatabas();
                    }
                });
            }
        });
    }
    private void saveFoodtoDatabas(){
        HashMap<String,Object> work=new HashMap<>();
        work.put("key",TAG+randomKey);
        work.put("name",name);
        work.put("des",des);
        work.put("type",cate);
        work.put("slg",0);
        work.put("price",price);
        work.put("image",downloadImgUrl);
        myRef.child(TAG+randomKey).updateChildren(work)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(System.currentTimeMillis());
                            AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

                            Intent intent = new Intent(EditDeleteActivity.this,
                                    MyReceiver.class);
                            intent.putExtra("myAction", "mDoNotify");
                            intent.putExtra("Name",txtname);
                            intent.putExtra("Des", txtdes);
                            intent.putExtra("Price", txtprice);

                            PendingIntent pendingIntent = PendingIntent.getBroadcast(EditDeleteActivity.this,
                                    0, intent, 0);
                            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                            Intent intent1=new Intent(EditDeleteActivity.this,
                                    MainActivity.class);
                            startActivity(intent1);
                            Toast.makeText(EditDeleteActivity.this, "Update thanh cong!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(EditDeleteActivity.this,
                                    "Them khong thanh cong!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void openGallery(){
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,galleryPick);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==galleryPick && resultCode==RESULT_OK && data!=null){
            imageUri=data.getData();
            food_image.setImageURI(imageUri);
        }
    }
}