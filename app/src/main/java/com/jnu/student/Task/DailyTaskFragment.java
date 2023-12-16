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

public class DailyTaskFragment extends Fragment {

    private static DailyTaskFragment dailyTaskFragment;
    public static final int MENU_ID_DELETE = 1;

    private ActivityResultLauncher<Intent> updateItemLauncher;
    private List<Task> dailyTaskList;
    private RecycleViewTaskAdapater adapter;


    public RecycleViewTaskAdapater getAdapter() {
        return adapter;
    }

    public void setDailyTaskList(List<Task> dailyTaskList) {
        this.dailyTaskList = dailyTaskList;
    }

    private DailyTaskFragment() {
    }

    public static DailyTaskFragment getInstance() {
        if (dailyTaskFragment == null) {
            dailyTaskFragment = new DailyTaskFragment();
        }
        return dailyTaskFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_daily_task, container, false);
        
        RecyclerView recycle_view_daily_task = rootView.findViewById(R.id.recycle_view_daily_task);
        recycle_view_daily_task.setLayoutManager(new LinearLayoutManager(requireActivity())); // 设置布局管理器

        recycle_view_daily_task.addItemDecoration(new DividerItemDecoration
                (getContext(),DividerItemDecoration.VERTICAL));// 添加分割线

        adapter = new RecycleViewTaskAdapater(dailyTaskList);
        recycle_view_daily_task.setAdapter(adapter);
        registerForContextMenu(recycle_view_daily_task);     // 创建场景菜单事件

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent updateIntent = new Intent(getContext(), TaskDetailActivity.class);
                // 创建一个新包裹
                Bundle bundle = new Bundle();
                bundle.putString("operate", "update");
                bundle.putInt("position", position);

                bundle.putString("content", dailyTaskList.get(position).getContent());
                bundle.putInt("point", dailyTaskList.get(position).getPoint());
                bundle.putInt("num", dailyTaskList.get(position).getNum());
                bundle.putString("classification", dailyTaskList.get(position).getClassification());

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

                            int id = dailyTaskList.get(position).getId();
                            String taskType = "Daily";
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

    public class RecycleViewTaskAdapater extends RecyclerView.Adapter<DailyTaskFragment.RecycleViewTaskAdapater.ViewHolder> {
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
            private final CheckBox cb_daily_task;
            private final TextView tv_daily_task_content;
            private final TextView tv_daily_task_point;
            private final TextView tv_daily_task_num;

            public ViewHolder(View itemView) {
                super(itemView);
                // Define click listener for the ViewHolder's View
                cb_daily_task = (CheckBox) itemView.findViewById(R.id.cb_daily_task);
                tv_daily_task_content = (TextView) itemView.findViewById(R.id.tv_daily_task_content);
                tv_daily_task_point = (TextView) itemView.findViewById(R.id.tv_daily_task_point);
                tv_daily_task_num = (TextView) itemView.findViewById(R.id.tv_daily_task_num);
                itemView.setOnCreateContextMenuListener(this);     // 创建一个上下文场景菜单监听器
            }

            @Override
            public void onCreateContextMenu(ContextMenu menu, View v,
                                            ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("具体操作");
                menu.add(0, MENU_ID_DELETE, this.getAdapterPosition(), "删除");
            }

            public CheckBox getDailyTaskFinish() {
                return cb_daily_task;
            }

            public TextView getDailyTaskContent() {
                return tv_daily_task_content;
            }

            public TextView getDailyTaskPoint() {
                return tv_daily_task_point;
            }

            public TextView getDailyTaskNum() {
                return tv_daily_task_num;
            }

        }

        @NonNull
        @Override
        public DailyTaskFragment.RecycleViewTaskAdapater.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.daily_task_item_row, viewGroup, false);

            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull DailyTaskFragment.RecycleViewTaskAdapater.ViewHolder holder, int position) {
            holder.getDailyTaskContent().setText(taskItemArrayList.get(position).getContent());
            int point = taskItemArrayList.get(position).getPoint();
            String pointStr = String.valueOf(taskItemArrayList.get(position).getPoint());
            holder.getDailyTaskPoint().setText("+" + pointStr);

            int finishedNum = taskItemArrayList.get(position).getFinishedNum();
            int num = taskItemArrayList.get(position).getNum();
            holder.getDailyTaskNum().setText(finishedNum + "/" + num);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(v, holder.getAdapterPosition());
                    // Toast.makeText(v.getContext(), String.valueOf(holder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
                }
            });

            holder.getDailyTaskFinish().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toast.makeText(getContext(), "CheckBox" + holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                    // builder.setTitle(R.string.confirmation);
                    builder.setMessage("是否确认完成这项任务？");
                    builder.setPositiveButton("确认",
                            (dialog, which) -> {
                                int position = holder.getAdapterPosition();
                                Task task = dailyTaskList.get(position);
                                String content = task.getContent();
                                String classification = task.getClassification();
                                int finishedNum = task.getFinishedNum();
                                int num = task.getNum();
                                task.setFinishedNum(finishedNum + 1);
                                PlayTaskMainActivity.mDBMaster.mTaskDBDao.updateTask(task);
                                adapter.getTaskItemArrayList().set(position, task);
                                adapter.notifyItemChanged(position);
                                holder.getDailyTaskFinish().setChecked(false);

                                long time = new java.util.Date().getTime();
                                Count count = new Count(0, new Date(time), point, content, classification);
                                PlayTaskMainActivity.mDBMaster.mCountDBDao.insertData(count);

                                TaskFragment.getInstance().updatePoint();

                                // 解决生命周期问题
//                                DayCountFragment.RecycleViewTaskAdapater dayCountAdapter = DayCountFragment.getInstance().getAdapter();
//                                if(dayCountAdapter != null){
//                                    dayCountAdapter.getCountItemArrayList().add(count);
//                                    dayCountAdapter.notifyItemInserted(dayCountAdapter.getCountItemArrayList().size());
//                                }
                                DayCountFragment.getInstance().updatePage(count);

                                if(finishedNum == num - 1){
                                    PlayTaskMainActivity.mDBMaster.mTaskDBDao.deleteData(dailyTaskList.get(position).getId());
                                    dailyTaskList.remove(position);
                                    adapter.notifyItemRemoved(position);
                                }

                            });
                    builder.setNegativeButton("取消",
                            ((dialog, which) -> {
                                holder.getDailyTaskFinish().setChecked(false);
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
        // Toast.makeText(requireActivity(), "clicked" + item.getOrder(), Toast.LENGTH_SHORT).show();

        switch (item.getItemId()) {
            case MENU_ID_DELETE:
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle(R.string.confirmation);
                builder.setMessage(R.string.sure_to_delete);
                builder.setPositiveButton(R.string.yes,
                        (dialog, which) -> {
                            PlayTaskMainActivity.mDBMaster.mTaskDBDao.deleteData(dailyTaskList.get(item.getOrder()).getId());
                            dailyTaskList.remove(item.getOrder());

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
        adapter.notifyItemInserted(dailyTaskList.size());
    }

}