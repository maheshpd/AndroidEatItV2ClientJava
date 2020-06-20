package com.createsapp.androideatitv2clientjava.callback;

import com.createsapp.androideatitv2clientjava.model.CommentModel;

import java.util.List;

public interface ICommentCallbackListener {
    void onCommentLoadSuccess(List<CommentModel> commentModels);

    void onCommentLoadFailed(String message);
}
