package com.jnu.student;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.jnu.student.data.Book;
import com.jnu.student.data.DataBank;
import com.jnu.student.data.Task;
import com.jnu.student.database.TaskDBHelper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DailyTaskFragment extends Fragment {

    public static final int MENU_ID_ADD = 1;
    public static final int MENU_ID_DELETE = 2;
    public static final int MENU_ID_UPDATE = 3;

    private ActivityResultLauncher<Intent> addItemLauncher;
    private ActivityResultLauncher<Intent> updateItemLauncher;
    private List<Task> taskItems;
    private RecycleViewTaskAdapater adapter;

    private String mDatabaseName;
    private TaskDBHelper dbHelper;

    public DailyTaskFragment() {
        // Required empty public constructor
    }

    public static DailyTaskFragment newInstance() {
        DailyTaskFragment fragment = new DailyTaskFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabaseName = getActivity().getFilesDir() + "/PlayTask.db";

        // 创建或打开数据库, 数据库如果不存在就创建, 如果存在就打开
        SQLiteDatabase db = getActivity().openOrCreateDatabase(mDatabaseName, Context.MODE_PRIVATE, null);
        String desc = String.format("数据库%s创建%s", db.getPath(), db != null ? "成功" : "失败");
        Log.d("database", desc);

        // 获得数据库帮助器的实例
        dbHelper = TaskDBHelper.getInstance(getActivity());
        // 打开数据库帮助器的读写连接
        dbHelper.openWriteLink();
        dbHelper.openReadLink();
    }

    @Override
    public void onStop() {
        super.onStop();
        // 关闭数据库连接
//        dbHelper.closeLink();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_daily_task, container, false);

        RecyclerView recycle_view_daily_task = rootView.findViewById(R.id.recycle_view_daily_task);
        recycle_view_daily_task.setLayoutManager(new LinearLayoutManager(requireActivity())); // 设置布局管理器

        taskItems = dbHelper.queryAll();

        if (0 == taskItems.size()) {
            taskItems.add(new Task("吃饭", new Timestamp(System.currentTimeMillis()), 5));
            taskItems.add(new Task("睡觉", new Timestamp(System.currentTimeMillis()), 10));
            taskItems.add(new Task("学习", new Timestamp(System.currentTimeMillis()), 15));
        }

        adapter = new RecycleViewTaskAdapater(taskItems);
        recycle_view_daily_task.setAdapter(adapter);
        registerForContextMenu(recycle_view_daily_task);     // 创建场景菜单事件

        addItemLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Intent data = result.getData();
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Bundle bundle = data.getExtras();
                        // 处理返回的数据
                        String content = bundle.getString("content");    // 获取返回的数据
                        int point = bundle.getInt("point");
                        // taskItems.add(new Task(content, new Timestamp(System.currentTimeMillis()),point));
                        Task newTask = new Task(content, new Timestamp(System.currentTimeMillis()),point);
                        newTask.setTaskType("Daily");
                        dbHelper.insert(newTask);

                        taskItems = dbHelper.queryAll();
                        adapter.setTaskItemArrayList(taskItems);
                        adapter.notifyItemInserted(taskItems.size());
                    }

                });

        updateItemLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {

                });


        return rootView;
    }

    public static class RecycleViewTaskAdapater extends RecyclerView.Adapter<DailyTaskFragment.RecycleViewTaskAdapater.ViewHolder> {

        private List<Task> taskItemArrayList;

        public void setTaskItemArrayList(List<Task> taskItemArrayList) {
            this.taskItemArrayList = taskItemArrayList;
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
            private final TextView tv_daily_task_content;
            private final TextView tv_daily_task_point;

            @Override
            public void onCreateContextMenu(ContextMenu menu, View v,
                                            ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("具体操作");
                menu.add(0, MENU_ID_ADD, this.getAdapterPosition(), "添加");
                menu.add(0, MENU_ID_DELETE, this.getAdapterPosition(), "删除");
                menu.add(0, MENU_ID_UPDATE, this.getAdapterPosition(), "修改");

            }

            public ViewHolder(View itemView) {
                super(itemView);
                // Define click listener for the ViewHolder's View
                tv_daily_task_content = (TextView) itemView.findViewById(R.id.tv_daily_task_content);
                tv_daily_task_point = (TextView) itemView.findViewById(R.id.tv_daily_task_point);

                itemView.setOnCreateContextMenuListener(this);     // 创建一个上下文场景菜单监听器

            }

            public TextView getDailyTaskContent() {
                return tv_daily_task_content;
            }

            public TextView getDailyTaskPoint() {
                return tv_daily_task_point;
            }
        }

        public RecycleViewTaskAdapater(List<Task> taskItemArrayList){
            this.taskItemArrayList = taskItemArrayList;
        }

        @NonNull
        @Override
        public DailyTaskFragment.RecycleViewTaskAdapater.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.daily_task_item_row, viewGroup, false);

            return new DailyTaskFragment.RecycleViewTaskAdapater.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DailyTaskFragment.RecycleViewTaskAdapater.ViewHolder holder, int position) {
            holder.getDailyTaskContent().setText(taskItemArrayList.get(position).getContent());
            int point = taskItemArrayList.get(position).getPoint();
            String pointStr = String.valueOf(taskItemArrayList.get(position).getPoint());
            holder.getDailyTaskPoint().setText("+" + pointStr);
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
            case MENU_ID_ADD:
                Intent addIntent = new Intent(getActivity(), TaskDetailActivity.class);
                addItemLauncher.launch(addIntent);
                break;
            case MENU_ID_DELETE:
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle(R.string.confirmation);
                builder.setMessage(R.string.sure_to_delete);
                builder.setPositiveButton(R.string.yes,
                        (dialog, which) -> {
                            taskItems.remove(item.getOrder());
                            adapter.notifyItemRemoved(item.getOrder());
                        });

                builder.setNegativeButton(R.string.no,
                        ((dialog, which) -> {

                        }));
                AlertDialog dialog = builder.create();
                dialog.show();

                break;
            case MENU_ID_UPDATE:
                Intent updateIntent = new Intent(requireActivity(), TaskDetailActivity.class);

                // 创建一个新包裹
                Bundle bundle = new Bundle();
                // item.getOrder() 获取该项的序号
                bundle.putInt("position", item.getOrder());
                bundle.putString("content", taskItems.get(item.getOrder()).getContent());
                bundle.putInt("point", taskItems.get(item.getOrder()).getPoint());
                updateIntent.putExtras(bundle);		// 把快递包裹塞给意图

                updateItemLauncher.launch(updateIntent);
                break;
        }
        return super.onContextItemSelected(item);
    }
}