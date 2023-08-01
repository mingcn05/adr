package com.example.mfood;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mfood.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//public class RegisterActivity extends AppCompatActivity {
//    Button btnRegister,btnCancel;
//    EditText txtemail,txtpassword;
//    protected FirebaseAuth mFirebaseAuth;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_register);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
//        initView();
//        mFirebaseAuth = FirebaseAuth.getInstance();
//        btnRegister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mFirebaseAuth.createUserWithEmailAndPassword(txtemail.getText().toString(),txtpassword.getText().toString()).
//                        addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//                            @Override
//                            public void onSuccess(AuthResult authResult) {
//                                Toast.makeText(RegisterActivity.this,
//                                        "Register Success", Toast.LENGTH_SHORT).show();
//                                final Intent data = new Intent();
//                                // Truyền data vào intent
//                                data.putExtra("email", txtemail.getText().toString());
//                                data.putExtra("pass", txtpassword.getText().toString());
//                                // Đặt resultCode là Activity.RESULT_OK
//                                setResult(Activity.RESULT_OK, data);
//                                finish();
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(RegisterActivity.this, "Register Failed", Toast.LENGTH_LONG).show();
//                            }
//                        });
//            }
//        });
//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
//    }
//
//    private void initView() {
//        btnRegister=findViewById(R.id.btnRegister);
//        btnCancel=findViewById(R.id.btnCancelRegister);
//        txtemail=findViewById(R.id.inputEmailRegister);
//        txtpassword=findViewById(R.id.inputPasswordRegister);
//    }
//}
public class RegisterActivity extends AppCompatActivity {

    private EditText txtName, txtPhone, txtAddress, txtEmail, txtPassword, txtRPassword;
    private Button btnRegister, btnCancel;
    private TextView tvDangNhap;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    //    private DatabaseReference root_user = FirebaseDatabase.getInstance().getReference("user");
    private String username = "", password = "", repassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });



        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {
        txtName = findViewById(R.id.txtName);

        txtPhone = findViewById(R.id.txtPhone);
        txtAddress = findViewById(R.id.txtAddress);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        txtRPassword = findViewById(R.id.txtRPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnCancel = findViewById(R.id.btnCancel);

    }

    private void register(){
        String name = txtName.getText().toString();

        String sdt = txtPhone.getText().toString();
        String diaChi = txtAddress.getText().toString();
        username = txtEmail.getText().toString();
        password = txtPassword.getText().toString();
        repassword = txtRPassword.getText().toString();
        if(name.isEmpty() || sdt.isEmpty() || diaChi.isEmpty() ||
                username.isEmpty() || password.isEmpty() || repassword.isEmpty()){
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!password.equals(repassword)){
            Toast.makeText(this, "Nhập lại mật khẩu sai!", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(txtEmail.getText().toString(), txtPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            DatabaseReference root_user = FirebaseDatabase.getInstance().getReference("user");
            @Override
            public void onSuccess(AuthResult authResult) {
//                String userId = auth.getCurrentUser().getUid();
                String usernameChild = username.substring(0, username.length() - 4) + "-com";
                root_user.child(usernameChild).setValue(new User(name, sdt, diaChi, username)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(RegisterActivity.this, "Tạo tài khoản thành công!", Toast.LENGTH_SHORT).show();
                        final Intent data = new Intent();
                        data.putExtra("email", username);
                        data.putExtra("password", password);
                        setResult(RESULT_OK, data);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, "Tạo tài khoản không thành công!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, "Tạo tài khoản không thành công!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}