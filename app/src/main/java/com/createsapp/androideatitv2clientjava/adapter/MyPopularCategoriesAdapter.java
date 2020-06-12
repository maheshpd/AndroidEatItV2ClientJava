package com.createsapp.androideatitv2clientjava.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.createsapp.androideatitv2clientjava.R;
import com.createsapp.androideatitv2clientjava.model.PopularCategoryModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class MyPopularCategoriesAdapter extends RecyclerView.Adapter<MyPopularCategoriesAdapter.PopularViewModel> {

    Context context;
    List<PopularCategoryModel> popularcategorymodelList;

    public MyPopularCategoriesAdapter(Context context, List<PopularCategoryModel> subject) {
        this.context = context;
        this.popularcategorymodelList = subject;
    }

    @NonNull
    @Override
    public PopularViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_popular_categories_item, parent, false);
        return new PopularViewModel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularViewModel holder, int position) {
        Glide.with(context).load(popularcategorymodelList.get(position).getImage())
                .into(holder.category_image);
        holder.txt_category_name.setText(popularcategorymodelList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return popularcategorymodelList.size();
    }

    public class PopularViewModel extends RecyclerView.ViewHolder {

        Unbinder unbinder;

        @BindView(R.id.txt_category_name)
        TextView txt_category_name;
        @BindView(R.id.category_image)
        CircleImageView category_image;
        public PopularViewModel(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);
        }
    }
}

