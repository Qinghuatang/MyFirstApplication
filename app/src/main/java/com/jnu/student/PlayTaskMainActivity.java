package com.jnu.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.jnu.student.Count.CountFragment;
import com.jnu.student.Task.TaskDetailActivity;
import com.jnu.student.Task.TaskFragment;
import com.jnu.student.database.DBMaster;

import java.util.List;


public class PlayTaskMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView mNavigationView;
    private FragmentManager mFragmentManager;
    private Fragment[] fragments;
    private int lastFragment;

    private DrawerLayout Drawer;
    private NavigationView navigationView;

    public static DBMaster mDBMaster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mNavigationView = findViewById(R.id.main_navigation_bar);
        initFragment();
        initListener();

        Drawer = findViewById(R.id.home_id);
        navigationView =findViewById(R.id.nav);
//           设置item图标正常显示
        navigationView.setItemIconTintList(null);


        navigationView.setNavigationItemSelectedListener(this);


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

                //默认显示taskFragment
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

    @Override
    public void onBackPressed() {
        if (Drawer.isDrawerOpen(GravityCompat.START)) {
            Drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(R.id.nav_statistics == item.getItemId()){
            Intent intent = new Intent(this, StatisticsActivity.class);
            startActivity(intent);
        } else if (R.id.nav_bill == item.getItemId()) {
            Intent intent = new Intent(this, BillActivity.class);
            startActivity(intent);
        }
        return false;
    }
}