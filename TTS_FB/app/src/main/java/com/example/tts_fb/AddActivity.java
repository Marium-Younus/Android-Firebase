package com.example.tts_fb;

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

public class AddActivity extends AppCompatActivity {

    Button back,insert_buttton;
    EditText ins_name,ins_course,ins_email,ins_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        back = findViewById(R.id.btnback);
        insert_buttton = findViewById(R.id.btnadd);
        //----------------------------
        ins_name = findViewById(R.id.teachername);
        ins_course = findViewById(R.id.txtCourse);
        ins_email = findViewById(R.id.txtEmail);
        ins_img = findViewById(R.id.txtImage);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        insert_buttton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertall(); //call here
                clearall();
            }
        });
    }
     private  void  insertall()//function define here
     {
         Map<String,Object> mapinsdata = new HashMap<>();
         mapinsdata.put("name",ins_name.getText().toString());
         mapinsdata.put("course",ins_course.getText().toString());
         mapinsdata.put("email",ins_email.getText().toString());
         mapinsdata.put("turl",ins_img.getText().toString());

         FirebaseDatabase.getInstance().getReference().child("teacher").push().setValue(mapinsdata).
                 addOnSuccessListener(new OnSuccessListener<Void>() {
                     @Override
                     public void onSuccess(Void unused) {
                         Toast.makeText(AddActivity.this, "Record Inserted", Toast.LENGTH_SHORT).show();
                     }
                 }).addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception e) {
                         Toast.makeText(AddActivity.this, "Record Not Inserted", Toast.LENGTH_SHORT).show();
                     }
                 });
     }
     private void  clearall(){
        ins_name.setText("");
        ins_email.setText("");
        ins_course.setText("");
        ins_img.setText("");
     }


}