package com.createsapp.androideatitv2clientjava.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.createsapp.androideatitv2clientjava.R;
import com.createsapp.androideatitv2clientjava.callback.IRecyclerClickListener;
import com.createsapp.androideatitv2clientjava.common.Common;
import com.createsapp.androideatitv2clientjava.database.CartDataSource;
import com.createsapp.androideatitv2clientjava.database.CartDatabase;
import com.createsapp.androideatitv2clientjava.database.CartItem;
import com.createsapp.androideatitv2clientjava.database.LocalCartDataSource;
import com.createsapp.androideatitv2clientjava.eventbus.CounterCartEvent;
import com.createsapp.androideatitv2clientjava.eventbus.FoodItemClick;
import com.createsapp.androideatitv2clientjava.model.FoodModel;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MyFoodListAdapter extends RecyclerView.Adapter<MyFoodListAdapter.FoodListViewHolder> {

    Context context;
    List<FoodModel> foodlists;

    private CompositeDisposable compositeDisposable;
    private CartDataSource cartDataSource;

    public MyFoodListAdapter(Context context, List<FoodModel> subject) {
        this.context = context;
        this.foodlists = subject;
        this.compositeDisposable = new CompositeDisposable();
        this.cartDataSource = new LocalCartDataSource(CartDatabase.getInstance(context).cartDAO());
    }

    @NonNull
    @Override
    public FoodListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_food_item, parent, false);
        return new FoodListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodListViewHolder holder, int position) {
        Glide.with(context).load(foodlists.get(position).getImage()).into(holder.img_food_image);

        holder.txt_food_price.setText(new StringBuilder("$")
                .append(foodlists.get(position).getPrice()));

        holder.txt_food_name.setText(new StringBuilder()
                .append(foodlists.get(position).getName()));

        //Event
        holder.setListener(new IRecyclerClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Common.selectedFood = foodlists.get(position);
                Common.selectedFood.setKey(String.valueOf(position));
                EventBus.getDefault().postSticky(new FoodItemClick(true, foodlists.get(position)));
            }
        });

        holder.img_quick_cart.setOnClickListener(view -> {
            CartItem cartItem = new CartItem();
            cartItem.setUid(Common.currentUser.getUid());
            cartItem.setUserPhone(Common.currentUser.getPhone());

            cartItem.setFoodId(foodlists.get(position).getId());
            cartItem.setFoodName(foodlists.get(position).getName());
            cartItem.setFoodImage(foodlists.get(position).getImage());
            cartItem.setFoodPrice(Double.valueOf(String.valueOf(foodlists.get(position).getPrice())));
            cartItem.setFoodQuantity(1);
            cartItem.setFoodExtraPrice(0.0); //Because default we not choose size + addon so extra price is 0
            cartItem.setFoodAddon("Default");
            cartItem.setFoodSize("Default");

            cartDataSource.getItemWithAllOptionsInCart(Common.currentUser.getUid(),
                    cartItem.getFoodId(),
                    cartItem.getFoodSize(),
                    cartItem.getFoodAddon())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<CartItem>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(CartItem cartItemFromDB) {
                            if (cartItemFromDB.equals(cartItem)) {
                                //Already in database , just update
                                cartItemFromDB.setFoodExtraPrice(cartItem.getFoodExtraPrice());
                                cartItemFromDB.setFoodAddon(cartItem.getFoodAddon());
                                cartItemFromDB.setFoodSize(cartItem.getFoodSize());
                                cartItemFromDB.setFoodQuantity(cartItemFromDB.getFoodQuantity() + cartItem.getFoodQuantity());

                                cartDataSource.updateCartItems(cartItemFromDB)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new SingleObserver<Integer>() {
                                            @Override
                                            public void onSubscribe(Disposable d) {

                                            }

                                            @Override
                                            public void onSuccess(Integer integer) {
                                                Toast.makeText(context, "Update Cart success", Toast.LENGTH_SHORT).show();
                                                EventBus.getDefault().postSticky(new CounterCartEvent(true));
                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                                Toast.makeText(context, "[UPDATE CART]", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            } else {
                                //Item not available in cart before , unsert new
                                compositeDisposable.add(cartDataSource.insertOrReplaceAll(cartItem)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(() -> {
                                            Toast.makeText(context, "Add to Cart success", Toast.LENGTH_SHORT).show();
                                            EventBus.getDefault().postSticky(new CounterCartEvent(true));
                                        }, throwable -> {
                                            Toast.makeText(context, "[CART ERROR]" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                        }));
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (e.getMessage().contains("empty")) {
                                //Default, If Cart is empty , this code will be fired
                                compositeDisposable.add(cartDataSource.insertOrReplaceAll(cartItem)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(() -> {
                                            Toast.makeText(context, "Add to Cart success", Toast.LENGTH_SHORT).show();
                                            EventBus.getDefault().postSticky(new CounterCartEvent(true));
                                        }, throwable -> {
                                            Toast.makeText(context, "[CART ERROR]" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                        }));
                            } else

                                Toast.makeText(context, "[GET CART]", Toast.LENGTH_SHORT).show();
                        }
                    });

        });

    }

    @Override
    public int getItemCount() {
        return foodlists.size();
    }

    public class FoodListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Unbinder unbinder;
        @BindView(R.id.txt_food_name)
        TextView txt_food_name;
        @BindView(R.id.txt_food_price)
        TextView txt_food_price;
        @BindView(R.id.img_food_image)
        ImageView img_food_image;
        @BindView(R.id.img_fav)
        ImageView img_fav;
        @BindView(R.id.img_quick_cart)
        ImageView img_quick_cart;

        IRecyclerClickListener listener;

        public void setListener(IRecyclerClickListener listener) {
            this.listener = listener;
        }

        public FoodListViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClickListener(v, getAdapterPosition());
        }
    }
}

