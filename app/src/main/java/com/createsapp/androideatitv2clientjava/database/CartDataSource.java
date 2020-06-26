package com.createsapp.androideatitv2clientjava.database;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

public interface CartDataSource {
    Flowable<List<CartItem>> getAllCart(String uid);

    Single<Integer> countItemInCart(String uid);

    Single<Long> sumPriceInCart(String uid);

    Single<CartItem> getItemInCart(String foodId, String uid);

    Single<Integer> insertOrReplaceAll(CartItem cartItem);

    Single<Integer> updateCartItems(CartItem cartItem);


    Single<Integer> deleteCartItem(CartItem cartItem);

    Single<Integer> cleanCart(String uid);
}
