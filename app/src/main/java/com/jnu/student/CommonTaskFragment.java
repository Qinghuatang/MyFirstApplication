package com.jnu.student;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.jnu.student.data.Task;
import com.jnu.student.database.TaskDBHelper;

import java.sql.Timestamp;
import java.util.List;

public class CommonTaskFragment extends Fragment {

    public static final int MENU_ID_DELETE = 3;

    private ActivityResultLauncher<Intent> updateItemLauncher;
    private List<Task> commonTaskList;
    private RecycleViewTaskAdapater adapter;

    private TaskDBHelper taskDBHelper;

    public RecycleViewTaskAdapater getAdapter() {
        return adapter;
    }

    public void setCommonTaskList(List<Task> commonTaskList) {
        this.commonTaskList = commonTaskList;
    }

    public CommonTaskFragment() {
    }

    public CommonTaskFragment(TaskDBHelper taskDBHelper) {
        this.taskDBHelper = taskDBHelper;
    }

    public static CommonTaskFragment newInstance() {
        CommonTaskFragment fragment = new CommonTaskFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_common_task, container, false);

        RecyclerView recycle_view_daily_task = rootView.findViewById(R.id.recycle_view_common_task);
        recycle_view_daily_task.setLayoutManager(new LinearLayoutManager(requireActivity())); // 设置布局管理器

        adapter = new RecycleViewTaskAdapater(commonTaskList);
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

                bundle.putString("content", commonTaskList.get(position).getContent());
                bundle.putInt("point", commonTaskList.get(position).getPoint());
                bundle.putInt("num", commonTaskList.get(position).getNum());
                bundle.putString("classification", commonTaskList.get(position).getClassification());

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

                            int id = commonTaskList.get(position).getId();
                            String taskType = "Common";
                            Timestamp time = new Timestamp(System.currentTimeMillis());
                            String content = bundle.getString("content");
                            int point = bundle.getInt("point");
                            int finishedNum = 0;
                            int num = bundle.getInt("num");
                            String classification = bundle.getString("classification");

                            Task task = new Task(id, taskType, time, content, point, finishedNum, num, classification);

                            taskDBHelper.updateTask(task);
                            adapter.getTaskItemArrayList().set(position, task);
                            adapter.notifyItemChanged(position);
                        }
                    }

                });


        return rootView;
    }

    public class RecycleViewTaskAdapater extends RecyclerView.Adapter<CommonTaskFragment.RecycleViewTaskAdapater.ViewHolder> {
        private List<Task> taskItemArrayList;
        private OnItemClickListener mOnItemClickListener;

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
            private final TextView tv_common_task_content;
            private final TextView tv_common_task_point;

            @Override
            public void onCreateContextMenu(ContextMenu menu, View v,
                                            ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("具体操作");
                menu.add(0, MENU_ID_DELETE, this.getAdapterPosition(), "删除");

            }

            public ViewHolder(View itemView) {
                super(itemView);
                // Define click listener for the ViewHolder's View
                tv_common_task_content = (TextView) itemView.findViewById(R.id.tv_common_task_content);
                tv_common_task_point = (TextView) itemView.findViewById(R.id.tv_common_task_point);

                itemView.setOnCreateContextMenuListener(this);     // 创建一个上下文场景菜单监听器

            }

            public TextView getDailyTaskContent() {
                return tv_common_task_content;
            }

            public TextView getDailyTaskPoint() {
                return tv_common_task_point;
            }
        }

        public RecycleViewTaskAdapater(List<Task> taskItemArrayList){
            this.taskItemArrayList = taskItemArrayList;
        }

        @NonNull
        @Override
        public CommonTaskFragment.RecycleViewTaskAdapater.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.common_task_item_row, viewGroup, false);

            return new CommonTaskFragment.RecycleViewTaskAdapater.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CommonTaskFragment.RecycleViewTaskAdapater.ViewHolder holder, int position) {
            holder.getDailyTaskContent().setText(taskItemArrayList.get(position).getContent());
            int point = taskItemArrayList.get(position).getPoint();
            String pointStr = String.valueOf(taskItemArrayList.get(position).getPoint());
            holder.getDailyTaskPoint().setText("+" + pointStr);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(v, holder.getAdapterPosition());
                    // Toast.makeText(v.getContext(), String.valueOf(holder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
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
//         Toast.makeText(requireActivity(), "clicked" + item.getOrder(), Toast.LENGTH_SHORT).show();
        switch (item.getItemId()) {
            case MENU_ID_DELETE:
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle(R.string.confirmation);
                builder.setMessage(R.string.sure_to_delete);
                builder.setPositiveButton(R.string.yes,
                        (dialog, which) -> {
                            taskDBHelper.delTask(commonTaskList.get(item.getOrder()));
                            commonTaskList.remove(item.getOrder());

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
        adapter.notifyItemInserted(commonTaskList.size());
    }

    public int getCommonTaskListSize() {
        return commonTaskList.size();
    }
}