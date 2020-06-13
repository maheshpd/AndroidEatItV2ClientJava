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
import com.createsapp.androideatitv2clientjava.eventbus.CategoryClick;
import com.createsapp.androideatitv2clientjava.model.CategoryModel;


import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyCategoryAdapter extends RecyclerView.Adapter<MyCategoryAdapter.MyCategoryViewHolder> {

    Context context;
    List<CategoryModel> categoryModelArrayList;

    public MyCategoryAdapter(Context context, List<CategoryModel> subject) {
        this.context = context;
        this.categoryModelArrayList = subject;
    }

    @NonNull
    @Override
    public MyCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_category_item, parent, false);
        return new MyCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyCategoryViewHolder holder, int position) {
        Glide.with(context).load(categoryModelArrayList.get(position).getImage()).into(holder.category_image);
        holder.category_name.setText(new StringBuilder(categoryModelArrayList.get(position).getName()));

        //Event
        holder.setListener((view, pos) -> {
            Common.categorySelected = categoryModelArrayList.get(pos);
            EventBus.getDefault().postSticky(new CategoryClick(true,categoryModelArrayList.get(pos)));
        });
    }

    @Override
    public int getItemCount() {
        return categoryModelArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (categoryModelArrayList.size() == 1)
            return Common.DEFAULT_COLUMN_COUNT;
        else {
            if (categoryModelArrayList.size() % 2 == 0)
                return Common.DEFAULT_COLUMN_COUNT;
            else {
                return (position > 1 && position == categoryModelArrayList.size() - 1) ? Common.FULL_WIDTH_COLUMN : Common.DEFAULT_COLUMN_COUNT;
            }
        }
    }

    public class MyCategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Unbinder unbinder;
        @BindView(R.id.category_image)
        ImageView category_image;
        @BindView(R.id.category_name)
        TextView category_name;

        IRecyclerClickListener listener;

        public void setListener(IRecyclerClickListener listener) {
            this.listener = listener;
        }

        public MyCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClickListener(v,getAdapterPosition());
        }
    }
}

