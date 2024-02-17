package com.example.tts_fb;

import android.annotation.SuppressLint;
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

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

// Now we register our myViewHolder class in MainAdapter
public class MainAdapter extends FirebaseRecyclerAdapter<MainModel,MainAdapter.myViewHolder> {

    public MainAdapter(@NonNull FirebaseRecyclerOptions<MainModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull MainModel model) {
        holder.n.setText(model.getName().toUpperCase());
        holder.c.setText(model.getCourse());
        holder.e.setText(model.getEmail());
        Glide.with(holder.image.getContext())
                .load(model.getTurl())
                .placeholder(R.drawable.ic_launcher_background)
                .circleCrop()
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.image);

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //dialog popup box
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.image.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_popup))
                        .setExpanded(true,1200).create();
                View view1 = dialogPlus.getHolderView();
                EditText x = view1.findViewById(R.id.teachername);
                EditText y = view1.findViewById(R.id.txtCourse);
                EditText z = view1.findViewById(R.id.txtEmail);
                EditText aurl = view1.findViewById(R.id.txtImage);

                x.setText(model.getName());
                y.setText(model.getCourse());
                z.setText(model.getEmail());
                aurl.setText(model.getTurl());
                dialogPlus.show();

                Button btnUpdate = view1.findViewById(R.id.btnalter);
                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ///------------------------------ update record work perform here
                        Map<String,Object> mapdata = new HashMap<>();
                        mapdata.put("name",x.getText().toString());
                        mapdata.put("course",y.getText().toString());
                        mapdata.put("email",z.getText().toString());
                        mapdata.put("turl",aurl.getText().toString());
                        FirebaseDatabase.getInstance().getReference().child("teacher").
                                child(getRef(position).getKey()).updateChildren(mapdata)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.n.getContext(), "update success", Toast.LENGTH_SHORT).show();
                                     dialogPlus.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(holder.n.getContext(), "update fail :(", Toast.LENGTH_SHORT).show();
                                    dialogPlus.dismiss();
                                   }
                                });
                    }
                });
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item,parent,false);
        return new myViewHolder(view);

    }

    class  myViewHolder extends RecyclerView.ViewHolder{

        CircleImageView image ;
        TextView n,c,e;
        Button btnEdit,btnDelete;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            image = (CircleImageView)itemView.findViewById(R.id.img1);
            n = (TextView) itemView.findViewById(R.id.nametext);
            c = (TextView) itemView.findViewById(R.id.coursetext);
            e = (TextView) itemView.findViewById(R.id.emailtext);


            //-----------------------------------popupwork
            btnEdit = (Button) itemView.findViewById(R.id.editbtn);
            btnDelete = (Button) itemView.findViewById(R.id.deletebtn);
            //---------------------------------------------
        }

}


}
