package com.jnu.student.Task;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;


import android.util.Log;
import android.view.LayoutInflater;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.jnu.student.PlayTaskMainActivity;
import com.jnu.student.R;
import com.jnu.student.data.Task;


import java.sql.Timestamp;


// 修改了原来的MainActivity
public class TaskFragment extends Fragment {
    private static TaskFragment taskFragment;
    private String[] tabHeaderStrings = {"每日任务", "每周任务", "普通任务"};
    private ActivityResultLauncher<Intent> addItemLauncher;
    Toolbar task_toolbar;

    private DailyTaskFragment dailyTaskFragment;
    private WeeklyTaskFragment weeklyTaskFragment;
    private CommonTaskFragment commonTaskFragment;

    private TaskFragment() {
        // Required empty public constructor
    }

    public static TaskFragment getInstance() {
        if (taskFragment == null) {
            taskFragment = new TaskFragment();
        }
        return taskFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("test", "TaskFragmentOnCreate");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        dailyTaskFragment = DailyTaskFragment.getInstance();
        weeklyTaskFragment = WeeklyTaskFragment.getInstance();
        commonTaskFragment = CommonTaskFragment.getInstance();

        dailyTaskFragment.setDailyTaskList(PlayTaskMainActivity.mDBMaster.mTaskDBDao.queryTaskByType("Daily"));
        weeklyTaskFragment.setWeeklyTaskList(PlayTaskMainActivity.mDBMaster.mTaskDBDao.queryTaskByType("Weekly"));
        commonTaskFragment.setCommonTaskList(PlayTaskMainActivity.mDBMaster.mTaskDBDao.queryTaskByType("Common"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_task, container, false);

        // 获取ViewPager2和TabLayout的实例
        ViewPager2 viewPager = rootView.findViewById(R.id.task_view_pager);
        TabLayout tabLayout = rootView.findViewById(R.id.task_tab_layout);

        // 创建适配器
        TaskFragment.FragmentAdapter fragmentAdapter = new TaskFragment.FragmentAdapter(getActivity().getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(fragmentAdapter);


        // 将TabLayout和ViewPager2进行关联
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(tabHeaderStrings[position])
        ).attach();

        // 注意要设置为类的成员变量，否则程序报错
        task_toolbar = rootView.findViewById(R.id.task_toolbar);
        // 为标题栏的右侧菜单添加监听器响应函数
        task_toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (R.id.create_new_task == item.getItemId()) {
                    Intent addIntent = new Intent(getActivity(), TaskDetailActivity.class);
                    addItemLauncher.launch(addIntent);
                }
                return true;
            }
        });

        TextView tv_point = rootView.findViewById(R.id.tv_point);
        int pointSum = PlayTaskMainActivity.mDBMaster.mCountDBDao.queryPointSum();
        tv_point.setText(String.valueOf(pointSum));


        addItemLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Intent data = result.getData();
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Bundle bundle = data.getExtras();

                        // 处理返回的数据
                        bundle.putString("operate", "add");

                        String taskType = bundle.getString("taskType");
                        Timestamp time = new Timestamp(System.currentTimeMillis());
                        String content = bundle.getString("content");
                        int point = bundle.getInt("point");
                        int finishedNum = 0;
                        int num = bundle.getInt("num");
                        String classification = bundle.getString("classification");

                        Task newTask = new Task(0, taskType, time, content, point, finishedNum, num, classification);
//                        Log.d("newTask", newTask.toString());

                        PlayTaskMainActivity.mDBMaster.mTaskDBDao.insertTask(newTask);
                        newTask.setId(PlayTaskMainActivity.mDBMaster.mTaskDBDao.getId());

//                        Log.d("newTask", String.valueOf(newTask.getId()));

                        switch (taskType) {
                            case "Daily":
                                // 无需再给dailyTaskList重新赋值
                                // dailyTaskFragment.setDailyTaskList(taskDBHelper.queryByType("Daily"));
                                dailyTaskFragment.InsertList(newTask);
                                break;
                            case "Weekly":
                                // weeklyTaskFragment.setWeeklyTaskList(taskDBHelper.queryByType("Weekly"));
                                weeklyTaskFragment.InsertList(newTask);
                                break;
                            case "Common":
                                // commonTaskFragment.setCommonTaskList(taskDBHelper.queryByType("Common"));
                                commonTaskFragment.InsertList(newTask);
                                break;
                        }
                    }

                });

        return rootView;
    }

    public class FragmentAdapter extends FragmentStateAdapter {
        private static final int NUM_TABS = 3;

        public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            // 根据位置返回对应的Fragment实例
            switch (position) {
                case 0:
                    return dailyTaskFragment;
                case 1:
                    return weeklyTaskFragment;
                case 2:
                    return commonTaskFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getItemCount() {
            return NUM_TABS;
        }
    }

    public void updatePoint(){
        TextView tv_point = getActivity().findViewById(R.id.tv_point);
        int pointSum = PlayTaskMainActivity.mDBMaster.mCountDBDao.queryPointSum();
        tv_point.setText(String.valueOf(pointSum));
    }
}