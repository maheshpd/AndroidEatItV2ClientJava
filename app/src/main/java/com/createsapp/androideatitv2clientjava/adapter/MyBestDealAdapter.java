package com.createsapp.androideatitv2clientjava.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asksira.loopingviewpager.LoopingPagerAdapter;
import com.bumptech.glide.Glide;
import com.createsapp.androideatitv2clientjava.R;
import com.createsapp.androideatitv2clientjava.eventbus.BestDealItemClick;
import com.createsapp.androideatitv2clientjava.model.BestDealModel;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyBestDealAdapter extends LoopingPagerAdapter<BestDealModel> {

    @BindView(R.id.img_best_deal)
    ImageView img_best_deal;

    @BindView(R.id.txt_best_deal)
    TextView txt_best_deal;

    Unbinder unbinder;

    public MyBestDealAdapter(Context context, List<BestDealModel> itemList, boolean isInfinite) {
        super(context, itemList, isInfinite);
    }

    @Override
    protected View inflateView(int viewType, ViewGroup container, int listPosition) {
        return LayoutInflater.from(context).inflate(R.layout.layout_best_deals_item, container, false);
    }

    @Override
    protected void bindView(View convertView, int listPosition, int viewType) {

        unbinder = ButterKnife.bind(this, convertView);

        //Set data
        Glide.with(convertView).load(itemList.get(listPosition).getImage()).into(img_best_deal);
        txt_best_deal.setText(itemList.get(listPosition).getName());

        convertView.setOnClickListener(view ->
                EventBus.getDefault().postSticky(new BestDealItemClick(itemList.get(listPosition))));

    }
}
