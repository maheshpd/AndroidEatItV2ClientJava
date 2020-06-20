package com.createsapp.androideatitv2clientjava.adapter;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.createsapp.androideatitv2clientjava.R;
import com.createsapp.androideatitv2clientjava.model.CommentModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyCommentAdapter extends RecyclerView.Adapter<MyCommentAdapter.CommentViewHolder> {

    Context context;
    List<CommentModel> commentModelsList;

    public MyCommentAdapter(Context context, List<CommentModel> commentModelsList) {
        this.context = context;
        this.commentModelsList = commentModelsList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_comment_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Long timeStamp = Long.valueOf(commentModelsList.get(position).getCommentTimeStamp().get("timeStamp").toString());
        holder.txt_comment_date.setText(DateUtils.getRelativeTimeSpanString(timeStamp));
        holder.txt_comment.setText(commentModelsList.get(position).getComment());
        holder.txt_comment_name.setText(commentModelsList.get(position).getName());
        holder.ratingBar.setRating(commentModelsList.get(position).getRatingValue());
    }

    @Override
    public int getItemCount() {
        return commentModelsList.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_comment_date)
        TextView txt_comment_date;
        @BindView(R.id.txt_comment)
        TextView txt_comment;
        @BindView(R.id.txt_comment_name)
        TextView txt_comment_name;
        @BindView(R.id.ratingBar)
        RatingBar ratingBar;
        private Unbinder unbinder;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);

            unbinder = ButterKnife.bind(this, itemView);
        }
    }
}
