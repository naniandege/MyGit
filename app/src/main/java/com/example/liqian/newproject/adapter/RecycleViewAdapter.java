package com.example.liqian.newproject.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.liqian.newproject.R;
import com.example.liqian.newproject.bean.MyBean;

import java.util.ArrayList;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {

    private ArrayList<MyBean.DataDTO> list;
    private Context context;

    public RecycleViewAdapter(ArrayList<MyBean.DataDTO> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.recy_item, null));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        final MyBean.DataDTO meBean = list.get(i);
        Glide.with(context).load(meBean.getImagePath()).into(viewHolder.img);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemLisnner != null) {
                    onItemLisnner.onPosition(meBean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
        }
    }

    private onItemLisnner onItemLisnner;

    public void setOnItemLisnner(RecycleViewAdapter.onItemLisnner onItemLisnner) {
        this.onItemLisnner = onItemLisnner;
    }

    public interface onItemLisnner {
        void onPosition(MyBean.DataDTO dataDTO);
    }
}
