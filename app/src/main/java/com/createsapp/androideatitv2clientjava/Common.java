package com.createsapp.androideatitv2clientjava;

import com.createsapp.androideatitv2clientjava.model.CategoryModel;
import com.createsapp.androideatitv2clientjava.model.FoodModel;
import com.createsapp.androideatitv2clientjava.model.UserModel;

public class Common {
    public static final String USER_REFERENCE = "Users";
    public static final String POPULAR_CATEGORY_REF = "MostPopular";
    public static final String BEST_DEAL_REF = "BestDeals";
    public static final int DEFAULT_COLUMN_COUNT = 0;
    public static final int FULL_WIDTH_COLUMN = 1;
    public static final String CATEGORY_REF = "Category";
    public static UserModel currentUser;
    public static CategoryModel categorySelected;
    public static FoodModel selectedFood;
}
