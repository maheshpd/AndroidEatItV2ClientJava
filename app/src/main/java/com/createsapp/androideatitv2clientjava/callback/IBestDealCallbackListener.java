package com.createsapp.androideatitv2clientjava.callback;

import com.createsapp.androideatitv2clientjava.model.BestDealModel;
import com.createsapp.androideatitv2clientjava.model.PopularCategoryModel;

import java.util.List;

public interface IBestDealCallbackListener {
    void onBestDealLoadSuccess(List<BestDealModel> bestDealModels);
    void onBestDealLoadFailed(String message);
}
