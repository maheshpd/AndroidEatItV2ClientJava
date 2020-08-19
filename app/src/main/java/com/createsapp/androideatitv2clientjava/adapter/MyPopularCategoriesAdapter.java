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
import com.createsapp.androideatitv2clientjava.callback.IRecyclerClickListener;
import com.createsapp.androideatitv2clientjava.eventbus.PopularCategoryClick;
import com.createsapp.androideatitv2clientjava.model.PopularCategoryModel;

import org.greenrobot.eventbus.EventBus;

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

        holder.setListener((view, position1) ->
                EventBus.getDefault().postSticky(new PopularCategoryClick(popularcategorymodelList.get(position1))));
    }

    @Override
    public int getItemCount() {
        return popularcategorymodelList.size();
    }

    public class PopularViewModel extends RecyclerView.ViewHolder implements View.OnClickListener {

        Unbinder unbinder;

        @BindView(R.id.txt_category_name)
        TextView txt_category_name;
        @BindView(R.id.category_image)
        CircleImageView category_image;

        IRecyclerClickListener listener;

        public PopularViewModel(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void setListener(IRecyclerClickListener listener) {
            this.listener = listener;
        }

        @Override
        public void onClick(View view) {
            listener.onItemClickListener(view, getAdapterPosition());
        }
    }
}

