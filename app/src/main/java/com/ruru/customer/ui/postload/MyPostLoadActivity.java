package com.ruru.customer.ui.postload;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ruru.customer.R;
import com.ruru.customer.ui.BaseActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyPostLoadActivity extends BaseActivity {

    @BindView(R.id.img_back)
    public ImageView imgBack;
    @BindView(R.id.tab)
    public TabLayout mTabs;
    @BindView(R.id.viewPager)
    public ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post_load);
        ButterKnife.bind(this);


        TabFragmentAdapter adapter = new TabFragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(CurrentPostLoadFragment.newInstance(), getString(R.string.mycurrentloads));
        adapter.addFragment(CompletPostLoadFragment.newInstance(), getString(R.string.completed));
        mViewPager.setAdapter(adapter);
        mTabs.setupWithViewPager(mViewPager);


    }

    @OnClick({R.id.img_back})

    public void onBindClick(View view) {
        if (view.getId() == R.id.img_back) {
            finish();
        }
    }

    public class TabFragmentAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public TabFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            if(position == 0) return new CurrentPostLoadFragment();
            if(position == 1) return new CompletPostLoadFragment();

            return null;
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }
    }
}