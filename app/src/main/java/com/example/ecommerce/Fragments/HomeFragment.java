package com.example.ecommerce.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ecommerce.Adapters.BannerViewPager2Adapter;
import com.example.ecommerce.Animations.ZoomOutPageTransformer;
import com.example.ecommerce.Models.ImageBanner;
import com.example.ecommerce.R;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;


public class HomeFragment extends Fragment {

    private ViewPager2 mViewPager2;
    private CircleIndicator3 mCircleIndicator3;
    private List<ImageBanner> mListImageBanner;

    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if(mViewPager2.getCurrentItem() == mListImageBanner.size() - 1) {
                mViewPager2.setCurrentItem(0);
            }
            else {
                mViewPager2.setCurrentItem(mViewPager2.getCurrentItem() + 1);
            }

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        mViewPager2 = rootView.findViewById(R.id.view_pager_2);
        mCircleIndicator3 = rootView.findViewById(R.id.circle_indicator_3);

        mListImageBanner = getListImageBanner();
        BannerViewPager2Adapter adapter = new BannerViewPager2Adapter(mListImageBanner);
        mViewPager2.setAdapter(adapter);

        mCircleIndicator3.setViewPager(mViewPager2);

        mViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mHandler.removeCallbacks(mRunnable);
                mHandler.postDelayed(mRunnable,5000);
            }
        });

        mViewPager2.setPageTransformer(new ZoomOutPageTransformer());

        return rootView;
    }

    private List<ImageBanner> getListImageBanner() {
        List<ImageBanner> list = new ArrayList<>();
        list.add(new ImageBanner(R.drawable.banner_ggmap));
        list.add(new ImageBanner(R.drawable.banner_vnpay));
        list.add(new ImageBanner(R.drawable.banner_voucher));
        list.add(new ImageBanner(R.drawable.banner_giveaway));

        return list;
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        mHandler.postDelayed(mRunnable,5000);
    }
}