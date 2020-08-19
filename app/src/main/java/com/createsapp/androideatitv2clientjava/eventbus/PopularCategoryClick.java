package com.createsapp.androideatitv2clientjava.eventbus;

import com.createsapp.androideatitv2clientjava.model.PopularCategoryModel;

public class PopularCategoryClick {
    private PopularCategoryModel popularCategoryModel;

    public PopularCategoryClick(PopularCategoryModel popularCategoryModel) {
        this.popularCategoryModel = popularCategoryModel;
    }

    public PopularCategoryModel getPopularCategoryModel() {
        return popularCategoryModel;
    }

    public void setPopularCategoryModel(PopularCategoryModel popularCategoryModel) {
        this.popularCategoryModel = popularCategoryModel;
    }
}
