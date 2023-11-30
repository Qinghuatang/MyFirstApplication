package com.jnu.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jnu.student.Count.CountFragment;
import com.jnu.student.Task.TaskFragment;
import com.jnu.student.database.DBMaster;


public class PlayTaskMainActivity extends AppCompatActivity {
    private BottomNavigationView mNavigationView;
    private FragmentManager mFragmentManager;
    private Fragment[] fragments;
    private int lastFragment;

    public static DBMaster mDBMaster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("test", "PlayTaskOnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_task_main);
        mNavigationView = findViewById(R.id.main_navigation_bar);
        initFragment();
        initListener();

        //启动数据库
        mDBMaster = new DBMaster(getApplicationContext());
        mDBMaster.openDataBase();

    }

    private void initFragment() {
        TaskFragment taskFragment = TaskFragment.getInstance();
        AwardFragment awardFragment = new AwardFragment();
        CountFragment countFragment = new CountFragment();
        PersonalFragment personalFragment = new PersonalFragment();
        fragments = new Fragment[]{taskFragment, awardFragment, countFragment, personalFragment};
        mFragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        // 添加所有的Fragment
        fragmentTransaction.add(R.id.main_page_controller, taskFragment);
        fragmentTransaction.add(R.id.main_page_controller, awardFragment);
        fragmentTransaction.add(R.id.main_page_controller, countFragment);
        fragmentTransaction.add(R.id.main_page_controller, personalFragment);

        // 提交事务
        fragmentTransaction.commit();

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