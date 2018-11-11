package com.example.rose.caloreats;

import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import android.support.annotation.NonNull;

public class DynamicAdapter extends RecyclerView.Adapter<DynamicAdapter.DynamicViewHolder> {
    private Context context;
    ArrayList<Food> foodList;

    public class DynamicViewHolder extends RecyclerView.ViewHolder {
        Food food;
        TextView text;
        ImageView image;

        public DynamicViewHolder(View theView) {
            super(theView);

        }
    }

    public DynamicAdapter(RecyclerView rv, Context _context) {
        context = _context;


    }

    @NonNull
    @Override
    public DynamicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*View v = LayoutInflater.from(context).inflate(R.layout.pic_text_row, parent, false);
        final DynamicViewHolder vh = new DynamicViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(swipeDetector.swipeDetected() && swipeDetector.getAction() == SwipeDetector.Action.RL) {
                    if (swipeDetector.getAction() == SwipeDetector.Action.RL){
                        removeItem(vh.getAdapterPosition());

                    }
                } else {
                    Intent intent = new Intent(view.getContext(), OnePost.class);
                    intent.putExtra("title", mData.get(vh.getAdapterPosition()).title);
                    intent.putExtra("imageURL", mData.get(vh.getAdapterPosition()).imageURL);
                    view.getContext().startActivity(intent);
                }
            }
        });*/

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull DynamicViewHolder holder, int position) {

        TextView t = holder.text;
        ImageView iv = holder.image;

        t.setText(foodList.get(position).getName());

    }


    @Override
    public int getItemCount() {
        return foodList.size();
    }
}
