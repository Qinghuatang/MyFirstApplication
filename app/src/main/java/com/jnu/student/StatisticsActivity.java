package com.jnu.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;

public class StatisticsActivity extends AppCompatActivity implements OnChartValueSelectedListener, View.OnClickListener {
    private PieChart incomePieChart;
    private PieChart expendPieChart;

    private int month;
    TextView tv_month;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        Toolbar toolbar = findViewById(R.id.statistics_toolbar);
        // 为标题栏的返回按钮添加相应函数
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.tv_month = findViewById(R.id.statistics_month);
        Button btn_minus = findViewById(R.id.statistics_month_minus);
        Button btn_add = findViewById(R.id.statistics_month_add);

        Calendar calendar = Calendar.getInstance();
        this.month = calendar.get(Calendar.MONTH) + 1;
        tv_month.setText(month + "月");

        btn_add.setOnClickListener(this);
        btn_minus.setOnClickListener(this);

        //饼状图
        incomePieChart = (PieChart) findViewById(R.id.incomePieChart);
        expendPieChart = (PieChart) findViewById(R.id.expendPieChart);
        initView(incomePieChart);
        initView(expendPieChart);

        updateData(this.month);
    }

    //初始化View
    private void initView(PieChart chart) {
        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);

        chart.setDragDecelerationFrictionCoef(0.95f);
        //设置中间文字
        chart.setCenterText(generateCenterSpannableText());

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(44f);
        chart.setTransparentCircleRadius(50f);

        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);
        // 触摸旋转
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);

        //变化监听
        chart.setOnChartValueSelectedListener(this);

//        mPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // 输入标签样式
        chart.setEntryLabelColor(Color.BLACK);
        chart.setEntryLabelTextSize(12f);
    }

    //设置中间文字
    private SpannableString generateCenterSpannableText() {
        SpannableString s = new SpannableString("");
        return s;
    }

    //设置数据
    private void setData(PieChart chart, ArrayList<PieEntry> entries, String label) {
        PieDataSet dataSet = new PieDataSet(entries, label);
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        //数据和颜色
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        chart.setData(data);
        chart.highlightValues(null);
        //刷新
        chart.invalidate();
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onClick(View v) {
        if(R.id.statistics_month_add == v.getId()){
            if(this.month < 12){
                month++;
                tv_month.setText(month + "月");
                updateData(this.month);
            }
        } else if (R.id.statistics_month_minus == v.getId()) {
            if(this.month > 1){
                month--;
                tv_month.setText(month + "月");
                updateData(this.month);
            }
        }
    }

    private void updateData(int month){
        //获取数据
        ArrayList<PieEntry> income = PlayTaskMainActivity.mDBMaster.mCountDBDao.queryClassMonthIncome(month);
        ArrayList<PieEntry> expend = PlayTaskMainActivity.mDBMaster.mCountDBDao.queryClassMonthExpend(month);
        //设置数据
        setData(incomePieChart, income, "收入情况");
        setData(expendPieChart, expend, "花费情况");
    }
}