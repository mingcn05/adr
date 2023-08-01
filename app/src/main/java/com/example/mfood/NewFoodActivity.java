package com.example.mfood;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;
public class NewFoodActivity extends AppCompatActivity {
    private static final String TAG = "To";
    EditText txtName, txtDes, txtPrice;
    Button btnSaveTask, btnCancel;
    ImageView img;
    Spinner sp;
    private final static int galleryPick=1;
    private Uri imageUri;
    private String saveCurDate,saveCurTime;
    private String downloadImgUrl,randomKey;
    private StorageReference imgRef;
    private DatabaseReference myRef;
    Integer NumWork = new Random().nextInt();
    String keytodo = Integer.toString(NumWork);
    Button btnSelectDate;
    private String name,des,price,cate;
    private int mYear, mMonth, mDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_food);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("New Food");
        actionBar.hide();
        initView();
        imgRef= FirebaseStorage.getInstance().getReference().child("food image");
        myRef= FirebaseDatabase.getInstance().getReference().child("food");
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        btnSaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
    private void validateData(){
        name=txtName.getText().toString();
        cate=sp.getSelectedItem().toString();
        des=txtDes.getText().toString();
        price=txtPrice.getText().toString();
        if(imageUri==null){
            Toast.makeText(this, "Chon anh", Toast.LENGTH_SHORT).show();
        }else if(des.isEmpty()){
            Toast.makeText(this, "chua mo ta viec", Toast.LENGTH_SHORT).show();
        }else if(name.isEmpty()){
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
                Toast.makeText(NewFoodActivity.this,
                        "error: "+message, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(NewFoodActivity.this,
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
                        Toast.makeText(NewFoodActivity.this,
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

                            Intent intent = new Intent(NewFoodActivity.this,
                                    MyReceiver.class);
                            intent.putExtra("myAction", "mDoNotify");
                            intent.putExtra("Name",name);
                            intent.putExtra("Des", des);
                            intent.putExtra("Price", price);

                            PendingIntent pendingIntent = PendingIntent.getBroadcast(NewFoodActivity.this,
                                    0, intent, 0);
                            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);




                            Intent intent1=new Intent(NewFoodActivity.this,
                                    MainActivity.class);
                            startActivity(intent1);
                            Toast.makeText(NewFoodActivity.this, "Them thanh cong!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(NewFoodActivity.this,
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
            img.setImageURI(imageUri);
        }
    }

    private void initView() {
        txtName = findViewById(R.id.txtName);
        txtDes = findViewById(R.id.txtDes);
        txtPrice = findViewById(R.id.txtPrice);
        sp=findViewById(R.id.spCategory);
        sp.setAdapter(new ArrayAdapter<String>(this,R.layout.item_spinner,getResources().getStringArray(R.array.category)));
        btnSaveTask = findViewById(R.id.btnSaveTask);
        btnCancel = findViewById(R.id.btnCancel);

        img=findViewById(R.id.img);

    }
}