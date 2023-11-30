package com.jnu.student.Count;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jnu.student.PlayTaskMainActivity;
import com.jnu.student.R;
import com.jnu.student.data.Count;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DayCountFragment extends Fragment {

    private static DayCountFragment instance;
    HashMap<String, Integer> classificationIconMap;


    private List<Count> dayCountList;
    private DayCountFragment.RecycleViewTaskAdapater adapter;

    public void setDayCountList(List<Count> dayCountList) {
        this.dayCountList = dayCountList;
    }

    public DayCountFragment.RecycleViewTaskAdapater getAdapter() {
        return adapter;
    }

    private DayCountFragment() {
    }


    //获取唯一可用的对象
    public static DayCountFragment getInstance(){
        if (instance == null)
            instance = new DayCountFragment();
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("test", "DayCountOnCreate");
        super.onCreate(savedInstanceState);
        classificationIconMap = new HashMap<>();
        classificationIconMap.put("学习工作", R.drawable.learn);
        classificationIconMap.put("运动健身", R.drawable.sport);
        classificationIconMap.put("其他", R.drawable.else_class);

        classificationIconMap.put("娱乐", R.drawable.play);
        classificationIconMap.put("美食", R.drawable.eat);
        classificationIconMap.put("休息", R.drawable.relax);
        classificationIconMap.put("购物", R.drawable.shopping);
        classificationIconMap.put("旅行", R.drawable.travel);

        // adapter要在onCreate的时候创建, 不能在onCreateView中创建
        // 因为任务中会更新adapter的数组, 此时要保证adapter已经创建了
        adapter = new DayCountFragment.RecycleViewTaskAdapater(dayCountList);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("test", "DayCountOnCreateView");
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_day_count, container, false);

        RecyclerView recycle_view_daily_task = rootView.findViewById(R.id.recycle_view_day_count);
        recycle_view_daily_task.setLayoutManager(new LinearLayoutManager(requireActivity())); // 设置布局管理器

        recycle_view_daily_task.addItemDecoration(new DividerItemDecoration
                (getContext(),DividerItemDecoration.VERTICAL));// 添加分割线

        recycle_view_daily_task.setAdapter(adapter);
        registerForContextMenu(recycle_view_daily_task);     // 创建场景菜单事件

        TextView tv_day_date = rootView.findViewById(R.id.tv_day_date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = new Date();
        String dateStr = sdf.format(date);
        tv_day_date.setText(" " + dateStr);

        setCountString(rootView);

        return rootView;
    }

    public class RecycleViewTaskAdapater extends RecyclerView.Adapter<DayCountFragment.RecycleViewTaskAdapater.ViewHolder> {
        private List<Count> countItemArrayList;

        public RecycleViewTaskAdapater(List<Count> taskItemArrayList) {
            this.countItemArrayList = taskItemArrayList;
        }

        public void setTaskItemArrayList(List<Count> taskItemArrayList) {
            this.countItemArrayList = taskItemArrayList;
        }

        public List<Count> getCountItemArrayList() {
            return countItemArrayList;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private final ImageView day_count_classification;
            private final TextView tv_day_count_content;
            private final TextView tv_day_count_point;

            public ViewHolder(View itemView) {
                super(itemView);
                // Define click listener for the ViewHolder's View
                day_count_classification = (ImageView) itemView.findViewById(R.id.day_count_classification);
                tv_day_count_content = (TextView) itemView.findViewById(R.id.tv_day_count_content);
                tv_day_count_point = (TextView) itemView.findViewById(R.id.tv_day_count_point);
            }

            public ImageView getClassification() {
                return day_count_classification;
            }

            public TextView getContent() {
                return tv_day_count_content;
            }

            public TextView getPoint() {
                return tv_day_count_point;
            }
        }

        @NonNull
        @Override
        public DayCountFragment.RecycleViewTaskAdapater.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.day_count_item_row, viewGroup, false);

            DayCountFragment.RecycleViewTaskAdapater.ViewHolder viewHolder = new DayCountFragment.RecycleViewTaskAdapater.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull DayCountFragment.RecycleViewTaskAdapater.ViewHolder holder, int position) {

            String classification = countItemArrayList.get(position).getClassification();
            holder.getClassification().setImageResource(classificationIconMap.get(classification));


            holder.getContent().setText(countItemArrayList.get(position).getContent());

            int point = countItemArrayList.get(position).getPoint();
            String pointStr = String.valueOf(point);
            if(point > 0){
                holder.getPoint().setText("+" + pointStr);
            }else {
                holder.getPoint().setText(pointStr);
                holder.getPoint().setTextColor(Color.RED);
            }
        }

        @Override
        public int getItemCount() {
            return countItemArrayList.size();
        }

    }

    public void updatePage(Count count) {
        if(adapter != null){
            if(count != null){
                adapter.getCountItemArrayList().add(count);
                adapter.notifyItemInserted(dayCountList.size());
                setCountString(getView());
            }
        }
    }

    public void setCountString(View rootView){
        TextView tv_day_count = rootView.findViewById(R.id.tv_day_count);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());
        int income = PlayTaskMainActivity.mDBMaster.mCountDBDao.queryDayIncomeByDate(date);
        int expend = PlayTaskMainActivity.mDBMaster.mCountDBDao.queryDayExpendByDate(date);
        tv_day_count.setText("收入: " + income + "    花费: " + expend + " ");
    }
}