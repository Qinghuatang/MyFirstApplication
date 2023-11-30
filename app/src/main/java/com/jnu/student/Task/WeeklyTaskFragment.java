package com.jnu.student.Task;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jnu.student.Count.DayCountFragment;
import com.jnu.student.OnItemClickListener;
import com.jnu.student.PlayTaskMainActivity;
import com.jnu.student.R;
import com.jnu.student.data.Count;
import com.jnu.student.data.Task;


import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

public class WeeklyTaskFragment extends Fragment {
    private static WeeklyTaskFragment weeklyTaskFragment;
    public static final int MENU_ID_DELETE = 2;

    private ActivityResultLauncher<Intent> updateItemLauncher;
    private List<Task> weeklyTaskList;
    private RecycleViewTaskAdapater adapter;

    public RecycleViewTaskAdapater getAdapter() {
        return adapter;
    }

    public void setWeeklyTaskList(List<Task> weeklyTaskList) {
        this.weeklyTaskList = weeklyTaskList;
    }

    private WeeklyTaskFragment() {
    }

    public static WeeklyTaskFragment getInstance() {
        if (weeklyTaskFragment == null) {
            weeklyTaskFragment = new WeeklyTaskFragment();
        }
        return weeklyTaskFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_weekly_task, container, false);

        RecyclerView recycle_view_weekly_task = rootView.findViewById(R.id.recycle_view_weekly_task);
        recycle_view_weekly_task.setLayoutManager(new LinearLayoutManager(requireActivity())); // 设置布局管理器

        recycle_view_weekly_task.addItemDecoration(new DividerItemDecoration
                (getContext(),DividerItemDecoration.VERTICAL));// 添加分割线

        adapter = new RecycleViewTaskAdapater(weeklyTaskList);
        recycle_view_weekly_task.setAdapter(adapter);
        registerForContextMenu(recycle_view_weekly_task);     // 创建场景菜单事件

//        TextView tv_point = rootView.findViewById(R.id.tv_point);
//        int pointSum = PlayTaskMainActivity.mDBMaster.mCountDBDao.queryPointSum();
//        tv_point.setText(String.valueOf(pointSum));

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent updateIntent = new Intent(getContext(), TaskDetailActivity.class);
                // 创建一个新包裹
                Bundle bundle = new Bundle();
                bundle.putString("operate", "update");
                bundle.putInt("position", position);

                bundle.putString("content", weeklyTaskList.get(position).getContent());
                bundle.putInt("point", weeklyTaskList.get(position).getPoint());
                bundle.putInt("num", weeklyTaskList.get(position).getNum());
                bundle.putString("classification", weeklyTaskList.get(position).getClassification());

                updateIntent.putExtras(bundle);        // 把快递包裹塞给意图
                updateItemLauncher.launch(updateIntent);

