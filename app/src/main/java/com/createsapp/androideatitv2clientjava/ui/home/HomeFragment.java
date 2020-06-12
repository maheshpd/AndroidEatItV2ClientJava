package com.createsapp.androideatitv2clientjava.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.asksira.loopingviewpager.LoopingViewPager;
import com.createsapp.androideatitv2clientjava.R;
import com.createsapp.androideatitv2clientjava.adapter.MyBestDealAdapter;
import com.createsapp.androideatitv2clientjava.adapter.MyPopularCategoriesAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    Unbinder unbinder;

    @BindView(R.id.recycler_popular)
    RecyclerView recycler_popular;

    @BindView(R.id.viewPager)
    LoopingViewPager viewPager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, root);

        init();

        homeViewModel.getPopularList().observe(this, popularCategoryModelList -> {
            MyPopularCategoriesAdapter adapter = new MyPopularCategoriesAdapter(getContext(), popularCategoryModelList);
            recycler_popular.setAdapter(adapter);
        });

        homeViewModel.getBestDealList().observe(this, bestDealModels -> {
            MyBestDealAdapter adapter = new MyBestDealAdapter(getContext(), bestDealModels, true);
            viewPager.setAdapter(adapter);
        });
        return root;
    }

    private void init() {
        recycler_popular.setHasFixedSize(true);
        recycler_popular.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
    }

    @Override
    public void onResume() {
        super.onResume();
        viewPager.resumeAutoScroll();
    }

    @Override
    public void onPause() {
        viewPager.pauseAutoScroll();
        super.onPause();
    }
}