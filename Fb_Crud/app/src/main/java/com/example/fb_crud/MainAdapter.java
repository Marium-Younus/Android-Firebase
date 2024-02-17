package com.example.fb_crud;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainAdapter extends FirebaseRecyclerAdapter<MainModel,MainAdapter.myViewHolder> {

    public MainAdapter(@NonNull FirebaseRecyclerOptions<MainModel> options) {
        super(options);
    }
    @SuppressLint("RecyclerView")
    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull MainModel model) {
        holder.name.setText(model.getName().toUpperCase());
        holder.course.setText(model.getCourse());
        holder.email.setText(model.getEmail());
        Glide.with(holder.img.getContext())
                .load(model.getTurl())
                .placeholder(R.drawable.ic_launcher_background)
                .circleCrop()
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.img);
   //-------------------------------------Click Edit Button
    holder.btnEdit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final DialogPlus dialogPlus = DialogPlus.newDialog(holder.img.getContext())
                    .setContentHolder(new ViewHolder(R.layout.update_popup))
                            .setExpanded(true,1200).create();
            //---------------------------------------------dialogPlus.show();
            View view1 = dialogPlus.getHolderView();
            EditText name = view1.findViewById(R.id.txtName);
            EditText course = view1.findViewById(R.id.txtCourse);
            EditText email = view1.findViewById(R.id.txtEmail);
            EditText turl = view1.findViewById(R.id.txtImage);

            Button btnUpdate = view1.findViewById(R.id.btnUpdate);
            name.setText(model.getName());
            course.setText(model.getCourse());
            email.setText(model.getEmail());
            turl.setText(model.getTurl());
            dialogPlus.show();
            //----------------------------------------------------------------------
            //--------------------------------------------------------------update work
            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Map<String,Object> map = new HashMap<>();
                    map.put("name",name.getText().toString());
                    map.put("course",course.getText().toString());
                    map.put("email",email.getText().toString());
                    map.put("turl",turl.getText().toString());
                    FirebaseDatabase.getInstance().getReference().child("teacher")
                            .child(getRef(position).getKey()).updateChildren(map)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(holder.name.getContext(), "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                                    dialogPlus.dismiss();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(holder.name.getContext(), "Data Error While Updating", Toast.LENGTH_SHORT).show();
                                    dialogPlus.dismiss();
                                }
                            });
                }
            });
        }
    });

    //-----------------------------------------Delete Work

    holder.btnDelete.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(holder.name.getContext());
            builder.setTitle("Are You Sure?");
            builder.setMessage("Deleted data can't be Undo.");

            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    FirebaseDatabase.getInstance().getReference().child("teacher").child(getRef(position).getKey()).removeValue();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(holder.name.getContext(), "Cancelled.", Toast.LENGTH_SHORT).show();
                }
            });
            builder.show();
        }
    });

    //--------------------------------------------------------
    }
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item,parent,false);
        return new myViewHolder(view);
    }
    class myViewHolder extends RecyclerView.ViewHolder{
        CircleImageView img;
        TextView name,course,email;

        //-----------------------------------popupwork
        Button btnEdit,btnDelete;
        //-----------------------------------------

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            img = (CircleImageView) itemView.findViewById(R.id.img1);
            name = (TextView) itemView.findViewById(R.id.nametext);
            course = (TextView) itemView.findViewById(R.id.coursetext);
            email = (TextView) itemView.findViewById(R.id.emailtext);

          //-----------------------------------popupwork
            btnEdit = (Button) itemView.findViewById(R.id.editbtn);
            btnDelete = (Button) itemView.findViewById(R.id.deletebtn);
         //---------------------------------------------

        }
    }
}