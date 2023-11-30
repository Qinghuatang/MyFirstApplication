package com.jnu.student;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
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

import com.jnu.student.Count.DayCountFragment;
import com.jnu.student.Task.TaskFragment;
import com.jnu.student.data.Award;
import com.jnu.student.data.Count;
import com.jnu.student.data.Task;


import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

public class AwardFragment extends Fragment {
    public static final int MENU_ID_DELETE = 4;
    public static final int MENU_ID_UPDATE = 5;
    private ActivityResultLauncher<Intent> addItemLauncher;
    private ActivityResultLauncher<Intent> updateItemLauncher;
    Toolbar award_toolbar;
    private List<Award> awardList;
    private AwardFragment.RecycleViewTaskAdapater adapter;


    public AwardFragment() {
        // Required empty public constructor
    }

    public static AwardFragment newInstance(String from) {
        AwardFragment fragment = new AwardFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from", from);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


        addItemLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Intent data = result.getData();
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Bundle bundle = data.getExtras();

                        // 处理返回的数据
                        Timestamp time = new Timestamp(System.currentTimeMillis());
                        String content = bundle.getString("content");
                        int point = bundle.getInt("point");
                        String classification = bundle.getString("classification");

                        Award award = new Award(0, time, content, point, 0, classification);

                        PlayTaskMainActivity.mDBMaster.mAwardDBDao.insertData(award);
                        award.setId(PlayTaskMainActivity.mDBMaster.mAwardDBDao.getId());

                        awardList.add(award);
                        adapter.notifyItemInserted(awardList.size());
                    }
                });

        updateItemLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (null != result) {
                        Intent data = result.getData();
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Bundle bundle = data.getExtras();

                            int position = bundle.getInt("position");

                            int id = awardList.get(position).getId();
                            Timestamp time = new Timestamp(System.currentTimeMillis());
                            String content = bundle.getString("content");
                            int point = bundle.getInt("point");
                            int buyNum = 0;
                            String classification = bundle.getString("classification");

                            Award award = new Award(id, time, content, point, buyNum, classification);

                            PlayTaskMainActivity.mDBMaster.mAwardDBDao.updateData(award);
                            adapter.getAwardItemArrayList().set(position, award);
                            adapter.notifyItemChanged(position);
                        }
                    }
                });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_award, container, false);

        RecyclerView recycle_view_award = rootView.findViewById(R.id.recycle_view_award);
        recycle_view_award.setLayoutManager(new LinearLayoutManager(requireActivity())); // 设置布局管理器

        recycle_view_award.addItemDecoration(new DividerItemDecoration
                (getContext(), DividerItemDecoration.VERTICAL));// 添加分割线

        awardList = PlayTaskMainActivity.mDBMaster.mAwardDBDao.queryDataList();

        adapter = new RecycleViewTaskAdapater(awardList);
        recycle_view_award.setAdapter(adapter);     // 先设置adapter才能显示工具栏的菜单
        registerForContextMenu(recycle_view_award);     // 创建场景菜单事件

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // Toast.makeText(getContext(), awardList.get(position).getContent(), Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                Award award = awardList.get(position);
                int point = award.getPoint();
                builder.setMessage(String.format("是否消耗%d分兑换这项奖励？", -point));
                builder.setPositiveButton("确认",
                        (dialog, which) -> {
                            int pointSum = PlayTaskMainActivity.mDBMaster.mCountDBDao.queryPointSum();
                            if (pointSum >= -point){
                                String content = award.getContent();
                                int buyNum = award.getBuyNum();
                                String classification = award.getClassification();
                                award.setBuyNum(buyNum + 1);
                                PlayTaskMainActivity.mDBMaster.mAwardDBDao.updateData(award);
                                adapter.getAwardItemArrayList().set(position, award);
                                adapter.notifyItemChanged(position);

                                long time = new java.util.Date().getTime();
                                Count count = new Count(0, new Date(time), point, content, classification);
                                PlayTaskMainActivity.mDBMaster.mCountDBDao.insertData(count);

                                TaskFragment.getInstance().updatePoint();
                                DayCountFragment.getInstance().updatePage(count);
                            }
                            else {
                                Toast.makeText(getContext(), "您的分数不足!", Toast.LENGTH_SHORT).show();
                            }
                        });
                builder.setNegativeButton("取消",
                        ((dialog, which) -> {

                        }));
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        // 注意要设置为类的成员变量，否则程序报错
        award_toolbar = rootView.findViewById(R.id.award_toolbar);

        // 为标题栏的右侧菜单添加监听器响应函数
        award_toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (R.id.create_new_award == item.getItemId()) {
                    Intent addIntent = new Intent(getActivity(), AwardDetailActivity.class);
                    addItemLauncher.launch(addIntent);
                }
                return true;
            }
        });

        return rootView;
    }

    public class RecycleViewTaskAdapater extends RecyclerView.Adapter<AwardFragment.RecycleViewTaskAdapater.ViewHolder> {
        private List<Award> awardItemArrayList;
        private OnItemClickListener mOnItemClickListener;

        public RecycleViewTaskAdapater(List<Award> awardItemArrayList) {
            this.awardItemArrayList = awardItemArrayList;
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.mOnItemClickListener = onItemClickListener;
        }

        public void setAwardItemArrayList(List<Award> awardItemArrayList) {
            this.awardItemArrayList = awardItemArrayList;
        }

        public List<Award> getAwardItemArrayList() {
            return awardItemArrayList;
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
            private final TextView tv_award_content;
            private final TextView tv_award_point;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                tv_award_content = (TextView) itemView.findViewById(R.id.tv_award_content);
                tv_award_point = (TextView) itemView.findViewById(R.id.tv_award_point);

                itemView.setOnCreateContextMenuListener(this);     // 创建一个上下文场景菜单监听器
            }

            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("具体操作");
                menu.add(0, MENU_ID_DELETE, this.getAdapterPosition(), "删除");
                menu.add(0, MENU_ID_UPDATE, this.getAdapterPosition(), "修改");
            }

            public TextView getAwardContent() {
                return tv_award_content;
            }

            public TextView getAwardPoint() {
                return tv_award_point;
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.award_item_row, viewGroup, false);

            AwardFragment.RecycleViewTaskAdapater.ViewHolder viewHolder = new AwardFragment.RecycleViewTaskAdapater.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.getAwardContent().setText(awardItemArrayList.get(position).getContent());
            int point = awardItemArrayList.get(position).getPoint();
            String pointStr = String.valueOf(awardItemArrayList.get(position).getPoint());
            holder.getAwardPoint().setText(pointStr);

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
            return awardItemArrayList.size();
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
                            PlayTaskMainActivity.mDBMaster.mAwardDBDao.deleteData(awardList.get(item.getOrder()).getId());
                            awardList.remove(item.getOrder());
                            adapter.notifyItemRemoved(item.getOrder());
                        });

                builder.setNegativeButton(R.string.no,
                        ((dialog, which) -> {

                        }));
                AlertDialog dialog = builder.create();
                dialog.show();
                break;

            case MENU_ID_UPDATE:
                Intent updateIntent = new Intent(getContext(), AwardDetailActivity.class);
                int position = item.getOrder();
                // 创建一个新包裹
                Bundle bundle = new Bundle();
                bundle.putString("operate", "update");
                bundle.putInt("position", position);

                bundle.putString("content", awardList.get(position).getContent());
                bundle.putInt("point", awardList.get(position).getPoint());
                bundle.putString("classification", awardList.get(position).getClassification());

                updateIntent.putExtras(bundle);        // 把快递包裹塞给意图
                updateItemLauncher.launch(updateIntent);
                break;
        }
        return super.onContextItemSelected(item);
    }

}