package com.example.fb_crud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddMainActivity extends AppCompatActivity {

    EditText textname,textcourse,textemail,imageurl;
     Button back,add_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_main);
        back = findViewById(R.id.btnback);
        add_button = findViewById(R.id.btnadd);

        textname = findViewById(R.id.txtName);
        textcourse = findViewById(R.id.txtCourse);
        textemail = findViewById(R.id.txtEmail);
        imageurl = findViewById(R.id.txtImage);

        //--------------------- Add Record
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData();
                clearAll();
            }
        });




        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
    }


    private  void  insertData(){
        Map<String,Object> map = new HashMap<>();
        map.put("name",textname.getText().toString());
        map.put("course",textcourse.getText().toString());
        map.put("email",textemail.getText().toString());
        map.put("turl",imageurl.getText().toString());

        FirebaseDatabase.getInstance().getReference().child("teacher").push().setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AddMainActivity.this, "Data Inserted Successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddMainActivity.this, "Data Error While Inserting", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private  void  clearAll(){
        textname.setText("");
        textcourse.setText("");
        textemail.setText("");
        imageurl.setText("");
    }
}