package com.kani.project.assisto.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kani.project.assisto.R;
import com.kani.project.assisto.connectionutils.models.ChatModel;


import java.util.List;



/**
 * Created by my hp on 3/13/2016.
 */
public class MyAdapterMainChat extends RecyclerView.Adapter<MyAdapterMainChat.ViewHolder> {

    List<ChatModel> mDataset;
    Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public ViewHolder(View v, MyAdapterMainChat myAdapter) {
            super(v);
            textView=(TextView)v.findViewById(R.id.textview_chat);


        }
    }


    public MyAdapterMainChat(List<ChatModel> myDataset, Context context) {
        mDataset = myDataset;
this.context=context;


    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapterMainChat.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_chat, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v,this);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder,int position) {

        holder.textView.setText(""+mDataset.get(position).getI());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}