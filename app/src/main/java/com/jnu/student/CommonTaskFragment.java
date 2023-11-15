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
import android.widget.Toast;

import com.jnu.student.data.Book;
import com.jnu.student.data.DataBank;
import com.jnu.student.data.Task;

import java.sql.Timestamp;
import java.util.ArrayList;

public class CommonTaskFragment extends Fragment {

    public static final int MENU_ID_ADD = 7;
    public static final int MENU_ID_DELETE = 8;
    public static final int MENU_ID_UPDATE = 9;

    private ActivityResultLauncher<Intent> addItemLauncher;
    private ActivityResultLauncher<Intent> updateItemLauncher;
    private ArrayList<Task> taskItems;
    private RecycleViewTaskAdapater adapter;

    public CommonTaskFragment() {
        // Required empty public constructor
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

        taskItems = new ArrayList<>();

        if (0 == taskItems.size()) {
            taskItems.add(new Task("吃饭", new Timestamp(System.currentTimeMillis()), 10));
            taskItems.add(new Task("睡觉", new Timestamp(System.currentTimeMillis()), 10));
            taskItems.add(new Task("学习", new Timestamp(System.currentTimeMillis()), 10));
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
                        taskItems.add(new Task(content, new Timestamp(System.currentTimeMillis()),point));
                        adapter.notifyItemInserted(taskItems.size());
                    }

                });

        updateItemLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {

                });


        return rootView;
    }

    public static class RecycleViewTaskAdapater extends RecyclerView.Adapter<CommonTaskFragment.RecycleViewTaskAdapater.ViewHolder> {

        private ArrayList<Task> taskItemArrayList;


        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
            private final TextView tv_common_task_content;
            private final TextView tv_common_task_point;

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

        public RecycleViewTaskAdapater(ArrayList<Task> taskItemArrayList){
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

//                // 创建一个新包裹
//                Bundle bundle = new Bundle();
//                bundle.putString("operate", "add");
//                bundle.putString("position", String.valueOf(item.getOrder()));
//                addIntent.putExtras(bundle);		// 把快递包裹塞给意图
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

                break;
        }
        return super.onContextItemSelected(item);
    }
}