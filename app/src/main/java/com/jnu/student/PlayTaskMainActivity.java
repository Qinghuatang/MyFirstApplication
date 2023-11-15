package com.jnu.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class PlayTaskMainActivity extends AppCompatActivity {
    private BottomNavigationView mNavigationView;

    private FragmentManager mFragmentManager;

    private Fragment[] fragments;
    private int lastFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_task_main);
        mNavigationView = findViewById(R.id.main_navigation_bar);
        initFragment();
        initListener();
    }

    private void initFragment() {
        TaskFragment taskFragment = new TaskFragment();
        AwardFragment awardFragment = new AwardFragment();
        CountFragment countFragment = new CountFragment();
        PersonalFragment personalFragment = new PersonalFragment();
        fragments = new Fragment[]{taskFragment, awardFragment, countFragment, personalFragment};
        mFragmentManager = getSupportFragmentManager();
        //默认显示HomeFragment
        mFragmentManager.beginTransaction()
                .replace(R.id.main_page_controller, taskFragment)
                .show(taskFragment)
                .commit();

        // 禁用所选项目图标的色调效果，可以更改底部导航栏菜单的图标
        mNavigationView.setItemIconTintList(null);

    }

    private void initListener() {
        mNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (R.id.task == item.getItemId()) {
                    if (lastFragment != 0) {
                        PlayTaskMainActivity.this.switchFragment(lastFragment, 0);
                        lastFragment = 0;
                    }
                    return true;
                } else if (R.id.award == item.getItemId()) {
                    if (lastFragment != 1) {
                        PlayTaskMainActivity.this.switchFragment(lastFragment, 1);
                        lastFragment = 1;
                    }
                    return true;
                } else if (R.id.count == item.getItemId()) {
                    if (lastFragment != 2) {
                        PlayTaskMainActivity.this.switchFragment(lastFragment, 2);
                        lastFragment = 2;
                    }
                    return true;
                } else if (R.id.personal == item.getItemId()) {
                    if (lastFragment != 3) {
                        PlayTaskMainActivity.this.switchFragment(lastFragment, 3);
                        lastFragment = 3;
                    }
                    return true;
                }

                return false;
            }
        });
    }

    private void switchFragment(int lastFragment, int index) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.hide(fragments[lastFragment]);
        if (!fragments[index].isAdded()) {
            transaction.add(R.id.main_page_controller, fragments[index]);
        }
        transaction.show(fragments[index]).commitAllowingStateLoss();
    }

}