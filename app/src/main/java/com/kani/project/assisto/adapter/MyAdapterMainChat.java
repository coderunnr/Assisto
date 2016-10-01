package com.kani.project.assisto.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.DrawableContainer;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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


    @Override
    public void onBindViewHolder(ViewHolder holder,int position) {
        if(mDataset.get(position).isResponse())
        {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.weight = 1.0f;
            params.gravity = Gravity.RIGHT;
            holder.textView.setLayoutParams(params);

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                holder.textView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
//            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.textView.setBackground(context.getResources().getDrawable(R.drawable.chat_bubble_reply));
            }
        }
        holder.textView.setText(mDataset.get(position).getMessage());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}