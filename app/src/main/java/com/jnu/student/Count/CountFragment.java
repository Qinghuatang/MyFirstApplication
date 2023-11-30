package com.jnu.student.Count;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.jnu.student.PlayTaskMainActivity;
import com.jnu.student.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CountFragment extends Fragment {
    private static CountFragment instance;
    private String[] tabHeaderStrings = {"日", "周", "月", "年"};
    Toolbar task_toolbar;

    private DayCountFragment dayCountFragment;
    private WeekCountFragment weekCountFragment;
    private MonthCountFragment monthCountFragment;
    private YearCountFragment yearCountFragment;

    public CountFragment() {
        // Required empty public constructor
    }

    public static CountFragment getInstance() {
        if (instance == null)
            instance = new CountFragment();
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("test", "CountFragmentOnCreate");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());
        dayCountFragment = DayCountFragment.getInstance();
        dayCountFragment.setDayCountList(PlayTaskMainActivity.mDBMaster.mCountDBDao.queryDataListByDate(date));

        weekCountFragment = new WeekCountFragment();
        monthCountFragment = new MonthCountFragment();
        yearCountFragment = new YearCountFragment();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_count, container, false);

        // 获取ViewPager2和TabLayout的实例
        ViewPager2 viewPager = rootView.findViewById(R.id.count_view_pager);
        TabLayout tabLayout = rootView.findViewById(R.id.count_tab_layout);

        // 创建适配器
        CountFragment.FragmentAdapter fragmentAdapter = new CountFragment.FragmentAdapter(getActivity().getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setOffscreenPageLimit(4);


        // 将TabLayout和ViewPager2进行关联
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(tabHeaderStrings[position])
        ).attach();




        return rootView;
    }

    public class FragmentAdapter extends FragmentStateAdapter {
        private static final int NUM_TABS = 4;

        public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            // 根据位置返回对应的Fragment实例
            switch (position) {
                case 0:
                    return dayCountFragment;
                case 1:
                    return weekCountFragment;
                case 2:
                    return monthCountFragment;
                case 3:
                    return yearCountFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getItemCount() {
            return NUM_TABS;
        }
    }
}