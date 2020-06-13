package com.createsapp.androideatitv2clientjava.callback;

import com.createsapp.androideatitv2clientjava.model.BestDealModel;
import com.createsapp.androideatitv2clientjava.model.CategoryModel;

import java.util.List;

public interface ICategoryCallbackListener {
    void onCategoryLoadSuccess(List<CategoryModel> categoryModels);
    void onCategoryLoadFailed(String message);
}
