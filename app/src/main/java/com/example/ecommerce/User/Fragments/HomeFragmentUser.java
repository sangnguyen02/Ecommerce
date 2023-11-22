package com.example.ecommerce.User.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.EditText;

import com.example.ecommerce.Adapters.BannerViewPager2Adapter;
import com.example.ecommerce.Animations.ZoomOutPageTransformer;
import com.example.ecommerce.Models.ImageBanner;
import com.example.ecommerce.Models.Order;
import com.example.ecommerce.R;
import com.example.ecommerce.User.Activities.MapActivityUser;
import com.example.ecommerce.User.Activities.ChooseDestinationActivity;
import com.example.ecommerce.User.Activities.ChooseDestinationActivity;

import com.example.ecommerce.User.Activities.ChooseDestinationActivity;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator3;


public class HomeFragmentUser extends Fragment {

    private ViewPager2 mViewPager2;
    private CircleIndicator3 mCircleIndicator3;
    private List<ImageBanner> mListImageBanner;

    private EditText search;

    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mViewPager2.getCurrentItem() == mListImageBanner.size() - 1) {
                mViewPager2.setCurrentItem(0);
            } else {
                mViewPager2.setCurrentItem(mViewPager2.getCurrentItem() + 1);
            }

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        search = rootView.findViewById(R.id.editText_search);
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
                mHandler.postDelayed(mRunnable, 5000);
            }
        });

        Order order = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            order = this.getArguments().getSerializable("order", Order.class);
            Toast.makeText(rootView.getContext(), "Order Client no: "+  order.getClientNo(), Toast.LENGTH_SHORT).show();
        }

        mViewPager2.setPageTransformer(new ZoomOutPageTransformer());

        CircleImageView circleImageView = rootView.findViewById(R.id.motor_icon);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Your click event logic goes here
                Intent intent = new Intent(rootView.getContext(), ChooseDestinationActivity.class);
                startActivity(intent);
            }
        });
        CircleImageView circleImageView2 = rootView.findViewById(R.id.car_icon);
        circleImageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Your click event logic goes here
                Intent intent = new Intent(rootView.getContext(), ChooseDestinationActivity.class);
                startActivity(intent);
            }
        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootView.getContext(), ChooseDestinationActivity.class);
                startActivity(intent);
            }
        });

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
        mHandler.postDelayed(mRunnable, 5000);
    }
}