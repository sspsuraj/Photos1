package com.p.suraj.photos1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Suraj on 9/6/2017.
 */

public class Recycler_adapter extends RecyclerView.Adapter<Recycler_adapter.myViewholder> {
    ArrayList<Image> arrayList = new ArrayList<>();
    Recycler_adapter(ArrayList<Image> arrayList){
        this.arrayList =arrayList;
    }
public static Context context ;
    @Override
    public myViewholder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout,parent,false);
        return new myViewholder(view );
    }

    @Override
    public void onBindViewHolder(myViewholder holder, final int position) {

        holder.c_flag.setImageBitmap(arrayList.get(position).getImage());
        holder.c_flag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),ViewImagefully.class);
               // intent.putExtra("a",arrayList);
                ViewImagefully.list = arrayList;
                ViewImagefully.position1 = position;
                v.getContext().startActivity(intent);

            }
        });
     //   holder.c_name.setText(arrayList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class myViewholder extends RecyclerView.ViewHolder{

        ImageView c_flag;
        TextView c_name;


        public myViewholder(View itemView) {
            super(itemView);
            c_flag = (ImageView) itemView.findViewById(R.id.imageView);
            c_name = (TextView)itemView.findViewById(R.id.name);


        }


    }


    public void setFilter(ArrayList<Image> newList)
    {
            arrayList = new ArrayList<>();
            arrayList.addAll(newList  );
            notifyDataSetChanged();
    }
}
