package com.kani.project.assisto.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kani.project.assisto.MerchantActivity;
import com.kani.project.assisto.PaymentAPI;
import com.kani.project.assisto.R;
import com.kani.project.assisto.connectionutils.models.ChatModel;
import com.kani.project.assisto.connectionutils.models.DoctorsModel;

import java.util.List;

/**
 * Created by root on 1/10/16.
 */
public class MyAdapterDoctorPlace  extends RecyclerView.Adapter<MyAdapterDoctorPlace.ViewHolder> {

    List<DoctorsModel> mDataset;
    Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,vicinity,price;
        Button bookappointement;


        public ViewHolder(View v, MyAdapterDoctorPlace myAdapter) {
            super(v);
            name=(TextView)v.findViewById(R.id.text_doctor_name);
            vicinity=(TextView)v.findViewById(R.id.text_doctor_vicinity);
            price=(TextView)v.findViewById(R.id.text_doctor_price);
            bookappointement=(Button)v.findViewById(R.id.book_doctor);


        }
    }


    public MyAdapterDoctorPlace(List<DoctorsModel> myDataset, Context context) {
        mDataset = myDataset;
        this.context=context;


    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapterDoctorPlace.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_doctor, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v,this);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder,int position) {
            holder.name.setText(mDataset.get(position).getName());
            holder.price.setText(""+mDataset.get(position).getPrice());
            holder.vicinity.setText(mDataset.get(position).getVicinity());
        holder.bookappointement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, MerchantActivity.class);
                context.startActivity(intent);
            }
        });



    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}