package com.createsapp.androideatitv2clientjava.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.createsapp.androideatitv2clientjava.Common;
import com.createsapp.androideatitv2clientjava.R;
import com.createsapp.androideatitv2clientjava.callback.IRecyclerClickListener;
import com.createsapp.androideatitv2clientjava.eventbus.FoodItemClick;
import com.createsapp.androideatitv2clientjava.model.FoodModel;


import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyFoodListAdapter extends RecyclerView.Adapter<MyFoodListAdapter.FoodListViewHolder> {

    Context context;
    List<FoodModel> foodlists;

    public MyFoodListAdapter(Context context, List<FoodModel> subject) {
        this.context = context;
        this.foodlists = subject;
    }

    @NonNull
    @Override
    public FoodListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_food_item, parent, false);
        return new FoodListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodListViewHolder holder, int position) {
        Glide.with(context).load(foodlists.get(position).getImage()).into(holder.img_food_image);

        holder.txt_food_price.setText(new StringBuilder("$")
                .append(foodlists.get(position).getPrice()));

        holder.txt_food_name.setText(new StringBuilder("")
                .append(foodlists.get(position).getName()));

        //Event
        holder.setListener(new IRecyclerClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Common.selectedFood = foodlists.get(position);
                EventBus.getDefault().postSticky(new FoodItemClick(true, foodlists.get(position)));
            }
        });

    }

    @Override
    public int getItemCount() {
        return foodlists.size();
    }

    public class FoodListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Unbinder unbinder;
        @BindView(R.id.txt_food_name)
        TextView txt_food_name;
        @BindView(R.id.txt_food_price)
        TextView txt_food_price;
        @BindView(R.id.img_food_image)
        ImageView img_food_image;
        @BindView(R.id.img_fav)
        ImageView img_fav;
        @BindView(R.id.img_quick_cart)
        ImageView img_quick_cart;

        IRecyclerClickListener listener;

        public void setListener(IRecyclerClickListener listener) {
            this.listener = listener;
        }

        public FoodListViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClickListener(v, getAdapterPosition());
        }
    }
}

