package com.createsapp.androideatitv2clientjava.callback;

import com.createsapp.androideatitv2clientjava.model.PopularCategoryModel;

import java.util.List;

public interface IPopularCallbackListener {
    void onPopularLoadSuccess(List<PopularCategoryModel> popularCategoryMode);
    void onPopularLoadFailed(String message);
}
