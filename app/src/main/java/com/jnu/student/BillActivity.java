package com.jnu.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jnu.student.Count.DayCountFragment;
import com.jnu.student.data.Count;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class BillActivity extends AppCompatActivity implements View.OnClickListener {
    HashMap<String, Integer> classificationIconMap;
    private BillActivity.RecycleViewBillAdapater adapter;
    private int month;
    TextView tv_month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        RecyclerView recycle_view_bill = findViewById(R.id.recycle_view_bill);
        recycle_view_bill.setLayoutManager(new LinearLayoutManager(this)); // 设置布局管理器

        recycle_view_bill.addItemDecoration(new DividerItemDecoration
                (this,DividerItemDecoration.VERTICAL));// 添加分割线


        classificationIconMap = new HashMap<>();
        classificationIconMap.put("学习工作", R.drawable.learn);
        classificationIconMap.put("运动健身", R.drawable.sport);
        classificationIconMap.put("其他", R.drawable.else_class);

        classificationIconMap.put("娱乐", R.drawable.play);
        classificationIconMap.put("美食", R.drawable.eat);
        classificationIconMap.put("休息", R.drawable.relax);
        classificationIconMap.put("购物", R.drawable.shopping);
        classificationIconMap.put("旅行", R.drawable.travel);

        Toolbar toolbar = findViewById(R.id.bill_toolbar);
        // 为标题栏的返回按钮添加相应函数
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.tv_month = findViewById(R.id.bill_month);
        Button btn_minus = findViewById(R.id.month_minus);
        Button btn_add = findViewById(R.id.month_add);

        Calendar calendar = Calendar.getInstance();
        this.month = calendar.get(Calendar.MONTH) + 1;
        tv_month.setText(month + "月");

        btn_add.setOnClickListener(this);
        btn_minus.setOnClickListener(this);

        ArrayList<Count> billList = getBillListByMonth(this.month);
        adapter = new RecycleViewBillAdapater(billList);
        recycle_view_bill.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if(R.id.month_add == v.getId()){
            if(this.month < 12){
                month++;
                tv_month.setText(month + "月");
                adapter.setTaskItemArrayList(getBillListByMonth(this.month));
                adapter.notifyDataSetChanged();
            }
        } else if (R.id.month_minus == v.getId()) {
            if(this.month > 1){
                month--;
                tv_month.setText(month + "月");
                adapter.setTaskItemArrayList(getBillListByMonth(this.month));
                adapter.notifyDataSetChanged();
            }
        }
    }


    public class RecycleViewBillAdapater extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private static final int VIEW_TYPE_BILL_ITEM = 100;
        private static final int VIEW_TYPE_DATE_ITEM = 200;
        private List<Count> countItemArrayList;

        public RecycleViewBillAdapater(List<Count> countList) {
            this.countItemArrayList = countList;
        }

        public void setTaskItemArrayList(List<Count> countList) {
            this.countItemArrayList = countList;
        }

        public List<Count> getCountItemArrayList() {
            return countItemArrayList;
        }

        @Override
        public int getItemViewType(int position) {
            if(countItemArrayList.get(position).getDate() != null){
                return VIEW_TYPE_BILL_ITEM;
            } else{
                return VIEW_TYPE_DATE_ITEM;
            }
        }

        public class BillViewHolder extends RecyclerView.ViewHolder {
            private final ImageView bill_classification;
            private final TextView tv_bill_content;
            private final TextView tv_bill_point;


            public BillViewHolder(View itemView) {
                super(itemView);
                // Define click listener for the ViewHolder's View
                bill_classification = (ImageView) itemView.findViewById(R.id.bill_classification);
                tv_bill_content = (TextView) itemView.findViewById(R.id.tv_bill_content);
                tv_bill_point = (TextView) itemView.findViewById(R.id.tv_bill_point);
            }

            public ImageView getClassification() {
                return bill_classification;
            }

            public TextView getContent() {
                return tv_bill_content;
            }

            public TextView getPoint() {
                return tv_bill_point;
            }
        }
        public class DateViewHolder extends RecyclerView.ViewHolder {

            private final TextView tv_date;

            public DateViewHolder(View itemView) {
                super(itemView);
                tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            }

            public TextView getDate() {
                return tv_date;
            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            if (VIEW_TYPE_BILL_ITEM == viewType ){
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.bill_item, viewGroup, false);
                return new BillActivity.RecycleViewBillAdapater.BillViewHolder(view);
            }
            else if(VIEW_TYPE_DATE_ITEM == viewType ){
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.date_item, viewGroup, false);
                return new BillActivity.RecycleViewBillAdapater.DateViewHolder(view);
            }
            else
                return null;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if(holder instanceof BillViewHolder){
                BillViewHolder billViewHolder = (BillViewHolder) holder;
                String classification = countItemArrayList.get(position).getClassification();
                billViewHolder.getClassification().setImageResource(classificationIconMap.get(classification));


                billViewHolder.getContent().setText(countItemArrayList.get(position).getContent());
                int point = countItemArrayList.get(position).getPoint();
                String pointStr = String.valueOf(point);
                if(point > 0){
                    billViewHolder.getPoint().setText("+" + pointStr);
                }else {
                    billViewHolder.getPoint().setText(pointStr);
                    billViewHolder.getPoint().setTextColor(Color.RED);
                }
            }
            else if (holder instanceof DateViewHolder){
                DateViewHolder dateViewHolder = (DateViewHolder) holder;
                String date = countItemArrayList.get(position).getContent();
                dateViewHolder.getDate().setText(date);
            }
        }

        @Override
        public int getItemCount() {
            return countItemArrayList.size();
        }
    }

    private ArrayList<Count> getBillListByMonth(int month){
        ArrayList<Count> billItemArrayList = new ArrayList<>();
        List<String> dates = PlayTaskMainActivity.mDBMaster.mCountDBDao.queryDateByMonth(month);
        for (String date : dates) {
            Count count = new Count();
            count.setContent(date);
            billItemArrayList.add(count);
            List<Count> counts = PlayTaskMainActivity.mDBMaster.mCountDBDao.queryDataListByDate(date);
            billItemArrayList.addAll(counts);
        }
        return billItemArrayList;
    }

}