                // Toast.makeText(getContext(), dailyTaskList.get(position).getContent(), Toast.LENGTH_SHORT).show();
            }
        });

        updateItemLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (null != result) {
                        Intent data = result.getData();
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Bundle bundle = data.getExtras();

                            int position = bundle.getInt("position");

                            int id = weeklyTaskList.get(position).getId();
                            String taskType = "Weekly";
                            Timestamp time = new Timestamp(System.currentTimeMillis());
                            String content = bundle.getString("content");
                            int point = bundle.getInt("point");
                            int finishedNum = 0;
                            int num = bundle.getInt("num");
                            String classification = bundle.getString("classification");

                            Task task = new Task(id, taskType, time, content, point, finishedNum, num, classification);

                            PlayTaskMainActivity.mDBMaster.mTaskDBDao.updateTask(task);

                            adapter.getTaskItemArrayList().set(position, task);
                            adapter.notifyItemChanged(position);
                        }
                    }

                });


        return rootView;
    }

    public class RecycleViewTaskAdapater extends RecyclerView.Adapter<WeeklyTaskFragment.RecycleViewTaskAdapater.ViewHolder> {
        private List<Task> taskItemArrayList;
        private OnItemClickListener mOnItemClickListener;

        public RecycleViewTaskAdapater(List<Task> taskItemArrayList) {
            this.taskItemArrayList = taskItemArrayList;
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            mOnItemClickListener = onItemClickListener;
        }

        public void setTaskItemArrayList(List<Task> taskItemArrayList) {
            this.taskItemArrayList = taskItemArrayList;
        }

        public List<Task> getTaskItemArrayList() {
            return taskItemArrayList;
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
            private final CheckBox cb_weekly_task;
            private final TextView tv_weekly_task_content;
            private final TextView tv_weekly_task_point;
            private final TextView tv_weekly_task_num;

            @Override
            public void onCreateContextMenu(ContextMenu menu, View v,
                                            ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("具体操作");
                menu.add(0, MENU_ID_DELETE, this.getAdapterPosition(), "删除");

            }

            public ViewHolder(View itemView) {
                super(itemView);
                // Define click listener for the ViewHolder's View
                cb_weekly_task = (CheckBox) itemView.findViewById(R.id.cb_weekly_task);
                tv_weekly_task_content = (TextView) itemView.findViewById(R.id.tv_weekly_task_content);
                tv_weekly_task_point = (TextView) itemView.findViewById(R.id.tv_weekly_task_point);
                tv_weekly_task_num = (TextView) itemView.findViewById(R.id.tv_weekly_task_num);
                itemView.setOnCreateContextMenuListener(this);     // 创建一个上下文场景菜单监听器

            }

            public CheckBox getWeeklyTaskFinish() {
                return cb_weekly_task;
            }

            public TextView getWeeklyTaskContent() {
                return tv_weekly_task_content;
            }

            public TextView getWeeklyTaskPoint() {
                return tv_weekly_task_point;
            }

            public TextView getWeeklyTaskNum() {
                return tv_weekly_task_num;
            }

        }


        @NonNull
        @Override
        public WeeklyTaskFragment.RecycleViewTaskAdapater.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.weekly_task_item_row, viewGroup, false);

            return new WeeklyTaskFragment.RecycleViewTaskAdapater.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull WeeklyTaskFragment.RecycleViewTaskAdapater.ViewHolder holder, int position) {
            holder.getWeeklyTaskContent().setText(taskItemArrayList.get(position).getContent());
            int point = taskItemArrayList.get(position).getPoint();
            String pointStr = String.valueOf(taskItemArrayList.get(position).getPoint());
            holder.getWeeklyTaskPoint().setText("+" + pointStr);

            int finishedNum = taskItemArrayList.get(position).getFinishedNum();
            int num = taskItemArrayList.get(position).getNum();
            holder.getWeeklyTaskNum().setText(finishedNum + "/" + num);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(v, holder.getAdapterPosition());
                    // Toast.makeText(v.getContext(), String.valueOf(holder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
                }
            });

            holder.getWeeklyTaskFinish().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toast.makeText(getContext(), "CheckBox" + holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                    // builder.setTitle(R.string.confirmation);
                    builder.setMessage("是否确认完成这项任务？");
                    builder.setPositiveButton("确认",
                            (dialog, which) -> {
                                int position = holder.getAdapterPosition();
                                Task task = weeklyTaskList.get(position);
                                String content = task.getContent();
                                String classification = task.getClassification();
                                int finishedNum = task.getFinishedNum();
                                int num = task.getNum();
                                task.setFinishedNum(finishedNum + 1);
                                PlayTaskMainActivity.mDBMaster.mTaskDBDao.updateTask(task);
                                adapter.getTaskItemArrayList().set(position, task);
                                adapter.notifyItemChanged(position);
                                holder.getWeeklyTaskFinish().setChecked(false);

                                TaskFragment.getInstance().updatePoint();

                                long time = new java.util.Date().getTime();
                                Count count = new Count(0, new Date(time), point, content, classification);
                                PlayTaskMainActivity.mDBMaster.mCountDBDao.insertData(count);

                                // 解决生命周期问题
                                DayCountFragment.RecycleViewTaskAdapater dayCountAdapter = DayCountFragment.getInstance().getAdapter();
                                if(dayCountAdapter != null){
                                    dayCountAdapter.getCountItemArrayList().add(count);
                                    dayCountAdapter.notifyItemInserted(dayCountAdapter.getCountItemArrayList().size());
                                }

                                if(finishedNum == num - 1){
                                    PlayTaskMainActivity.mDBMaster.mTaskDBDao.deleteData(weeklyTaskList.get(position).getId());
                                    weeklyTaskList.remove(position);
                                    adapter.notifyItemRemoved(position);
                                }

                            });
                    builder.setNegativeButton("取消",
                            ((dialog, which) -> {
                                holder.getWeeklyTaskFinish().setChecked(false);
                            }));
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return taskItemArrayList.size();
        }
    }

    public boolean onContextItemSelected(MenuItem item) {   // 响应RecycleView中每一项的菜单
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        switch (item.getItemId()) {
            case MENU_ID_DELETE:
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle(R.string.confirmation);
                builder.setMessage(R.string.sure_to_delete);
                builder.setPositiveButton(R.string.yes,
                        (dialog, which) -> {
                            PlayTaskMainActivity.mDBMaster.mTaskDBDao.deleteData(weeklyTaskList.get(item.getOrder()).getId());
                            weeklyTaskList.remove(item.getOrder());

                            // 不用再从数据库加载数据，维护集合dailyTaskList即可，只在刚启动的时候从数据库加载数据
                            // adapter.setTaskItemArrayList(taskDBHelper.queryByType("Daily"));
                            adapter.notifyItemRemoved(item.getOrder());
                        });

                builder.setNegativeButton(R.string.no,
                        ((dialog, which) -> {

                        }));
                AlertDialog dialog = builder.create();
                dialog.show();

                break;
        }
        return super.onContextItemSelected(item);
    }


    public void InsertList(Task task) {
        adapter.getTaskItemArrayList().add(task);
        adapter.notifyItemInserted(weeklyTaskList.size());
    }

}